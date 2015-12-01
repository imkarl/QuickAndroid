package cn.jeesoft.qa.libcore.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAJsonException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.manager.QAFileManager;
import cn.jeesoft.qa.utils.lang.QAClassUtils;
import cn.jeesoft.qa.utils.log.QALog;

/**
 * 网络请求客户端
 * @version v0.1.0 king 2015-03-09 重写实现GET支持文件上传
 */
class HttpClient {
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private HttpsDelegate mHttpsDelegate = new HttpsDelegate();
    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
    private DisplayImageDelegate mDisplayImageDelegate = new DisplayImageDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();
    private UploadDelegate mUploadDelegate = new UploadDelegate();
    private PostDelegate mPostDelegate = new PostDelegate();

    public HttpClient() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));

        // 缓存
        File cacheDir = new File(QAFileManager.getUsableDir(QACore.getApp().getPackageName(), "http"));
        int cacheSize = 30 * 1024 * 1024; // 30 MiB
        Cache cache = new Cache(cacheDir, cacheSize);
        mOkHttpClient.setCache(cache);
        mDelivery = new Handler(Looper.getMainLooper());
        /*
        just for test !!!*/
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        });

    }

    public <T> void load(QAHttpMethod method, String url,
                         QARequestParams params, QAHttpCallback<T> listener) {
        if (method == null) {
            method = QAHttpMethod.GET;
        }

        final File targetFile = params==null?null:params.mTargetFile;
        if (method == QAHttpMethod.GET) {
            url = parseGetUrl(url, params!=null?params.mParams:null);
            params = null;
        } else {
            if (params == null) {
                params = new QARequestParams();
            }
        }

        final QAHttpCallback resCallBack = (listener == null) ? DEFAULT_RESULT_CALLBACK : listener;

        Request.Builder builder = new Request.Builder();
        try {
            builder.url(url);
        } catch (final Exception e) {
            final String finalUrl = url;
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    resCallBack.onFail(finalUrl, QAException.make(e));
                }
            });
        }
        // TODO 强制使用缓存
        builder.cacheControl(CacheControl.FORCE_CACHE);
        builder.method(method.name(), params != null ? params.build() : null);
        builder.headers(params != null ? params.toHeaderArray() : new Headers.Builder().build());
        final Request request = builder.build();

        //UI thread
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resCallBack.onStart(request.urlString());
            }
        });
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    Class type = DefaultHttp.getResultType(resCallBack);
                    QALog.e(type);
                    if (QAClassUtils.isFrom(File.class, type)) {
                        if (targetFile != null) {
                            //当前读取字节数
                            long totalBytesRead = 0L;
                            int tipCout = 1;

                            InputStream stream = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            try {
                                ResponseBody responseBody = response.body();
                                stream = responseBody.byteStream();
                                fos = new FileOutputStream(targetFile);
                                while ((len = stream.read(buf)) != -1) {
                                    fos.write(buf, 0, len);

                                    //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                                    totalBytesRead += len != -1 ? len : 0;

                                    if (totalBytesRead / tipCout / 1024 > 0) {
                                        tipCout++;
                                        //回调，如果contentLength()不知道长度，会返回-1
                                        sendProgressCallback(request, totalBytesRead, responseBody.contentLength(), resCallBack);
                                    }
                                }
                                fos.flush();
                                //如果下载文件成功，第一个参数为文件的绝对路径
                                sendSuccessQAHttpCallback(request, targetFile, resCallBack);
                            } catch (IOException e) {
                                sendFailedStringCallback(response.request(), e, resCallBack);
                            } finally {
                                try {
                                    if (stream != null) stream.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null) fos.close();
                                } catch (IOException e) {
                                }
                            }
                        } else {
                            sendFailedStringCallback(response.request(),
                                    new QANoFoundException(QAException.CODE_NULL, "'targetFile' can't be NULL."),
                                    resCallBack);
                        }
                    } else {
                        final String string = response.body().string();
                        if (QAClassUtils.isFrom(String.class, type)) {
                            sendSuccessQAHttpCallback(request, string, resCallBack);
                        } else {
                            Object o = QAJsonUtils.fromJson(string, type);
                            sendSuccessQAHttpCallback(request, o, resCallBack);
                        }
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (QAJsonException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }
    public <V, T> void load(QAHttpMethod method,
                         String url, QARequestParams params,
                         final QAParser<T, V> parser, QAHttpCallback<T> listener) {
        if (method == null) {
            method = QAHttpMethod.GET;
        }

        if (method == QAHttpMethod.GET) {
            url = parseGetUrl(url, params!=null?params.mParams:null);
            params = null;
        } else {
            if (params == null) {
                params = new QARequestParams();
            }
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.method(method.name(), params != null ? params.build() : null);
        builder.headers(params != null ? params.toHeaderArray() : new Headers.Builder().build());
        final Request request = builder.build();

        final QAHttpCallback resCallBack = (listener == null) ? DEFAULT_RESULT_CALLBACK : listener;
        //UI thread
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resCallBack.onStart(request.urlString());
            }
        });
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    Class type = DefaultHttp.getResultType(resCallBack);

                    if (QAClassUtils.isFrom(String.class, type)) {
                        sendSuccessQAHttpCallback(request, parser.parser((V) string), resCallBack);
                    } else {
                        QAJson json = QAJsonUtils.fromJson(string);
                        sendSuccessQAHttpCallback(request, parser.parser((V) json), resCallBack);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (QAJsonException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }

            }
        });
    }

    /**
     * 解析GET方式URL（补充URL请求参数）
     *
     * @param url
     * @param params
     * @return 返回处理后的URL
     */
    private static String parseGetUrl(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();

        if (params == null) {
            params = new HashMap<>();
        }

        if (url.contains("?")) {
            if (url.endsWith("&")) {
                sb.append(url.substring(0, url.length() - 1));
            }
        } else {
            sb.append(url + "?");
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
                    sb.append("&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                QALog.e(e);
            }
        }

        return sb.toString().replace("?&", "?");
    }



    /**
     * ============Get方便的访问方式============
     */
    public Response get(String url) throws IOException
    {
        return mGetDelegate.get(url);
    }

    public String getAsString(String url) throws IOException
    {
        return mGetDelegate.getAsString(url);
    }

    public void getAsyn(String url, QAHttpCallback callback)
    {
        mGetDelegate.getAsyn(url, callback);
    }

    /**
     * ============POST方便的访问方式============
     */
    public Response post(String url, Param... params) throws IOException
    {
        return mPostDelegate.post(url, params);
    }

    public String postAsString(String url, Param... params) throws IOException
    {
        return mPostDelegate.postAsString(url, params);
    }

    public void postAsyn(String url, Param[] params, final QAHttpCallback callback)
    {
        mPostDelegate.postAsyn(url, params, callback);
    }

    public void postAsyn(String url, Map<String, String> params, final QAHttpCallback callback)
    {
        mPostDelegate.postAsyn(url, params, callback);
    }

    public void postAsyn(String url, String bodyStr, final QAHttpCallback callback)
    {
        mPostDelegate.postAsyn(url, bodyStr, callback);
    }

    public void post(String url, String bodyStr, final QAHttpCallback callback) throws IOException
    {
        mPostDelegate.post(url, bodyStr);
    }

    private Param[] validateParam(Param[] params)
    {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(QAHttpCallback callback, final Request request)
    {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final QAHttpCallback resCallBack = callback;
        //UI thread
        callback.onStart(request.urlString());
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response)
            {
                try
                {
                    final String string = response.body().string();
                    Class type = DefaultHttp.getResultType(resCallBack);

                    if (QAClassUtils.isFrom(String.class, type))
                    {
                        sendSuccessQAHttpCallback(request, string, resCallBack);
                    } else
                    {
                        Object o = QAJsonUtils.fromJson(string, type);
                        sendSuccessQAHttpCallback(request, o, resCallBack);
                    }
                } catch (IOException e)
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (QAJsonException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final QAHttpCallback callback)
    {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onFail(request.urlString(), QAException.make(e));
            }
        });
    }

    private void sendSuccessQAHttpCallback(final Request request, final Object object, final QAHttpCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onSuccessNet(request.urlString(), object);
            }
        });
    }

    private void sendProgressCallback(final Request request, final long current, final long total, final QAHttpCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onProgress(request.urlString(), current, total);
            }
        });
    }

    private String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private Request buildPostFormRequest(String url, Param[] params)
    {
        if (params == null)
        {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params)
        {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();


        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password)
    {
        mHttpsDelegate.setCertificates(certificates, bksFile, password);
    }

    /**
     * 设置证书，传入cer文件的inputStream即可
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates)
    {
        mHttpsDelegate.setCertificates(certificates, null, null);
    }

    private final QAHttpCallback<String> DEFAULT_RESULT_CALLBACK = new QAHttpCallback<String>()
    {
        @Override
        public void onStart(String url) {
        }
        @Override
        public void onCancel(String url) {
        }
        @Override
        public void onSuccessCache(String url, String data) {
        }
        @Override
        public void onSuccessNet(String url, String data) {
        }
        @Override
        public void onFail(String url, QAException exception) {
        }
        @Override
        public void onProgress(String url, long current, long total) {
        }
    };


    public static class Param
    {
        public Param()
        {
        }

        public Param(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    //====================PostDelegate=======================
    public class PostDelegate
    {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");

        /**
         * 同步的Post请求
         */
        public Response post(String url, Param... params) throws IOException
        {
            Request request = buildPostFormRequest(url, params);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public void postAsyn(String url, Map<String, String> params, final QAHttpCallback callback)
        {
            Param[] paramsArr = map2Params(params);
            postAsyn(url, paramsArr, callback);
        }

        /**
         * 同步的Post请求
         */
        public String postAsString(String url, Param... params) throws IOException
        {
            Response response = post(url, params);
            return response.body().string();
        }

        /**
         * 异步的post请求
         */
        public void postAsyn(String url, Param[] params, final QAHttpCallback callback)
        {
            Request request = buildPostFormRequest(url, params);
            deliveryResult(callback, request);
        }


        /**
         * 同步的Post请求:直接将bodyStr以写入请求体
         */
        public Response post(String url, String bodyStr) throws IOException
        {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, bodyStr);
            Request request = buildPostRequest(url, body);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 同步的Post请求:直接将bodyFile以写入请求体
         */
        public Response post(String url, File bodyFile) throws IOException
        {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
            Request request = buildPostRequest(url, body);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 同步的Post请求
         */
        public Response post(String url, byte[] bodyBytes) throws IOException
        {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
            Request request = buildPostRequest(url, body);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsyn(String url, String bodyStr, final QAHttpCallback callback)
        {
            postAsyn(url, bodyStr, MediaType.parse("text/plain;charset=utf-8"), callback);
        }

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsyn(String url, String bodyStr, MediaType type, final QAHttpCallback callback)
        {
            RequestBody body = RequestBody.create(type, bodyStr);
            Request request = buildPostRequest(url, body);
            deliveryResult(callback, request);
        }

        /**
         * 直接将bodyFile以写入请求体
         */
        public void postAsyn(String url, File bodyFile, final QAHttpCallback callback)
        {
            postAsyn(url, bodyFile, MediaType.parse("application/octet-stream;charset=utf-8"), callback);
        }

        /**
         * 直接将bodyFile以写入请求体
         */
        public void postAsyn(String url, File bodyFile, MediaType type, final QAHttpCallback callback)
        {
            RequestBody body = RequestBody.create(type, bodyFile);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            deliveryResult(callback, request);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsyn(String url, byte[] bodyBytes, final QAHttpCallback callback)
        {
            postAsyn(url, bodyBytes, MediaType.parse("application/octet-stream;charset=utf-8"), callback);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsyn(String url, byte[] bodyBytes, MediaType type, final QAHttpCallback callback)
        {
            RequestBody body = RequestBody.create(type, bodyBytes);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            deliveryResult(callback, request);
        }

        private Request buildPostRequest(String url, RequestBody body)
        {
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            return request;
        }


    }

    //====================GetDelegate=======================
    public class GetDelegate
    {
        /**
         * 同步的Get请求
         */
        public Response get(String url) throws IOException
        {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 同步的Get请求
         */
        public String getAsString(String url) throws IOException
        {
            Response execute = get(url);
            return execute.body().string();
        }


        /**
         * 异步的get请求
         */
        public void getAsyn(String url, final QAHttpCallback callback)
        {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            deliveryResult(callback, request);
        }
    }


    //====================DisplayImageDelegate=======================

    /**
     * 加载图片相关
     */
    public class DisplayImageDelegate
    {
        /**
         * 加载图片
         */
        public void displayImage(final ImageView view, final String url, final int errorResId)
        {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Request request, IOException e)
                {
                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Response response)
                {
                    InputStream is = null;
                    try
                    {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try
                        {
                            is.reset();
                        } catch (IOException e)
                        {
                            response = mGetDelegate.get(url);
                            is = response.body().byteStream();
                        }

                        BitmapFactory.Options ops = new BitmapFactory.Options();
                        ops.inJustDecodeBounds = false;
                        ops.inSampleSize = inSampleSize;
                        final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                        mDelivery.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                view.setImageBitmap(bm);
                            }
                        });
                    } catch (Exception e)
                    {
                        setErrorResId(view, errorResId);

                    } finally
                    {
                        if (is != null) try
                        {
                            is.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }

        public void displayImage(final ImageView view, String url)
        {
            displayImage(view, url, -1);
        }

        private void setErrorResId(final ImageView view, final int errorResId)
        {
            mDelivery.post(new Runnable()
            {
                @Override
                public void run()
                {
                    view.setImageResource(errorResId);
                }
            });
        }
    }


    //====================UploadDelegate=======================

    /**
     * 上传相关的模块
     */
    public class UploadDelegate
    {

        /**
         * 同步基于post的文件上传:上传多个文件以及携带key-value对：主方法
         */
        public Response post(String url, QARequestParams params) throws IOException
        {
            Request request = buildMultipartFormRequest(url, params);
            return mOkHttpClient.newCall(request).execute();
        }

        /**
         * 异步基于post的文件上传:主方法
         */
        public void postAsyn(String url, QARequestParams params, QAHttpCallback callback)
        {
            Request request = buildMultipartFormRequest(url, params);
            deliveryResult(callback, request);
        }

        private Request buildMultipartFormRequest(String url, QARequestParams params)
        {
            MultipartBuilder builder = params.mBuilder.type(MultipartBuilder.FORM);

            RequestBody requestBody = builder.build();
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }

    }

    //====================DownloadDelegate=======================

    /**
     * 下载相关的模块
     */
    public class DownloadDelegate
    {
        /**
         * 异步下载文件
         *
         * @param url
         * @param destFileDir 本地文件存储的文件夹
         * @param callback
         */
        public void downloadAsyn(final String url, final String destFileDir, final QAHttpCallback callback)
        {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(final Request request, final IOException e)
                {
                    sendFailedStringCallback(request, e, callback);
                }

                @Override
                public void onResponse(Response response)
                {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try
                    {
                        is = response.body().byteStream();

                        File dir = new File(destFileDir);
                        if (!dir.exists())
                        {
                            dir.mkdirs();
                        }
                        File file = new File(dir, getFileName(url));
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1)
                        {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessQAHttpCallback(request, file.getAbsolutePath(), callback);
                    } catch (IOException e)
                    {
                        sendFailedStringCallback(response.request(), e, callback);
                    } finally
                    {
                        try
                        {
                            if (is != null) is.close();
                        } catch (IOException e)
                        {
                        }
                        try
                        {
                            if (fos != null) fos.close();
                        } catch (IOException e)
                        {
                        }
                    }

                }
            });
        }
    }

    //====================HttpsDelegate=======================

    /**
     * Https相关模块
     */
    public class HttpsDelegate
    {
        public TrustManager[] prepareTrustManager(InputStream... certificates)
        {
            if (certificates == null || certificates.length <= 0) return null;
            try
            {

                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates)
                {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    try
                    {
                        if (certificate != null)
                            certificate.close();
                    } catch (IOException e)

                    {
                    }
                }
                TrustManagerFactory trustManagerFactory = null;

                trustManagerFactory = TrustManagerFactory.
                        getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

                return trustManagers;
            } catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            } catch (CertificateException e)
            {
                e.printStackTrace();
            } catch (KeyStoreException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;

        }

        public KeyManager[] prepareKeyManager(InputStream bksFile, String password)
        {
            try
            {
                if (bksFile == null || password == null) return null;

                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();

            } catch (KeyStoreException e)
            {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e)
            {
                e.printStackTrace();
            } catch (CertificateException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        public void setCertificates(InputStream[] certificates, InputStream bksFile, String password)
        {
            try
            {
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                SSLContext sslContext = SSLContext.getInstance("TLS");

                sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
                mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            } catch (KeyManagementException e)
            {
                e.printStackTrace();
            } catch (KeyStoreException e)
            {
                e.printStackTrace();
            }
        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers)
        {
            for (TrustManager trustManager : trustManagers)
            {
                if (trustManager instanceof X509TrustManager)
                {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }


        public class MyTrustManager implements X509TrustManager
        {
            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException
            {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
                try
                {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce)
                {
                    localTrustManager.checkServerTrusted(chain, authType);
                }
            }


            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }
        }

    }

    //====================ImageUtils=======================
    public static class ImageUtils
    {
        /**
         * 根据InputStream获取图片实际的宽度和高度
         *
         * @param imageStream
         * @return
         */
        public static ImageSize getImageSize(InputStream imageStream)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);
            return new ImageSize(options.outWidth, options.outHeight);
        }

        public static class ImageSize
        {
            int width;
            int height;

            public ImageSize()
            {
            }

            public ImageSize(int width, int height)
            {
                this.width = width;
                this.height = height;
            }

            @Override
            public String toString()
            {
                return "ImageSize{" +
                        "width=" + width +
                        ", height=" + height +
                        '}';
            }
        }

        public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize)
        {
            // 源图片的宽度
            int width = srcSize.width;
            int height = srcSize.height;
            int inSampleSize = 1;

            int reqWidth = targetSize.width;
            int reqHeight = targetSize.height;

            if (width > reqWidth && height > reqHeight)
            {
                // 计算出实际宽度和目标宽度的比率
                int widthRatio = Math.round((float) width / (float) reqWidth);
                int heightRatio = Math.round((float) height / (float) reqHeight);
                inSampleSize = Math.max(widthRatio, heightRatio);
            }
            return inSampleSize;
        }

        /**
         * 计算合适的inSampleSize
         */
        public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ImageView imageView)
        {
            final int srcWidth = srcSize.width;
            final int srcHeight = srcSize.height;
            final int targetWidth = targetSize.width;
            final int targetHeight = targetSize.height;

            int scale = 1;

            if (imageView == null)
            {
                scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
            } else
            {
                switch (imageView.getScaleType())
                {
                    case FIT_CENTER:
                    case FIT_XY:
                    case FIT_START:
                    case FIT_END:
                    case CENTER_INSIDE:
                        scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
                        break;
                    case CENTER:
                    case CENTER_CROP:
                    case MATRIX:
                        scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // min
                        break;
                    default:
                        scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
                        break;
                }
            }

            if (scale < 1)
            {
                scale = 1;
            }

            return scale;
        }

        /**
         * 根据ImageView获适当的压缩的宽和高
         *
         * @param view
         * @return
         */
        public static ImageSize getImageViewSize(View view)
        {

            ImageSize imageSize = new ImageSize();

            imageSize.width = getExpectWidth(view);
            imageSize.height = getExpectHeight(view);

            return imageSize;
        }

        /**
         * 根据view获得期望的高度
         *
         * @param view
         * @return
         */
        private static int getExpectHeight(View view)
        {

            int height = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                height = view.getWidth(); // 获得实际的宽度
            }
            if (height <= 0 && params != null)
            {
                height = params.height; // 获得布局文件中的声明的宽度
            }

            if (height <= 0)
            {
                height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
            }

            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (height <= 0)
            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                height = displayMetrics.heightPixels;
            }

            return height;
        }

        /**
         * 根据view获得期望的宽度
         *
         * @param view
         * @return
         */
        private static int getExpectWidth(View view)
        {
            int width = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                width = view.getWidth(); // 获得实际的宽度
            }
            if (width <= 0 && params != null)
            {
                width = params.width; // 获得布局文件中的声明的宽度
            }

            if (width <= 0)

            {
                width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
            }
            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (width <= 0)

            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                width = displayMetrics.widthPixels;
            }

            return width;
        }

        /**
         * 通过反射获取imageview的某个属性值
         *
         * @param object
         * @param fieldName
         * @return
         */
        private static int getImageViewFieldValue(Object object, String fieldName)
        {
            int value = 0;
            try
            {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = field.getInt(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
                {
                    value = fieldValue;
                }
            } catch (Exception e)
            {
            }
            return value;

        }
    }

}
