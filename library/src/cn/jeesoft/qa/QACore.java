package cn.jeesoft.qa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.app.Application;
import android.os.Handler;
import cn.jeesoft.qa.app.QAApp;
import cn.jeesoft.qa.config.DefaultConfig;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.error.QACheckException;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAInstantiationException;
import cn.jeesoft.qa.libcore.http.QAHttp;
import cn.jeesoft.qa.manager.QAActivityManager;
import cn.jeesoft.qa.manager.QAFileManager;

/**
 * 核心功能管理类
 * @version v0.1.0 king 2015-01-05 核心功能管理
 */
public class QACore {
    /**
     * 合法性验证类
     */
    public static class QAPrivateCheck {
        private QAPrivateCheck() {
        }
        
        public boolean check() {
            return false;
        }
        
        public static void check(QAPrivateCheck check) {
            if (check == null || !check.check()) {
                throw new QACheckException("不合法的QAPrivateCheck");
            }
        }
    }
    
    
    /**
     * 是否调试模式
     * @return true:调试模式
     */
    public static boolean isDebug() {
    	return DefaultConfig.DEBUG;
    }
    
    

    /**
     * 初始化方法
     * <pre>
     * initApp()仅需调用一次
     * </pre>
     * @param app {@link android.app.Application}
     * @see #initApp(Application)
     */
    public static void initApp(Application app) {
        new QAApp(new QAPrivateCheck(){
        	public boolean check() {
        		return true;
        	}
        }, app);
    }
    /**
     * 初始化方法
     * <pre>
     * initApp()仅需调用一次
     * </pre>
     * @param app {@link android.app.Application}
     * @param clazz 自定义的{@link cn.jeesoft.qa.app.QAApp}类型
     * @throws QAInstantiationException QAApp实例化异常
     * @see #initApp(Application)
     */
    public static void initApp(Application app, Class<? extends QAApp> clazz) throws QAInstantiationException {
        // 检查QAApp是否已初始化
        try {
            if (QAApp.getApp() != null) {
                return;
            }
        } catch (QAException e) { }
        
        // 实例化QAApp
        try {
            Constructor<? extends QAApp> constructor = clazz.getConstructor(QAPrivateCheck.class, Application.class);
            QAApp newInstance = constructor.newInstance(new QAPrivateCheck(){
            	public boolean check() {
            		return true;
            	}
            }, app);
            if (newInstance == null) {
                throw new NullPointerException("QAApp实例化失败");
            }
        } catch (NoSuchMethodException e) {
            throw new QAInstantiationException(QAException.CODE_NO_METHOD, "QAApp找不到对应的构造方法", e);
        } catch (InstantiationException e) {
            throw new QAInstantiationException(QAException.CODE_INSTANTIATION, "QAApp构造方法无法执行", e);
        } catch (IllegalAccessException e) {
            throw new QAInstantiationException(QAException.CODE_ILLEGAL_ACCESS, "QAApp构造方法非法访问", e);
        } catch (IllegalArgumentException e) {
            throw new QAInstantiationException(QAException.CODE_ILLEGAL_ARGUMENT, "QAApp构造方法参数非法", e);
        } catch (InvocationTargetException e) {
            throw new QAInstantiationException(QAException.CODE_EXECUTE, "QAApp构造方法执行异常", e.getTargetException());
        } catch (Exception e) {
            throw new QAInstantiationException(QAException.CODE_UNKNOW, "QAApp实例化失败", e);
        }
    }
    
    
	public static QAConfig getConfig() {
		return QAApp.getConfig();
	}
	public static QAApp getApp() {
		return QAApp.getApp();
	}
	public static QAActivityManager getManager() {
		return QAApp.getManager();
	}
	public static Handler getHandler() {
	    return QAApp.getHandler();
	}
    
    private static QAHttp StaticHttp = null;
    /**
     * 获取全局网络请求管理类
     */
    public final static QAHttp getHttp() {
        if (StaticHttp == null) {
            synchronized (QAHttp.class) {
                if (StaticHttp == null) {
                    QAHttp http = new QAHttp(new QAPrivateCheck() {
                        @Override
                        public boolean check() {
                            return true;
                        }
                    });
                    StaticHttp = http;
                }
            }
        }
        return StaticHttp;
    }
	
	/**
	 * 退出应用程序
	 */
	public static void exitApp() {
	    getManager().exitApp(getApp().getApplication());
	}
	
	/**
	 * 获取合适的缓存目录路径，指定子目录
	 * @param dirName 子目录名称
	 * @return 完整路径
	 */
	public static String getUsableDir(String dirName) {
	    return QAFileManager.getUsableDir(getApp().getApplication().getPackageName(), dirName);
	}
	public static String getUsableDir() {
	    return getUsableDir(null);
	}
	
}
