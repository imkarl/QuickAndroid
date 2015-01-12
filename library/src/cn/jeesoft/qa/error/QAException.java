package cn.jeesoft.qa.error;

import cn.jeesoft.qa.libcore.http.request.QAVolleyError;

import com.android.volley.VolleyError;

/**
 * 全局异常
 * @version v0.1.0 king 2015-01-05 定义全局异常
 */
public class QAException extends Exception implements ExceptionCode {
    private static final long serialVersionUID = 1L;
    
    public QAException(int code) {
        super(code);
    }
    public QAException(int code, String message) {
        super(code, message);
    }
    public QAException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
    
    
    /**
     * 生成QAException实例
     */
    public static QAException make(java.lang.Exception exception) {
        if (exception == null) {
            return null;
        }
        
        if (exception instanceof VolleyError) {
            VolleyError volleyError = (VolleyError) exception;
            if (exception instanceof QAVolleyError
                    && ((QAVolleyError) volleyError).getCause() instanceof QAException) {
                    return (QAException) ((QAVolleyError) volleyError).getCause();
            } else {
                return new QAHttpException(QAException.CODE_UNKNOW,
                        volleyError.getCause(),
                        volleyError.networkResponse);
            }
        } else {
            // TODO 未细化处理
            return new QAException(QAException.CODE_UNKNOW, exception);
        }
    }
    
    
    
}
