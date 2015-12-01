package cn.jeesoft.qa.error;

import android.text.TextUtils;
import cn.jeesoft.qa.utils.lang.QAClassUtils;

/**
 * 定义异常超类
 * @version v0.1.0 king 2015-01-05 定义异常超类
 */
abstract class Exception extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int code;
    
    public Exception(int code) {
        this.code = code;
    }
    public Exception(int code, String message) {
        super(message);
        this.code = code;
    }
    public Exception(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    public Exception(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }
    
    
    @Override
    public String toString() {
        String msg = this.getLocalizedMessage();
        msg = (TextUtils.isEmpty(msg) ? "" : msg);
        
        String name = QAClassUtils.getShortClassName(this.getClass());
        
        if (getCause() instanceof QAException) {
            return name + "=>" + msg + String.format("[异常码:%s]", this.getCode());
        }
        
        return name + ": " + msg + String.format("[异常码:%s]", this.getCode());
    }
    
    
    /**
     * 获取异常状态码
     * @return 大于0的整数
     */
    public int getCode() {
        return code;
    }
    
}
