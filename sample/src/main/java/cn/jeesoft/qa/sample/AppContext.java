package cn.jeesoft.qa.sample;

import android.app.Application;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.app.QAApp;

/**
 * @version 0.1 king 2015-11
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QACore.initApp(this, App.class);
    }

    private static class App extends QAApp {
        public App(QACore.QAPrivateCheck check, Application app) {
            super(check, app);
        }
    }

}
