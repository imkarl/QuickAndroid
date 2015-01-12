package cn.jeesoft.qa.libcore.http.request;

import java.util.Map;

import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;

/**
 * 支持GET、POST的字符串请求
 * 
 * <pre>
 * 传参方式：
 * new QAStringRequest(QAHttpMethod.POST,
 *     "http://you_domain/action/login.action",
 *     params,
 *     listener);
 * </pre>
 * 
 * @version v0.1.0 king 2015-01-12 支持GET、POST
 */
public class QAStringRequest extends QARequest<String> {

    public QAStringRequest(QAHttpMethod method,
            String url,
            Map<String, String> params,
            final QAHttpCallback<String> listener) {
        super(method, url, params, listener);
    }
	public QAStringRequest(QAHttpMethod method,
	        String url,
	        QAHttpCallback<String> listener) {
	    this(method, url, null, listener);
	}
	public QAStringRequest(String url,
	        Map<String, String> params,
	        QAHttpCallback<String> listener) {
	    this(QAHttpMethod.GET, url, params, listener);
	}
	public QAStringRequest(String url,
	        QAHttpCallback<String> listener) {
	    this(QAHttpMethod.GET, url, null, listener);
	}
	
	@Override
	String parseResultData(String resultData) {
	    return resultData;
	}
	
}
