package cn.jeesoft.qa.manager;

import android.app.Activity;
import android.content.Context;

/**
 * 应用程序Activity管理器：用于Activity管理和应用程序退出
 * @version v0.1.0 king 2014-12-16 接口定义
 */
public interface QAActivityManager {
    
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity);
    
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity();
    
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity();
    
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity);
    
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls);
    
    /**
     * 退出应用程序
     */
    public void exitApp(Context context);

}
