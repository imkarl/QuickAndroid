package cn.jeesoft.qa.app;

import android.app.Application;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.config.DefaultConfig;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.error.QAAppException;
import cn.jeesoft.qa.error.QACheckException;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.manager.QAActivityManager;

/**
 * 全局APP管理器
 * @version v0.1.0 king 2015-01-05
 */
abstract class App {

	private static QAApp StaticApp;
    /**
     * 获取全局APP控制器
     */
    public final static QAApp getApp() {
    	if (StaticApp == null) {
	        synchronized (QAApp.class) {
	            if (StaticApp == null) {
	                throw new QAAppException(QAException.CODE_NULL, "未实例化QAApp");
	            }
	        }
    	}
        return StaticApp;
    }
    

	private static QAConfig StaticConfig = null;
	/**
	 * 获取全局配置管理类
	 */
	public final static QAConfig getConfig() {
		if (StaticConfig == null) {
			synchronized (QAConfig.class) {
				if (StaticConfig == null) {
					DefaultConfig config = new DefaultConfig();
					config.initialize();
					getApp().onInitConfig();
					StaticConfig = config;
				}
			}
		}
		return StaticConfig;
	}
	

	private static QAActivityManager StaticActivityManager = null;
	/**
	 * 获取全局配置管理类
	 */
	public final static QAActivityManager getManager() {
		if (StaticActivityManager == null) {
			synchronized (QAActivityManager.class) {
				if (StaticActivityManager == null) {
					QAActivityManager manager = new DefaultActivityManager();
					StaticActivityManager = manager;
				}
			}
		}
		return StaticActivityManager;
	}
	
	
	

    private Application mApplication;
    /**
     * 构造方法
     */
	public App(QAPrivateCheck check, Application app) {
		super();
		if (check == null || !check.check()) {
		    throw new QACheckException("不合法的QAPrivateCheck");
		}
		
		this.mApplication = app;
		// 注册APP异常崩溃处理器
		DefaultExceptionHandler.registUncaughtExceptionHandler();
    	this.onCreate();
    	
    	synchronized (QAApp.class) {
        	StaticApp = (QAApp) this;
        }
	}
    /**
     * 获取全局Application
     */
    public final Application getApplication() {
    	synchronized (QAApp.class) {
    		return mApplication;
    	}
    }
    
    
    
    
    
    
	
	/**
	 * <code>QAApp</code>控制器创建时执行
	 */
    protected abstract void onCreate();
	/**
	 * 初始化<code>Config</code>时执行
	 */
    protected abstract void onInitConfig();
	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * @param exception 异常
	 * @return true:处理了该异常信息;否则返回false
	 */
    protected abstract boolean onHandleException(Throwable exception);
    /**
     * 通知程序，退出应用
     */
    protected abstract void onHandleAppExit();
	
}
