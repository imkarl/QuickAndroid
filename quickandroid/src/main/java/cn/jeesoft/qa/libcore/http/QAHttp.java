package cn.jeesoft.qa.libcore.http;

import android.content.Context;

/**
 * HTTP网络请求
 * @version v0.1.1 king 2015-01-14 定义接口
 * @version v0.1.0 king 2015-01-10 HTTP网络请求包装
 */
public interface QAHttp {
    
    public <T> void load(QAHttpMethod method,
                         String url, QARequestParams params,
                         QAStringParser<T> parser, QAHttpCallback<T> listener);
    
    public <T> void load(QAHttpMethod method,
                         String url, QARequestParams params,
                         QAJsonParser<T> parser, QAHttpCallback<T> listener);
    
    /**
     * 发送网络请求
     * @param method
     * @param url 请求的URL
     * @param params
     * @param listener
     */
    public <T> void load(QAHttpMethod method, String url, QARequestParams params, QAHttpCallback<T> listener);

    public <T> void load(String url, QARequestParams params, QAHttpCallback<T> listener);

    public <T> void load(QAHttpMethod method, String url, QAHttpCallback<T> listener);

    public <T> void load(String url, QAHttpCallback<T> listener);

//    /**
//     * 清空Cookie
//     * @param <T>
//     */
//    public <T> void clearCookie();

}
