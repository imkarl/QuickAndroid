package cn.jeesoft.qa.config;

/**
 * 配置管理类
 * @version v0.1.0 king 2015-01-05
 */
public class DefaultConfig extends Config {
	private static final long serialVersionUID = 1L;

	@Override
    public void initialize() {
        
        set(QAConfig.Tips.ERROR_REPORT_TITLE, "应用程序错误");
        set(QAConfig.Tips.ERROR_REPORT_DESCRIPTION, "很抱歉，应用程序出现错误，即将退出。\n请提交错误报告，我们会尽快修复这个问题！");
        set(QAConfig.Tips.ERROR_REPORT_SUBMIT, "提交报告");
        set(QAConfig.Tips.ERROR_REPORT_CANCEL, "取消");
        set(QAConfig.Tips.ERROR_REPORT_EMAIL, "alafighting@163.com");
        set(QAConfig.Tips.ERROR_REPORT_SUBJECT, "Android客户端 - 错误报告");
        
        set(QAConfig.Http.TIMEOUT, 3 * 1000);
        set(QAConfig.Http.MAX_RETRIES, 3);
        set(QAConfig.Http.BACKOFF_MULTIPLIER, 1.0F);
        
    }
    
}
