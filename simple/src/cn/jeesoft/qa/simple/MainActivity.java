package cn.jeesoft.qa.simple;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
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
        
        throw new NullPointerException("fsd");
        
    }

}
