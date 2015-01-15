package cn.jeesoft.qa.simple;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.utils.log.QALog;

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
        } catch (QAException e) {
            QALog.e(e);
        }
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        QACore.getManager().finishActivity(this);
    }
    
    /**
     * 测试异常拦截
     */
    public void testException(View view) {
        throw new NullPointerException("这是测试的异常");
    }
    
    /**
     * 测试HTTP请求
     */
    public void testHttp(View view) {
        String url = "http://www.baidu.com/s";
        Map<String, String> params = new HashMap<String, String>();
        params.put("wd", "android");
        QAHttpCallback<QAJson> listener = new QAHttpCallback<QAJson>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            // data不会为NULL
            @Override
            public void onSuccessNet(String url, QAJson data) {
                QALog.e(url, "onSuccessNet", data.getClass());
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, QAJson data) {
                QALog.e(url, "onSuccessCache", data.getClass());
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url, null, listener);
    }
    
    /**
     * 测试HTTP请求（Bitmap）
     */
    public void testHttpImage(final View view) {
        String url = "https://raw.githubusercontent.com/alafighting/QuickAndroid/master/library/res/drawable-hdpi/qa_ic_logo.png";
      
        QAHttpCallback<Bitmap> listener = new QAHttpCallback<Bitmap>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            @SuppressWarnings("deprecation")
            @Override
            public void onSuccessNet(String url, Bitmap data) {
                QALog.e(url, "onSuccessNet", data);
                
                // 设置按钮的背景图片
                view.setBackgroundDrawable(new BitmapDrawable(getResources(), data));
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, Bitmap data) {
                QALog.e(url, "onSuccessCache", data);
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url, null, listener);
    }
    
    /**
     * 测试Image图片加载
     */
    public void testImage(View view) {
        String url = "https://raw.githubusercontent.com/alafighting/QuickAndroid/master/library/res/drawable-hdpi/qa_ic_logo.png";
        QACore.getImage().load(view, url);
    }

}
