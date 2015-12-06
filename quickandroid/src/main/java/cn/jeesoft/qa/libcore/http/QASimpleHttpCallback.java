package cn.jeesoft.qa.libcore.http;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.utils.lang.QAStringUtils;
import cn.jeesoft.qa.utils.log.QALog;

/**
 * @version 0.1 king 2015-10
 */
public abstract class QASimpleHttpCallback<T> implements QAHttpCallback<T> {

    @Override
    public void onCancel(String url) {
    	QALog.e("onCancel | "+url);
    }

    @Override
    public void onSuccessCache(String url, T data) {
    	QALog.e("onSuccessCache | "+url+", "+cut(data));
    }

    @Override
    public void onSuccessNet(String url, T data) {
    	QALog.e("onSuccessNet | "+url+", "+cut(data));
    }
    
    private static final String cut(Object data) {
    	if (QAStringUtils.isEmpty(data)) {
    		return "";
    	}
    	String val = String.valueOf(data);
    	if (val.length() > 100) {
    		val = val.substring(0, 100);
    	}
    	return val;
    }

    @Override
    public void onFail(String url, QAException exception) {
    	QALog.e("onFail | "+url, exception);
    }

    @Override
    public void onProgress(String url, long current, long total,
    		QAHttpAction action) {
    	QALog.e("onProgress | "+action+" | "+current+"/"+total);
    }
}
