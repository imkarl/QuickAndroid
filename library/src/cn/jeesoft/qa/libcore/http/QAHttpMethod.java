package cn.jeesoft.qa.libcore.http;

/**
 * HTTP请求方式（GET\POST）
 * @version v0.1.0 king 2015-01-10 HTTP请求方式
 */
public enum QAHttpMethod {

//    int DEPRECATED_GET_OR_POST = -1;
//    int GET = 0;
//    int POST = 1;
//    int PUT = 2;
//    int DELETE = 3;
//    int HEAD = 4;
//    int OPTIONS = 5;
//    int TRACE = 6;
//    int PATCH = 7;
    
    GET(0), POST(1);
    
    private int value;
    
    private QAHttpMethod(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
