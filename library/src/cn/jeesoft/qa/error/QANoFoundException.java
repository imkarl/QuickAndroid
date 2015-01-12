package cn.jeesoft.qa.error;

/**
 * 找不到类型的异常
 * @version v0.1.0 king 2015-01-12 找不到类型的异常
 */
public class QANoFoundException extends QAException {
    private static final long serialVersionUID = 1L;

    public QANoFoundException(int code) {
        super(code);
    }
    public QANoFoundException(int code, String message) {
        super(code, message);
    }
    public QANoFoundException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QANoFoundException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
