package cn.jeesoft.qa.error;

import com.android.volley.NetworkResponse;

/**
 * HTTP异常
 * @version v0.1.0 king 2015-01-12 HTTP异常
 */
public class QAHttpException extends QAException {
    private static final long serialVersionUID = 1L;

    private final NetworkResponse response;
    private long networkTimeMs;


    public QAHttpException(int code, NetworkResponse response) {
        super(code);
        this.response = response;
    }
    public QAHttpException(int code, String message, NetworkResponse response) {
        super(code, message);
        this.response = response;
    }
    public QAHttpException(int code, String message, Throwable throwable, NetworkResponse response) {
        super(code, message, throwable);
        this.response = response;
    }
    public QAHttpException(int code, Throwable throwable, NetworkResponse response) {
        super(code, throwable);
        this.response = response;
    }
    
    
    
    
    public int getHttpStatusCode() {
        if (this.response == null) {
            return -1;
        }
        return this.response.statusCode;
    }
    public long getNetworkTimeMs() {
       return networkTimeMs;
    }
    public NetworkResponse getNetworkResponse() {
        return this.response;
    }
    
}
