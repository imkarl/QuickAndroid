package cn.jeesoft.qa.error;

import java.net.UnknownHostException;


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
    public static QAException make(Throwable exception) {
        if (exception == null) {
            return null;
        }
        
        if (exception instanceof NullPointerException) {
            return new QANullException(exception.getMessage(), exception.getCause());
        } else if (exception instanceof com.alibaba.fastjson.JSONException
                || exception instanceof org.json.JSONException) {
            return new QAJsonException(QAException.CODE_FORMAT, exception.getMessage(), exception.getCause());
        } else if (exception instanceof UnknownHostException) {
            return new QAHttpException(QAException.CODE_FORMAT, -1, exception);
        } else {
            // TODO 未细化处理
            return new QAException(QAException.CODE_UNKNOW, exception.getMessage(), exception.getCause());
        }
    }
    
    
}
