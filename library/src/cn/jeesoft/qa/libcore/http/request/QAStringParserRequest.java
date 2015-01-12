package cn.jeesoft.qa.libcore.http.request;

import java.util.Map;

import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.libcore.http.QAParser;

/**
 * 支持GET、POST的请求
 * 
 * @param <V> 返回值类型
 * @param <P> 解析器类型
 * @version v0.1.1 king 2015-01-12 代码重新组织
 * @version v0.1.0 king 2015-01-12 支持GET、POST
 */
public class QAStringParserRequest<V, P extends QAParser<V, String>> extends QARequest<V> {
    
    private final P mParser;

    public QAStringParserRequest(QAHttpMethod method,
            String url,
            Map<String, String> params,
            P parser,
            QAHttpCallback<V> listener) {
        super(method, url, params, listener);
        this.mParser = parser;
    }
    public QAStringParserRequest(QAHttpMethod method,
            String url,
            P parser,
            QAHttpCallback<V> listener) {
        super(method, url, null, listener);
        this.mParser = parser;
    }
    public QAStringParserRequest(String url,
            Map<String, String> params,
            P parser,
            QAHttpCallback<V> listener) {
        super(QAHttpMethod.GET, url, params, listener);
        this.mParser = parser;
    }
    public QAStringParserRequest(String url,
            P parser,
            QAHttpCallback<V> listener) {
        super(QAHttpMethod.GET, url, null, listener);
        this.mParser = parser;
    }
    
    @Override
    V parseResultData(String resultData) {
        return mParser.parser(resultData);
    }
    
    public P getParser() {
        return this.mParser;
    }
    
}
