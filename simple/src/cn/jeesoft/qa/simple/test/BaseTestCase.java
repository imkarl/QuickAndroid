package cn.jeesoft.qa.simple.test;

import cn.jeesoft.qa.utils.log.QALog;
import android.app.Application;
import android.test.AndroidTestCase;

public class BaseTestCase extends AndroidTestCase {
    
    /**
     * 暂停执行
     * @param time
     */
    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            QALog.e(e);
        }
    }

    public Application getApplication() {
        return (Application) getContext().getApplicationContext();
    }
    
}
