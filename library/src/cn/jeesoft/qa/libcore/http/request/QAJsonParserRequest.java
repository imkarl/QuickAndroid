package cn.jeesoft.qa.libcore.http.request;

import java.util.Map;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.libcore.http.QAParser;

/**
 * 支持GET、POST的请求
 * 
 * @param <V> 返回值类型
 * @param <P> 解析器类型
 * @version v0.1.0 king 2015-01-12 支持GET、POST
 */
public class QAJsonParserRequest<V, P extends QAParser<V, QAJson>> extends QARequest<V> {
    
    private final P mParser;

    public QAJsonParserRequest(QAHttpMethod method,
            String url,
            Map<String, String> params,
            P parser,
            QAHttpCallback<V> listener) {
        super(method, url, params, listener);
        this.mParser = parser;
    }
    public QAJsonParserRequest(QAHttpMethod method,
            String url,
            P parser,
            QAHttpCallback<V> listener) {
        super(method, url, null, listener);
        this.mParser = parser;
    }
    public QAJsonParserRequest(String url,
            Map<String, String> params,
            P parser,
            QAHttpCallback<V> listener) {
        super(QAHttpMethod.GET, url, params, listener);
        this.mParser = parser;
    }
    public QAJsonParserRequest(String url,
            P parser,
            QAHttpCallback<V> listener) {
        super(QAHttpMethod.GET, url, null, listener);
        this.mParser = parser;
    }
    
    @Override
    V parseResultData(String resultData) {
        return (V) mParser.parser(QAJsonUtils.fromJson(resultData));
    }
    
    public P getParser() {
        return this.mParser;
    }
    
}
