package cn.jeesoft.qa.error;

/**
 * Image加载异常
 * 
 * <pre>
 * 包含五种Code：
 * CODE_IO_FILE、CODE_DECODE、CODE_NETWORK、CODE_OOM、CODE_UNKNOW
 * </pre>
 * 
 * @version v0.1.0 king 2015-01-05 JSON异常
 */
public class QAImageException extends QAException {
    private static final long serialVersionUID = 1L;

    public QAImageException(int code) {
        super(code);
    }
    public QAImageException(int code, String message) {
        super(code, message);
    }
    public QAImageException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAImageException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
