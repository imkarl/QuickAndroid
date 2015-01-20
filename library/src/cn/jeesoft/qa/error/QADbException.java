package cn.jeesoft.qa.error;

/**
 * 数据库操作异常
 * @version v0.1.0 king 2015-01-19 数据库操作异常
 */
public class QADbException extends QAException {
    private static final long serialVersionUID = 1L;

    public QADbException(int code) {
        super(code);
    }
    public QADbException(int code, String message) {
        super(code, message);
    }
    public QADbException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QADbException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
