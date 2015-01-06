package cn.jeesoft.qa.error;

/**
 * 全局异常
 * @version v0.1.0 king 2015-01-05 定义全局异常
 */
public class QAException extends Exception implements ExceptionCode {
    private static final long serialVersionUID = 1L;
    
    public QAException(int code) {
        super(code);
    }
    public QAException(int code, String message) {
        super(code, message);
    }
    public QAException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
    public QAException(int code, Throwable throwable) {
        super(code, throwable);
    }
    
}
