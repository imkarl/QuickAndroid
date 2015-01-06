package cn.jeesoft.qa.error;

/**
 * APP异常
 * @version v0.1.0 king 2015-01-05 APP异常
 */
public class QAAppException extends QAException {
    private static final long serialVersionUID = 1L;
    
    public QAAppException(int code) {
        super(code);
    }
    public QAAppException(int code, String message) {
        super(code, message);
    }
    public QAAppException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAAppException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
