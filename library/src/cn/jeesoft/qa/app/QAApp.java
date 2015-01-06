package cn.jeesoft.qa.app;

import android.app.Application;
import cn.jeesoft.qa.QACore.QAPrivateCheck;

/**
 * APP全局管理器
 * @version v0.1.0 king 2015-01-06
 */
public class QAApp extends AppImpl {
    
	public QAApp(QAPrivateCheck check, Application app) {
		super(check, app);
	}
    
}
