package cn.jeesoft.qa.libcore.http;

import java.io.File;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAImageException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.libcore.image.QAImageCallback;
import cn.jeesoft.qa.utils.lang.QAClassUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 网络请求客户端
 * @version v0.1.0 king 2015-03-09 重写实现GET支持文件上传
 */
class HttpClient extends AsyncHttpClient {
    
    public HttpClient() {
        super();

        // 设置超时时间、最大请求次数
        int timeout = QACore.getConfig().getInt(QAConfig.Http.TIMEOUT);
        int retries = QACore.getConfig().getInt(QAConfig.Http.MAX_RETRIES);
        
        setTimeout(timeout);
        setConnectTimeout(timeout);
        setResponseTimeout(timeout);
        
        setMaxRetriesAndTimeout(retries, timeout);
        setURLEncodingEnabled(true);
        setMaxConnections(10);
    }

    @Override
    public RequestHandle get(Context context,
            String url, Header[] headers, RequestParams params,
            ResponseHandlerInterface responseHandler) {
        HttpGet request = new HttpGet(getUrlWithQueryString(isUrlEncodingEnabled(), url, params));
        if (headers != null) request.setHeaders(headers);

        HttpEntity entity = paramsToEntity(params, responseHandler);
        if (entity != null) request.setEntity(entity);

        return sendRequest((DefaultHttpClient)getHttpClient(), getHttpContext(),
                request, null, responseHandler, context);
    }

    @Override
    public RequestHandle get(Context context,
            String url, RequestParams params,
            ResponseHandlerInterface responseHandler) {
        HttpGet request = new HttpGet(getUrlWithQueryString(isUrlEncodingEnabled(), url, params));
        HttpEntity entity = paramsToEntity(params, responseHandler);
        if (entity != null) request.setEntity(entity);

        return sendRequest((DefaultHttpClient)getHttpClient(), getHttpContext(),
                request, null, responseHandler, context);
    }

    private static HttpEntity paramsToEntity(RequestParams params,
            ResponseHandlerInterface responseHandler) {
        HttpEntity entity = null;

        try {
            if (params != null) {
                entity = params.getEntity(responseHandler);
            }
        } catch (IOException e) {
            if (responseHandler != null) {
                responseHandler.sendFailureMessage(0, null, null, e);
            } else {
                e.printStackTrace();
            }
        }

        return entity;
    }
    

    /**
     * 发送网络请求
     * @param context
     * @param method HTTP请求方法
     * @param url 链接地址
     * @param params 参数
     * @param handler 响应处理器
     * @return 请求处理器
     */
    private RequestHandle load(Context context, QAHttpMethod method,
            String url, QARequestParams params, AsyncHttpResponseHandler handler) {
        RequestHandle requestHandle;
        switch (method) {
            case GET:
                requestHandle = this.get(context, url,
                                params==null?null:params.toHeaderArray(),
                                params==null?null:params.mParams, handler);
                break;
                
            case POST:
                requestHandle = this.post(context, url,
                                params==null?null:params.toHeaderArray(),
                                params==null?null:params.mParams, params.mContentType, handler);
                break;

            default:
                requestHandle = null;
                break;
        }
        return requestHandle;
    }

    /**
     * 发送网络请求
     * @param context
     * @param method HTTP请求方法
     * @param url 链接地址
     * @param params 参数
     * @param listener 回调通知接口
     */
    @SuppressWarnings("unchecked")
    public <T> void load(Context context, QAHttpMethod method,
            String url, QARequestParams params,
            QAHttpCallback<T> listener) {
        final Class<? super T> type = DefaultHttp.getResultType(listener);
        
        if (QAClassUtils.isFrom(String.class, type)
                || QAClassUtils.isFrom(QAJson.class, type)) {
            // String\QAJson
            load(context, method, url, params,
                    new TextHandler<T>(url, listener));
        } else if (QAClassUtils.isFrom(File.class, type)) {
            // File
            load(context, method, url, params,
                    new FileHandler(url, (QAHttpCallback<File>) listener, params.mTargetFile));
        } else if (QAClassUtils.isFrom(Bitmap.class, type)) {
            // Bitmap
            final QAHttpCallback<Bitmap> listenerBitmap = (QAHttpCallback<Bitmap>)listener;
            
            View temView = new View(QACore.getApp().getApplication());
            QACore.getImage().load(temView, url, new QAImageCallback() {
                // TODO onSuccessCache 来自缓存
                @Override
                public void onStart(String url, View view) {
                    if (listenerBitmap != null) {
                        listenerBitmap.onStart(url);
                    }
                }
                @Override
                public void onSuccess(String url, View view, Bitmap bitmap) {
                    if (listenerBitmap != null) {
                        listenerBitmap.onSuccessNet(url, bitmap);
                    }
                }
                @Override
                public void onFail(String url, View view, QAImageException exception) {
                    if (listenerBitmap != null) {
                        listenerBitmap.onFail(url, exception);
                    }
                }
                @Override
                public void onCancel(String url, View view) {
                    if (listenerBitmap != null) {
                        listenerBitmap.onCancel(url);
                    }
                }
            });
        } else {
            throw new QANoFoundException(QAException.CODE_NO_SUPPORT,
                    "不支持的自动解析类型[仅支持String/QAJson/File/Bitmap]");
        }
    }
    
    /**
     * 发送网络请求
     * @param context
     * @param method HTTP请求方法
     * @param url 链接地址
     * @param params 参数
     * @param handler 响应处理器
     */
    public <Value, Data> void load(Context context, QAHttpMethod method,
            String url, QARequestParams params,
            QAParser<Value, Data> parser, QAHttpCallback<Value> listener) {
        load(context, method, url, params, new ParserHandler<Value, Data>(url, listener, parser));
    }
    
}
