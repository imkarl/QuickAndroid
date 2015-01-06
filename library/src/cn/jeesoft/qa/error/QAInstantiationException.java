package cn.jeesoft.qa.error;

/**
 * 类实例化异常
 * @version v0.1.0 king 2015-01-06 类实例化异常
 */
public class QAInstantiationException extends QAException {
    private static final long serialVersionUID = 1L;
    
    public QAInstantiationException(int code) {
        super(code);
    }
    public QAInstantiationException(int code, String message) {
        super(code, message);
    }
    public QAInstantiationException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAInstantiationException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
