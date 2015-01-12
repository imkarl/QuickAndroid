package cn.jeesoft.qa.config;

/**
 * 配置项常量
 * @version v0.1.2 king 2015-01-12 增加HTTP配置项常量
 * @version v0.1.1 king 2015-01-06 增加提示信息常量
 */
interface QAConfigKeys {

    /**
     * 提示信息常量
     */
    public static class Tips {

        public static final String ERROR_REPORT_TITLE = "Tips.ERROR_REPORT_TITLE";
        public static final String ERROR_REPORT_DESCRIPTION = "Tips.ERROR_REPORT_DESCRIPTION";
        public static final String ERROR_REPORT_SUBMIT = "Tips.ERROR_REPORT_SUBMIT";
        public static final String ERROR_REPORT_CANCEL = "Tips.ERROR_REPORT_CANCEL";
        public static final String ERROR_REPORT_EMAIL = "Tips.ERROR_REPORT_EMAIL";
        public static final String ERROR_REPORT_SUBJECT = "Tips.ERROR_REPORT_SUBJECT";
        
    }
    /**
     * HTTP配置项
     */
    public static class Http {
        
        /** 超时时间 */
        public static final String TIMEOUT = "Http.TIMEOUT";
        /** 最大重试次数 */
        public static final String MAX_RETRIES = "Http.MAX_RETRIES";
        /** 退避指数：“指数退避”用来从RESTful服务器请求数据 */
        public static final String BACKOFF_MULTIPLIER = "Http.BACKOFF_MULTIPLIER";
        
    }
    
}
