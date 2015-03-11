package cn.jeesoft.qa.libcore.http;

import org.apache.http.Header;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAHttpException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.utils.lang.QAClassUtils;

import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 文本响应处理器
 * @version v0.1.0 king 2015-03-09 文本响应处理
 * @param <T>
 */
public class TextHandler<T> extends TextHttpResponseHandler {
    
    final QAHttpCallback<T> listener;
    final String url;
    final Class<? super T> type;
    
    public TextHandler(String url, QAHttpCallback<T> listener) {
        this.url = url;
        this.listener = listener;
        this.type = DefaultHttp.getResultType(listener);
    }
    
    @Override
    public void onStart() {
        this.listener.onStart(this.url);
    }
    @Override
    public void onCancel() {
        this.listener.onCancel(this.url);
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers,
            String responseBody) {
        if (responseBody==null) {
            onFailure(statusCode, headers, responseBody, null);
        } else {
            dispatchSuccess(statusCode, headers, responseBody);
        }
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
            String responseBody, Throwable error) {
        this.listener.onFail(this.url,
                new QAHttpException(QAException.CODE_UNKNOW, statusCode, error));
    }
    
    /**
     * 派发处理请求成功操作
     * @param statusCode 状态码
     * @param headers 头信息
     * @param data 返回数据
     */
    @SuppressWarnings("unchecked")
    void dispatchSuccess(int statusCode, Header[] headers, String data) {
        T value = null;
        try {
            if (QAClassUtils.isFrom(String.class, type)) {
                // 文本
                value = (T) data;
            } else if (QAClassUtils.isFrom(QAJson.class, type)) {
                // JSON
                value = (T) QAJsonUtils.fromJson(data);
            } else {
                throw new QANoFoundException(QAException.CODE_NO_SUPPORT,
                        "不支持的自动解析类型 "+type+" [仅支持String/QAJson/File/Bitmap]");
            }
        } catch (QAException e) {
            onFailure(statusCode, headers, data, e);
        } catch (Throwable e) {
            onFailure(statusCode, headers, data, e);
        } finally {
            if (value != null) {
                listener.onSuccessNet(url, value);
            } else {
                onFailure(statusCode, headers, data,
                        new QANullException("返回数据为空"));
            }
        }
    }

}
