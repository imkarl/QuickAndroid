package cn.jeesoft.qa.error;

/**
 * HTTP异常
 * @version v0.1.0 king 2015-01-12 HTTP异常
 * @version v0.1.1 king 2015-03-10 去除Volley部分，重新实现
 */
public class QAHttpException extends QAException {
    private static final long serialVersionUID = 1L;

    private final int statusCode;

    public QAHttpException(int code, int statusCode, String message) {
        super(code, message);
        this.statusCode = statusCode;
    }
    public QAHttpException(int code, int statusCode, Throwable error) {
        super(code, error);
        this.statusCode = statusCode;
    }
    public QAHttpException(int code, int statusCode, String message, Throwable error) {
        super(code, message, error);
        this.statusCode = statusCode;
    }
    
    
    
    /**
     * 获取HTTP状态码
     * @return HTTP状态码（200:正常）
     */
    public int getHttpStatusCode() {
        return this.statusCode;
    }
    
}
