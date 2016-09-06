package cn.jeesoft.qa.libcore.http.okhttp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android.graphics.Bitmap;
import android.text.TextUtils;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoSupportException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonArray;
import cn.jeesoft.qa.json.QAJsonObject;
import cn.jeesoft.qa.libcore.handle.QAHandle;
import cn.jeesoft.qa.libcore.http.DefaultHttp;
import cn.jeesoft.qa.libcore.http.QAHttpAction;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.libcore.http.QARequestParams;
import cn.jeesoft.qa.libcore.http.handler.QABimtapHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAFastJsonHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAFileHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAJsonArrayHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAJsonObjectHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAJsonHttpHandler;
import cn.jeesoft.qa.libcore.http.handler.QAStringHttpHandler;
import cn.jeesoft.qa.libcore.http.part.Part;
import cn.jeesoft.qa.libcore.http.part.StringPart;
import cn.jeesoft.qa.manager.QAFileManager;
import cn.jeesoft.qa.utils.lang.QAStringUtils;
import cn.jeesoft.qa.utils.log.QALog;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 网络请求客户端
 */
public class OkHttp {

    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");

    /**
     * 进度监听
     */
    public static interface OnProgressListener {
        public void onProgress(long currentBytes, long contentLength);
    }

    private OkHttpClient mOkHttpClient;
    private QAHandle mDelivery;
    private final ConcurrentHashMap<Class, QAHttpHandler> mHttpHandlers = new ConcurrentHashMap<Class, QAHttpHandler>();
    private ConcurrentHashMap<Request, OnProgressListener> mOnProgressListeners = new ConcurrentHashMap<Request, OnProgressListener>() {
		public OnProgressListener get(Object key) {
    		OnProgressListener listener = super.get(key);
    		if (listener == null) {
        		if (key instanceof Request) {
    	    		Object tag = ((Request) key).tag();
	                if (tag != null && tag instanceof Request) {
	                	listener = super.get(tag);
	                }
        		}
    		}
    		return listener;
    	};
    };

