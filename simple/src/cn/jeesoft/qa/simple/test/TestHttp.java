package cn.jeesoft.qa.simple.test;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.libcore.http.QARequestParams;
import cn.jeesoft.qa.utils.log.QALog;

public class TestHttp extends BaseTestCase {
    
    /**
     * 测试HTTP请求
     */
    public void testHttp() throws Throwable {
        
        QALog.e(getApplication());
        
        QACore.initApp(getApplication());
        String url = "http://www.baidu.com/s";
        QARequestParams params = new QARequestParams();
        params.put("wd", "android");
        QAHttpCallback<String> listener = new QAHttpCallback<String>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            @Override
            public void onSuccessNet(String url, String data) {
                QALog.e(url, "onSuccessNet", data);
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, String data) {
                QALog.e(url, "onSuccessCache", data);
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url , params, listener);
        

        // 暂停，以便观察log输出
        sleep(3 * 1000);
        
    }
    
    
    
}
