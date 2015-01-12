package cn.jeesoft.qa.libcore.http.request;

import cn.jeesoft.qa.error.QAException;

import com.android.volley.VolleyError;

/**
 * Volley异常
 * @version v0.1.0 king 2015-01-12 Volley异常
 */
public class QAVolleyError extends VolleyError {
    private static final long serialVersionUID = 1L;
    
    private final int code;
    
    QAVolleyError(QAException cause) {
        super(cause);
        this.code = cause.getCode();
    }
    
    public int getCode() {
        return this.code;
    }
    
}