    public OkHttp() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));

        // 缓存
        File cacheDir = new File(QAFileManager.getUsableDir(QACore.getApp().getPackageName(), "http"));
        int cacheSize = 30 * 1024 * 1024; // 30 MiB
        Cache cache = new Cache(cacheDir, cacheSize);
        mOkHttpClient.setCache(cache);
        mDelivery = QACore.getHandler();
        /* just for test !!!*/
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
            	
                //包装响应体并返回
                Response response = originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), mOnProgressListeners.get(request)))
                        .build();
                mOnProgressListeners.remove(request);
                return response;
            }
        });
        
        registerHttpHandlers();
    }
    
    private void registerHttpHandlers() {
        registerHttpHandler(String.class, new QAStringHttpHandler());
        registerHttpHandler(File.class, new QAFileHttpHandler());
        registerHttpHandler(Bitmap.class, new QABimtapHttpHandler());

        registerHttpHandler(QAJson.class, new QAJsonHttpHandler());
        registerHttpHandler(QAJsonObject.class, new QAJsonHttpHandler());
        registerHttpHandler(QAJsonArray.class, new QAJsonHttpHandler());
        registerHttpHandler(org.json.JSONObject.class, new QAJsonObjectHttpHandler());
        registerHttpHandler(org.json.JSONArray.class, new QAJsonArrayHttpHandler());
        registerHttpHandler(com.alibaba.fastjson.JSONObject.class, new QAFastJsonHttpHandler());
        registerHttpHandler(com.alibaba.fastjson.JSONArray.class, new QAFastJsonHttpHandler());
    }

    /**
     * 读取缓存
     * @param request
     * @return
     */
    public Response getCache(Request request) {
		return OkHttpCacheHelper.getCache(mOkHttpClient, request);
    }
    
    /**
     * 设置HTTPS证书
     * @param certificates
     * @param bksFile
     * @param password
     */
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
    	HttpsHelper.setCertificates(mOkHttpClient, certificates, bksFile, password);
    }
    
    public <T> void registerHttpHandler(Class<? extends T> type, QAHttpHandler<T> handler) {
    	mHttpHandlers.put(type, handler);
    }

    /**
     * 异步请求
     *
     * @param method
     * @param url
     * @param params
     * @param listener
     * @param <T>
     */
    public <T> void load(QAHttpMethod method, final String url,
                         final QARequestParams params, final QAHttpCallback<T> listener) {
        final Request request = createRequest(method, url, params, listener);
        if (request == null) {
            return;
        }

        if (params != null && params.isPreLoad()) {
            // 缓存加载
        	preLoad(url, params, request, listener);
        }
        
        // 网络加载
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedCallback(url, e, listener);
            }
            @Override
            public void onResponse(final Response response) {
                handlerResponse(url, params, response, listener);
            }
        });
    }

    /**
     * 同步请求
     *
     * @param method
     * @param url
     * @param params
     * @param listener
     * @param <T>
     */
    public <T> void loadSync(QAHttpMethod method, final String url,
                             QARequestParams params, final QAHttpCallback<T> listener) {
        final Request request = createRequest(method, url, params, listener);
        if (request == null) {
            return;
        }

        if (params != null && params.isPreLoad()) {
            // 缓存加载
        	preLoad(url, params, request, listener);
        }
        
        // 网络加载
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            handlerResponse(url, params, response, listener);
        } catch (Throwable e) {
            sendFailedCallback(url, e, listener);
        }
    }
    
    /**
     * 预加载
     * @param <T>
     * @param url
     * @param request
     * @param listener
     */
    private <T> void preLoad(final String url, final QARequestParams params, final Request request, final QAHttpCallback<T> listener) {
        mOkHttpClient.getDispatcher().getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
	            Class<T> type = DefaultHttp.getResultType(listener);
	            QAHttpHandler<T> httpHandler = getHttpHandler(params, type);
	            if (httpHandler != null) {
			        // 读缓存
			        try {
			            Response response = getCache(request);
			            sendSuccessCacheCallback(url, httpHandler.handlerResponse(response), listener);
			        } catch (Throwable e) {
			        }
	            }
			}
		});
    }
    
    private <T> void handlerResponse(String url, QARequestParams params, Response response, QAHttpCallback<T> listener) {
        try {
            Class<T> type = DefaultHttp.getResultType(listener);
            QAHttpHandler<T> httpHandler = getHttpHandler(params, type);
            if (httpHandler == null) {
                sendFailedCallback(url, new QANoSupportException(QAException.CODE_NO_SUPPORT, "不支持的类型'"+type+"'"), listener);
            }
            sendSuccessNetCallback(url, httpHandler.handlerResponse(response), listener);
        } catch (Throwable e) {
            sendFailedCallback(url, e, listener);
        }
    }
    
    private <T> QAHttpHandler<T> getHttpHandler(QARequestParams params, Class<T> type) {
        @SuppressWarnings("unchecked")
		QAHttpHandler<T> httpHandler = mHttpHandlers.get(type);
        if (httpHandler != null && httpHandler instanceof QAFileHttpHandler) {
        	((QAFileHttpHandler)httpHandler).setTargetFile(params.getTargetFile());
        }
        return httpHandler;
    }

    private <T> Request createRequest(QAHttpMethod method, String url,
                                      QARequestParams params, final QAHttpCallback<T> listener) {
        if (method == null) {
            method = QAHttpMethod.GET;
        }

        final String finalUrl = url;
        if (method == QAHttpMethod.GET) {
            url = parseGetUrl(url, params != null ? params.getParams() : null);
            params = null;
        } else {
            if (params == null) {
                params = new QARequestParams();
            }
        }

        Request.Builder builder = new Request.Builder();
        try {
            builder.url(url);
        } catch (final Exception e) {
            sendFailedCallback(finalUrl, e, listener);
            return null;
        }

        // 强制使用缓存
        builder.cacheControl(CacheControl.FORCE_CACHE);
        
        // header
        Headers.Builder headerBuilder = new Headers.Builder();
        if (params != null && !params.getHeaders().isEmpty()) {
        	for (Entry<String, String> entry : params.getHeaders().entrySet()) {
        		headerBuilder.add(entry.getKey(), entry.getValue());
        	}
        }
        builder.headers(headerBuilder.build());
        
        // param
        RequestBody requestBody = null;
        if (params != null) {
	        if (!TextUtils.isEmpty(params.getBody())) {
	        	requestBody = RequestBody.create(MEDIA_TYPE_TEXT, params.getBody());
	        } else {
	            try {
	            	MultipartBuilder paramBuilder = new MultipartBuilder();
	                if (params != null && !params.getParams().isEmpty()) {
	                	for (Entry<String, List<Part>> entry : params.getParams().entrySet()) {
	                		String name = entry.getKey();
	                		List<Part> parts = entry.getValue();
	                        if (parts != null && !parts.isEmpty()) {
	                        	for (Part part : parts) {
	    					        paramBuilder.addPart(part.header(), part.body());
	                        	}
	                        }
	                	}
	                }
	            	
	            	requestBody = paramBuilder.build();
	            } catch (IllegalStateException e) {
	            	requestBody = new FormEncodingBuilder().build();
	            }
	        }
        }
        
        builder.method(method.name(), requestBody == null ? null : new ProgressRequestBody(requestBody, new OnProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength) {
                // 上传进度
                sendProgressCallback(finalUrl, currentBytes, contentLength, QAHttpAction.REQUEST, listener);
            }
        }));

        final Request request = builder.build();
        mOnProgressListeners.put(request, new OnProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength) {
                // 下载进度
                sendProgressCallback(finalUrl, currentBytes, contentLength, QAHttpAction.RESPONSE, listener);
            }
        });
        return request;
    }


    /**
     * 解析GET方式URL（补充URL请求参数）
     *
     * @param url
     * @param params
     * @return 返回处理后的URL
     */
    private static String parseGetUrl(String url, Map<String, List<Part>> params) {
        StringBuilder sb = new StringBuilder();

        if (params == null) {
            params = new HashMap<String, List<Part>>();
        }

        if (url.contains("?")) {
            if (url.endsWith("&")) {
                sb.append(url.substring(0, url.length() - 1));
            }
        } else {
            sb.append(url + "?");
        }

        for (Entry<String, List<Part>> entry : params.entrySet()) {
    		String name = entry.getKey();
    		List<Part> parts = entry.getValue();
            try {
                if (!TextUtils.isEmpty(name) && !QAStringUtils.isEmpty(parts)) {
                	for (Part part : parts) {
                		if (part instanceof StringPart) {
	                		sb.append("&" + URLEncoder.encode(part.name(), "UTF-8")
	                				+ "=" + URLEncoder.encode(((StringPart) part).value(), "UTF-8"));
                		}
                	}
                }
            } catch (UnsupportedEncodingException e) {
                QALog.e(e);
            }
        }

        return sb.toString().replace("?&", "?");
    }
    
    
    

    private void sendFailedCallback(final String url, final Throwable e, final QAHttpCallback callback) {
        if (callback == null) {
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onFail(url, QAException.make(e));
            }
        });
    }

    private void sendSuccessNetCallback(final String url, final Object object, final QAHttpCallback callback) {
        if (callback == null) {
            return;
        }
        if (object == null) {
            sendFailedCallback(url, new QANullException("数据解析为空"), callback);
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccessNet(url, object);
            }
        });
    }
    private void sendSuccessCacheCallback(final String url, final Object object, final QAHttpCallback callback) {
        if (callback == null) {
            return;
        }
        if (object == null) {
            sendFailedCallback(url, new QANullException("数据解析为空"), callback);
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccessCache(url, object);
            }
        });
    }

    private void sendProgressCallback(final String url, final long current, final long total, final QAHttpAction action, final QAHttpCallback callback) {
        if (callback == null) {
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(url, current, total, action);
            }
        });
    }

}
