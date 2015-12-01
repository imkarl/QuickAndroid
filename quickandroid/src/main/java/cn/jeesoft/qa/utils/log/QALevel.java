package cn.jeesoft.qa.utils.log;

/**
 * Log输出级别
 * @version v0.1.0 king 2014-11-07
 */
public enum QALevel {

    DEBUG(android.util.Log.DEBUG),
    INFO(android.util.Log.INFO),
    ERROR(android.util.Log.ERROR);
    
    private final int level;
    public int getLevel() {
        return this.level;
    }
    private QALevel(int level) {
        this.level = level;
    }
    
    public QALevel valueOf(int level) {
        QALevel theLevel = null;
        switch (level) {
        case android.util.Log.DEBUG:
            theLevel = DEBUG;
            break;
        case android.util.Log.INFO:
            theLevel = INFO;
            break;
        case android.util.Log.ERROR:
            theLevel = ERROR;
            break;
        default:
            break;
        }
        return theLevel;
    }

}
