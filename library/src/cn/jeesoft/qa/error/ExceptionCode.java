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
    public static final int CODE_NOSUCHMETHOD = 1005;
    /** 实例化异常 */
    public static final int CODE_INSTANTIATION = 1006;
    /** 非法访问 */
    public static final int CODE_ILLEGAL_ACCESS = 1007;
    /** 非法参数 */
    public static final int CODE_ILLEGAL_ARGUMENT = 1008;
    /** 执行异常 */
    public static final int CODE_EXECUTE = 1009;
    
    
    /** 验证失败 */
    public static final int CODE_CHECK = 1100;
    
    
}
