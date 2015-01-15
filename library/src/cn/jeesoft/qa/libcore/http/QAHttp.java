package cn.jeesoft.qa.libcore.http;

import java.util.Map;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.libcore.http.request.QARequest;

/**
 * HTTP网络请求
 * @version v0.1.1 king 2015-01-14 定义接口
 * @version v0.1.0 king 2015-01-10 HTTP网络请求包装
 */
public interface QAHttp {

    /**
     * 添加指定的请求到RequestQueue,可以指定标签，否则使用默认标签
     */
    public void load(String tag, QARequest<?> request);
    
    /**
     * 添加指定的请求到RequestQueue,可以指定标签，否则使用默认标签
     */
    public void load(QARequest<?> request);
    
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
            QAHttpCallback<V> listener);
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
    public <V> void load(QAHttpMethod method,
            String url,
            Map<String, String> params,
            QAHttpCallback<V> listener);
    
}
