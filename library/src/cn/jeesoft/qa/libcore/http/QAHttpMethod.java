package cn.jeesoft.qa.libcore.http;

import com.android.volley.Request;

/**
 * HTTP请求方式（GET\POST）
 * @version v0.1.0 king 2015-01-10 HTTP请求方式
 */
public enum QAHttpMethod {

    GET(Request.Method.GET), POST(Request.Method.POST);
    
    private int value;
    
    private QAHttpMethod(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
