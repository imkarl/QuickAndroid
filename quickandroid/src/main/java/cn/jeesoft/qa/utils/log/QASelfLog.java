package cn.jeesoft.qa.utils.log;

/**
 * 自定义Log接口
 * @version v0.1.0 king 2014-11-12 定义接口方法
 */
public interface QASelfLog {

    /*
     * 自动生成TAG
     */
    public void i(Object... objs);
    public void d(Object... objs);
    public void e(Object... objs);
    

    /**
     * 按级别，输出格式化后的消息
     */
    public void format(QALevel level, String format, Object... args);
    
    /**
     * 按级别，输出格式化后的消息+异常信息
     */
    public void format(QALevel level, Throwable exception, String format, Object... args);
    
    public void log(QALevel level, Object... messages);
    
}
