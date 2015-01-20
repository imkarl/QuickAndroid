package cn.jeesoft.qa.error;

/**
 * 不支持异常
 * @version v0.1.0 king 2015-01-12 不支持异常
 */
public class QANoSupportException extends QAException {
    private static final long serialVersionUID = 1L;

    public QANoSupportException(int code) {
        super(code);
    }
    public QANoSupportException(int code, String message) {
        super(code, message);
    }
    public QANoSupportException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QANoSupportException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
