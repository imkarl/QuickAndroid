package cn.jeesoft.qa.error;

/**
 * IO异常
 * @version v0.1.0 king 2015-12-06 IO异常
 */
public class QAIOException extends QAException {
    private static final long serialVersionUID = 1L;

    public QAIOException(int code) {
        super(code);
    }
    public QAIOException(int code, String message) {
        super(code, message);
    }
    public QAIOException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAIOException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
