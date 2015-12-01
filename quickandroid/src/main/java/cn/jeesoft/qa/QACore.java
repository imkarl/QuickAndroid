package cn.jeesoft.qa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import cn.jeesoft.qa.app.QAApp;
import cn.jeesoft.qa.config.DefaultConfig;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.error.QACheckException;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAInstantiationException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.libcore.db.DefaultDb;
import cn.jeesoft.qa.libcore.db.QADb;
import cn.jeesoft.qa.libcore.db.QADb.QADbListener;
import cn.jeesoft.qa.libcore.handle.QAHandle;
import cn.jeesoft.qa.libcore.http.DefaultHttp;
import cn.jeesoft.qa.libcore.http.QAHttp;
import cn.jeesoft.qa.libcore.image.DefaultImage;
import cn.jeesoft.qa.libcore.image.QAImage;
import cn.jeesoft.qa.manager.QAActivityManager;
import cn.jeesoft.qa.manager.QAFileManager;

/**
 * 核心功能管理类
 * @version v0.1.0 king 2015-01-05 核心功能管理
 */
public class QACore implements QACoreUtils {
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
     * @param app {@link Application}
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
     * @param app {@link Application}
     * @param clazz 自定义的{@link QAApp}类型
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
	public static QAHandle getHandler() {
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
                    QAHttp http = new DefaultHttp(new QAPrivateCheck() {
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
    
    private static QAImage StaticImage = null;
    /**
     * 获取全局网络请求管理类
     */
    public final static QAImage getImage() throws QANullException {
        if (StaticImage == null) {
            synchronized (QAImage.class) {
                if (StaticImage == null) {
                    QAImage image = null;
                    
                    try {
                        Drawable imageLoading = getConfig().getDrawable(QAConfig.Http.IMAGE_LOADING);
                        Drawable imageLoadfail = getConfig().getDrawable(QAConfig.Http.IMAGE_LOADFAIL);

                        if (imageLoading == null || imageLoadfail == null) {
                            image = new DefaultImage(new QAPrivateCheck() {
                                    @Override
                                    public boolean check() {
                                        return true;
                                    }
                                },
                                getApp().getApplication(), imageLoading, imageLoadfail);
                        }
                    } catch (Exception e) { }
                    
                    if (image == null) {
                        int imageLoadingRes = getConfig().getInt(QAConfig.Http.IMAGE_LOADING);
                        int imageLoadfailRes = getConfig().getInt(QAConfig.Http.IMAGE_LOADFAIL);

                        image = new DefaultImage(new QAPrivateCheck() {
                            @Override
                            public boolean check() {
                                return true;
                            }
                        },
                        getApp().getApplication(), imageLoadingRes, imageLoadfailRes);
                    }
                    
                    StaticImage = image;
                }
            }
        }
        
        if (StaticImage == null) {
            throw new QANullException("QAImage create Failed.");
        }
        return StaticImage;
    }
	

    public static final int DEF_DB_VERSION = 1;
    public static final String DEF_DB_NAME = "QA_default.db";
    
    private static final Map<String, QADb> mDbMap = new HashMap<String, QADb>();
    public static QADb getDb(Context context, QADbListener listener) {
        return getDb(context, DEF_DB_VERSION, listener);
    }
    public static QADb getDb(Context context, int version, QADbListener listener) {
        return getDb(context, null, null, version, listener);
    }
    public static QADb getDb(Context context, String dbDir, String dbName, int version, QADbListener listener) {
        if (TextUtils.isEmpty(dbDir)) {
            dbDir = QAFileManager.getUsableDir(QACore.getApp().getPackageName());
        }
        if (TextUtils.isEmpty(dbName)) {
            dbName = DEF_DB_NAME;
        }
        if (version <= 0) {
            version = DEF_DB_VERSION;
        }
        
        final String key = dbDir+dbName;
        // 无缓存则实例化
        if (!mDbMap.containsKey(key)) {
            synchronized (QADb.class) {
                if (!mDbMap.containsKey(key)) {
                    // 创建新实例
                    QADb db = new DefaultDb(context, dbDir, dbName, null, version, listener);
                    mDbMap.put(key, db);
                    return db;
                }
            }
        }

        // 从缓存获取
        QADb db = mDbMap.get(key);
        synchronized (QADb.class) {
            // 判断是否升级
            DefaultDb.checkOnUpgrade(db.getDatabase(), dbName, version, listener);
        }
        return db;
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
