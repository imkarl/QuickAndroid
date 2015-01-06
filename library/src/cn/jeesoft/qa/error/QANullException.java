package cn.jeesoft.qa.error;

/**
 * 空指针异常
 * @version v0.1.0 king 2015-01-05 空指针异常
 */
public class QANullException extends QAException {
    private static final long serialVersionUID = 1L;

    public QANullException() {
        super(CODE_NULL);
    }
    public QANullException(String message) {
        super(CODE_NULL, message);
    }
    public QANullException(String message, Throwable throwable) {
        super(CODE_NULL, message, throwable);
    }
    public QANullException(Throwable throwable) {
        super(CODE_NULL, throwable);
    }
    
}
