package cn.jeesoft.qa.error;

/**
 * 异常状态码
 * @version v0.1.0 king 2015-01-05 异常状态码
 */
interface ExceptionCode {
    
    /** 未知 */
    public static final int CODE_UNKNOW = 1001;
    /** 空指针 */
    public static final int CODE_NULL = 1002;
    /** 数据格式 */
    public static final int CODE_FORMAT = 1003;
    /** 类型转换 */
    public static final int CODE_CLASSCAST = 1004;
    /** 没有该方法 */
    public static final int CODE_NO_METHOD = 1005;
    /** 实例化异常 */
    public static final int CODE_INSTANTIATION = 1006;
    /** 非法访问 */
    public static final int CODE_ILLEGAL_ACCESS = 1007;
    /** 非法参数 */
    public static final int CODE_ILLEGAL_ARGUMENT = 1008;
    /** 执行异常 */
    public static final int CODE_EXECUTE = 1009;
    /** 不支持的编码 */
    public static final int CODE_NO_CHAR_ENCODING = 1010;
    /** 找不到的类型 */
    public static final int CODE_NO_TYPE = 1011;
    /** 不支持 */
    public static final int CODE_NO_SUPPORT = 1012;
    /** 没有连接 */
    public static final int CODE_NO_CONNECTION = 1013;
    /** 网络连接异常 */
    public static final int CODE_NETWORK = 1014;
    /** 超时异常 */
    public static final int CODE_TIMEOUT = 1015;
    /** 解析失败 */
    public static final int CODE_PARSER = 1016;
    /** 认证失败 */
    public static final int CODE_AUTH_FAILURE = 1017;
    /** 服务器异常 */
    public static final int CODE_SERVER = 1018;
    /** 未知HOST */
    public static final int CODE_UNKNOW_HOST = 1019;
    /** IO异常 */
    public static final int CODE_IO_FILE = 1010;
    /** 解码异常 */
    public static final int CODE_DECODE = 1011;
    /** 解码异常 */
    public static final int CODE_OOM = 1012;
    /** 只读 */
    public static final int CODE_READ_ONLY = 1013;
    /** 不允许 */
    public static final int CODE_NO_ALLOW = 1014;
    /** 非法状态 */
    public static final int CODE_ILLEGAL_STATE = 1015;
    

    
    
    /** 验证失败 */
    public static final int CODE_CHECK = 1100;
    
    
}
