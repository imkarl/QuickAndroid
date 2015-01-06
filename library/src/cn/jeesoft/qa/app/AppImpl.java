package cn.jeesoft.qa.app;

import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.utils.QAAppUtils;
import cn.jeesoft.qa.utils.QAAppUtils.QAAppInfo;
import android.app.Application;

/**
 * {@link cn.jeesoft.qa.app.App}的拓展增强实现
 * @version v0.1.0 king 2015-01-05
 */
abstract class AppImpl extends App {

	public AppImpl(QAPrivateCheck check, Application app) {
		super(check, app);
	}

    @Override
    protected void onCreate() {
    }
    @Override
    protected void onInitConfig() {
    }
    @Override
    protected void onHandleAppExit() {
    }
    @Override
    protected boolean onHandleException(Throwable ex) {
        return false;
    }
    
    
    
    
    
    

    public String getPackageName() {
        return getApplication().getPackageName();
    }
    /**
     * 获取App安装包信息
     * @return
     */
    public QAAppInfo getAppInfo() {
        return QAAppUtils.getCurrentAppInfo(getApplication());
    }
    
    
    
    
}
