package cn.jeesoft.qa.simple;

import java.util.HashMap;
import java.util.Map;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.simple.R;
import cn.jeesoft.qa.utils.log.QALog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QACore.getManager().addActivity(this);
        
        setContentView(R.layout.activity_main);
        
        try {
            QALog.e(this);
            QALog.e(QACore.getApp());
            QALog.e(QACore.getConfig());
            QALog.e(QACore.getManager());
            

            String url = "http://www.baidu.c1m/s";
            Map<String, String> params = new HashMap<String, String>();
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
            
        } catch (QAException e) {
            QALog.e(e);
        }
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        QACore.getManager().finishActivity(this);
    }
    
    public void myclick(View view) {
        
        throw new NullPointerException("这是测试的异常");
        
    }

}
