package cn.jeesoft.qa.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.config.QAConfig;
import cn.jeesoft.qa.manager.QAFileManager;
import cn.jeesoft.qa.ui.uikit.QAToast;
import cn.jeesoft.qa.utils.QAAppUtils.QAAppInfo;
import cn.jeesoft.qa.utils.log.QALog;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 * @version v0.1.1 king 2015-01-06 简化逻辑
 * @version v0.1.0 king 2013-09-02 异常捕获
 */
class DefaultExceptionHandler implements UncaughtExceptionHandler {

	private final static boolean Debug = QACore.isDebug();  // 是否保存错误日志

    /**
     * 注册监听APP未捕获异常
     */
    public static void registUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
    }
    
    
    
    
    
	
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	
	private DefaultExceptionHandler(){
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }
    
    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     * @param exception
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable exception) {
        if(Debug)
            this.saveErrorLog(exception);
        
        boolean flag = false;
        try {
            flag = QAApp.getApp().onHandleException(exception);
        } catch (Exception e) {
            QALog.e(e);
        }
        
        if (!flag) {
            onHandleException(exception);
        }
        return true;
    }

    /**
     * 保存异常日志
     * @param exception
     */
    @SuppressWarnings("deprecation")
    private void saveErrorLog(Throwable exception) {
        String errorlog = "errorlog.txt";
        String savePath = "";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            //判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();        
            if (storageState.equals(Environment.MEDIA_MOUNTED)){
                savePath = QAFileManager.getUsableDir(QACore.getApp().getPackageName(), "Log");
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                logFilePath = savePath + errorlog;
            }
            //没有挂载SD卡，无法写文件
            if (logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile,true);
            pw = new PrintWriter(fw);
            pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");   
            exception.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            QALog.e(e);
        } finally { 
            if (pw != null) pw.close();
            
            if(fw != null) {
                try {
                    fw.close();
                } catch (IOException e) { }
            }
        }
    }
    
    
    /**
     * 自己实现的异常处理
     * @param exception
     */
    private void onHandleException(Throwable exception) {
    	// 打印APP崩溃异常信息
        QALog.e(exception);
        
        // 获取APP崩溃异常报告
        final String crashReport = getCrashReport(exception);
        //显示异常信息&发送报告
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    sendAppCrashReport(QACore.getManager().currentActivity(), crashReport);
                    Looper.loop();
                } catch (Exception e) {
                	// 打印发送报告失败异常
                    QALog.e("发送异常报告失败：" + e.toString(), e);
                    try {
                        QACore.getManager().exitApp(QACore.getApp().getApplication());
                    } catch (Exception e2) {
                    	// 打印发送报告失败异常
                        QALog.e("应用程序退出失败：" + e2.toString(), e2);
                        
                        try {
                            QAToast.show(QACore.getApp().getApplication(), "应用程序异常崩溃");
                        } catch (Exception e3) { }
                    }
                }
            }
        }.start();
    }
    

    /**
     * 获取APP崩溃异常报告
     * @param ex
     * @return
     */
    private static String getCrashReport(Throwable ex) {
        QAAppInfo appInfo = QACore.getApp().getAppInfo();
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: "+appInfo.versionName+"("+appInfo.versionCode+")\n");
        exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
        exceptionStr.append("Exception: "+QALog.toLogString(ex)+"\n");
        return exceptionStr.toString();
    }
    /**
     * 发送App异常崩溃报告
     * @param activity
     * @param crashReport
     */
    private void sendAppCrashReport(final Activity activity, final String crashReport) {
        try {
            // 读取配置项
            final String title = QACore.getConfig().getString(QAConfig.Tips.ERROR_REPORT_TITLE);
            final String description = QACore.getConfig().getString(QAConfig.Tips.ERROR_REPORT_DESCRIPTION);
            final String email = QACore.getConfig().getString(QAConfig.Tips.ERROR_REPORT_EMAIL);
            final String submit = QACore.getConfig().getString(QAConfig.Tips.ERROR_REPORT_SUBMIT);
            final String cancel = QACore.getConfig().getString(QAConfig.Tips.ERROR_REPORT_CANCEL);
            
            // 弹出异常提示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle(title);
            builder.setMessage(description);
            builder.setPositiveButton(submit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 发送异常报告
                            Intent i = new Intent(Intent.ACTION_SEND);
                            // i.setType("text/plain"); //模拟器
                            i.setType("message/rfc822"); // 真机
                            i.putExtra(Intent.EXTRA_EMAIL,
                                    new String[] {email});
                            i.putExtra(Intent.EXTRA_SUBJECT, submit);
                            i.putExtra(Intent.EXTRA_TEXT, crashReport);
                            activity.startActivity(Intent.createChooser(i, "发送错误报告"));
                            // 退出
                            QACore.getManager().exitApp(activity);
                        }
                    });
            builder.setNegativeButton(cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 退出
                            QACore.getManager().exitApp(activity);
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // 退出
                    QACore.getManager().exitApp(activity);
                }
            });
            builder.setCancelable(true);
            builder.show();
        } catch (RuntimeException e) {
            QALog.e(e);
        } catch (Throwable e) {
            QALog.e(e);
        }
    }
    
}
