package cn.jeesoft.qa.libcore.http;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.libcore.http.request.QAJsonParserRequest;
import cn.jeesoft.qa.libcore.http.request.QAJsonRequest;
import cn.jeesoft.qa.libcore.http.request.QARequest;
import cn.jeesoft.qa.libcore.http.request.QAStringParserRequest;
import cn.jeesoft.qa.libcore.http.request.QAStringRequest;
import cn.jeesoft.qa.utils.lang.QAClassUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

/**
 * HTTP网络请求
 * @version v0.1.0 king 2015-01-10 HTTP网络请求包装
 */
public class QAHttp {
    
    /** 用于保存所有的请求队列 */
    private RequestQueue mRequestQueue;
    
    public QAHttp(QAPrivateCheck check) {
        super();
        QAPrivateCheck.check(check);
    }

    /**
     * 获取RequestQueue
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (RequestQueue.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = newRequestQueue(QACore.getApp().getApplication(),
                            new File(QACore.getUsableDir("http")));
                }
            }
        }
        return mRequestQueue;
    }
    

    /**
     * 添加指定的请求到RequestQueue,可以指定标签，否则使用默认标签
     */
    @SuppressWarnings("rawtypes")
    public void load(String tag, QARequest<?> request) {
        if (request == null) {
            throw new QANullException("'request' can not be NULL.");
        }
        
        if (TextUtils.isEmpty(tag)) {
            if (request instanceof QAStringParserRequest) {
                tag = ((QAStringParserRequest)request).getParser().getClass().getName();
            } else if (request instanceof QAJsonParserRequest) {
                tag = ((QAJsonParserRequest)request).getParser().getClass().getName();
            }
        }
        
        // 如果标签是空的，设置为默认标签
        request.setTag(TextUtils.isEmpty(tag) ? QACore.getApp().getPackageName() : tag);
        // 设置超时时间、最大请求次数、退避指数
        request.setRetryPolicy(new DefaultRetryPolicy(QACore.getConfig().getInt(QAConfig.Http.TIMEOUT),
                QACore.getConfig().getInt(QAConfig.Http.MAX_RETRIES),
                QACore.getConfig().getFloat(QAConfig.Http.BACKOFF_MULTIPLIER)));
        getRequestQueue().add(request);
    }
    /**
     * 添加指定的请求到RequestQueue,可以指定标签，否则使用默认标签
     */
    public void load(QARequest<?> request) {
        load(null, request);
    }
    /**
     * 发送网络请求
     * @param method
     * @param url 请求的URL
     * @param params
     * @param parser
     * @param listener
     */
    public <V, P extends QAParser<V, QAJson>> void load(QAHttpMethod method,
            String url,
            Map<String, String> params,
            P parser,
            QAHttpCallback<V> listener) {
        load(new QAJsonParserRequest<V, P>(method, url, params, parser, listener));
    }
    /**
     * 发送网络请求
     * 
     * <pre>
     * 仅支持String/QAJson/File/Bitmap
     * </pre>
     * 
     * @param method
     * @param url 请求的URL
     * @param params
     * @param listener
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <V> void load(QAHttpMethod method,
            String url,
            Map<String, String> params,
            QAHttpCallback<V> listener) {
        final Class<V> type;
        try {
            type = QAClassUtils.getGenericInterfaces(listener.getClass())[0];
        } catch (Exception e) {
            throw new QANoFoundException(QAException.CODE_NO_TYPE, "找不到自动解析类型[仅支持String/QAJson/File/Bitmap]", e);
        }
        
        if (QAClassUtils.isFrom(String.class, type)) {
            load(new QAStringRequest(method, url, params, (QAHttpCallback<String>)listener));
        } else if (QAClassUtils.isFrom(QAJson.class, type)) {
            load(new QAJsonRequest(method, url, params, listener));
        } else if (QAClassUtils.isFrom(File.class, type)) {
            // TODO 文件
//            load(new QAJsonRequest(method, url, params, listener));
            throw new QAException(QAException.CODE_UNKNOW);
        } else if (QAClassUtils.isFrom(Bitmap.class, type)) {
            // TODO 图片
//            load(new QAJsonRequest(method, url, params, listener));
            throw new QAException(QAException.CODE_UNKNOW);
        } else {
            throw new QANoFoundException(QAException.CODE_NO_SUPPORT, "不支持的自动解析类型[仅支持String/QAJson/File/Bitmap]");
        }
    }

    
    
    



    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @return A started {@link RequestQueue} instance.
     */
    private static RequestQueue newRequestQueue(Context context, File cacheDir, HttpStack stack) {
        if (cacheDir == null || !cacheDir.exists()) {
            throw new QANullException("'cacheDir' cannot be empty.");
        } else if (!cacheDir.isDirectory()) {
            throw new QANullException("'cacheDir' must be Directory.");
        }
        
        String userAgent = "QuickAndroid/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        queue.start();

        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    private static RequestQueue newRequestQueue(Context context, File cacheDir) {
        return newRequestQueue(context, cacheDir, null);
    }
    
}
