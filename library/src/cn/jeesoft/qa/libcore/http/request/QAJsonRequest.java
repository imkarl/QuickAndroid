package cn.jeesoft.qa.libcore.http.request;

import java.util.Map;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;

/**
 * 
 * 支持GET、POST的JSON请求
 * 
 * <pre>
 * 传参方式：
 * new QAJsonRequest(Request.Method.POST,
 *     "http://you_domain/action/login.action",
 *     params,
 *     listener);
 * </pre>
 * 
 * @param <V> 返回值类型
 * @version v0.1.1 king 2015-01-12 代码重新组织
 * @version v0.1.0 king 2014-12-15 支持GET、POST
 */
public class QAJsonRequest<V extends QAJson> extends QARequest<V> {

    public QAJsonRequest(QAHttpMethod method,
            String url,
            Map<String, String> params,
            final QAHttpCallback<V> listener) {
        super(method, url, params, listener);
    }
	public QAJsonRequest(QAHttpMethod method,
	        String url,
	        QAHttpCallback<V> listener) {
	    this(method, url, null, listener);
	}
	public QAJsonRequest(String url,
	        Map<String, String> params,
	        QAHttpCallback<V> listener) {
	    this(QAHttpMethod.GET, url, params, listener);
	}
	public QAJsonRequest(String url,
	        QAHttpCallback<V> listener) {
	    this(QAHttpMethod.GET, url, null, listener);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	V parseResultData(String resultData) {
	    return (V)QAJsonUtils.fromJson(resultData);
	}
	
}
