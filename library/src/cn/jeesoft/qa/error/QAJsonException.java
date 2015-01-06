package cn.jeesoft.qa.error;

/**
 * JSON异常
 * @version v0.1.0 king 2015-01-05 JSON异常
 */
public class QAJsonException extends QAException {
    private static final long serialVersionUID = 1L;

    public QAJsonException(int code) {
        super(code);
    }
    public QAJsonException(int code, String message) {
        super(code, message);
    }
    public QAJsonException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAJsonException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
