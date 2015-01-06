package cn.jeesoft.qa.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.manager.QAActivityManager;
import cn.jeesoft.qa.utils.log.QALog;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @version v0.1.0 king 2013-09-02
 */
class DefaultActivityManager extends Stack<Activity> implements QAActivityManager {
	private static final long serialVersionUID = 1L;
	
	public DefaultActivityManager() {
		super();
	}
	
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		super.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
	    if (isEmpty()) {
	        throw new QANullException("Currently no Activity Instance.");
	    }
    	return super.lastElement();
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
	    try {
	        Activity activity = currentActivity();
	        finishActivity(activity);
        } catch (Exception e) {
            QALog.e(e);
        }
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		super.remove(activity);
		try {
		    activity.finish();
        } catch (Exception e) { }
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : this) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = super.size(); i < size; i++){
			Activity activity = super.get(i);
            if (null != activity){
            	activity.finish();
            }
	    }
		super.clear();
	}
	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void exitApp(Context context) {
		try {
			// 通知程序，退出应用
		    QAApp.getApp().onHandleAppExit();
		} catch (Exception e) {
		    QALog.e(e);
		}
		
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			activityMgr.restartPackage(context.getPackageName());
		} catch (Exception e) {
            QALog.e(e);
        }
		
		try {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} catch (Exception e) { }
	}
	
}