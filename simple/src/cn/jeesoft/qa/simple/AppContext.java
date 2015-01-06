package cn.jeesoft.qa.simple;

import android.app.Application;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.app.QAApp;

public class AppContext extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        QACore.initApp(this, App.class);
    }
    
    private static class App extends QAApp {
        public App(QAPrivateCheck check, Application app) {
            super(check, app);
        }
    }
    
}
