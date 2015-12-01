package cn.jeesoft.qa.error;

/**
 * 适配器操作异常
 * @version v0.1.0 king 2015-02-02 适配器操作异常
 */
public class QAAdapterException extends QAException {
    private static final long serialVersionUID = 1L;

    public QAAdapterException(int code) {
        super(code);
    }
    public QAAdapterException(int code, String message) {
        super(code, message);
    }
    public QAAdapterException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAAdapterException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
