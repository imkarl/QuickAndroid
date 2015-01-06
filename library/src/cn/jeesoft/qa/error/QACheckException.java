package cn.jeesoft.qa.error;

/**
 * 验证失败异常
 * @version v0.1.0 king 2015-01-06 验证失败
 */
public class QACheckException extends QAException {
    private static final long serialVersionUID = 1L;

    public QACheckException() {
        super(CODE_CHECK);
    }
    public QACheckException(String message) {
        super(CODE_CHECK, message);
    }
    public QACheckException(String message, Throwable throwable) {
        super(CODE_CHECK, message, throwable);
    }
    public QACheckException(Throwable throwable) {
        super(CODE_CHECK, throwable);
    }
    
}
