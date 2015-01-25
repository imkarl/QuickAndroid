package cn.jeesoft.qa.utils.log;

import android.text.TextUtils;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.utils.stack.QAStackTraceInfo;

/**
 * 日志打印工具类
 * @version v0.1.0 king 2014-11-07 自动生成TAG信息
 * @version v0.1.1 king 2015-01-22 可设置全局TAG
 */
public class QALog extends Log {
    public static final QALevel DEBUG = QALevel.DEBUG;
    public static final QALevel INFO = QALevel.INFO;
    public static final QALevel ERROR = QALevel.ERROR;
    
    
    protected QALog(QAPrivateCheck check) {
    	QAPrivateCheck.check(check);
    }
    
    
    /**
     * 设置自定义Log
     */
    public static QASelfLog selfLog;
    private static String TAG;
	
    /**
     * 设置TAG
     * @param tag
     */
    protected static void setTag(String tag) {
        QALog.TAG = tag;
    }
    /**
     * 获取TAG
     * @return
     */
    protected static String getTag() {
        if (!TextUtils.isEmpty(TAG)) {
            return TAG;
        } else {
            return getStackTraceInfo(3);
        }
    }
    
    
    /**
     * 获取栈堆信息
     * @param index
     * @return
     */
	protected static String getStackTraceInfo(int index) {
		QAStackTraceInfo info = QAStackTraceInfo.getStackTraceInfo(index);
		if (info == null) {
			return "[null]";
		}
		
		String tag = "[%s]%s.%s(L:%d)";
        String classSimpleName = info.getClassSimpleName();
        return String.format(tag, getThreadName(), classSimpleName, info.getMethodName(), info.getLineNumber());
	}
	/**
	 * 获取当前线程名
	 * @return
	 */
    protected static String getThreadName() {
        String threadName = Thread.currentThread().getName();
        if (!TextUtils.isEmpty(threadName) && threadName.equals("Instr: android.test.InstrumentationTestRunner")) {
            threadName = "Instr: AndroidTestCase";
        }
        return threadName;
    }
	
	
	
	/*
	 * 自动生成TAG
	 */
	public static void i(Object... messages) {
        if (selfLog != null) {
            selfLog.i(messages);
        } else {
            i(getTag(), messages);
        }
	}
	public static void d(Object... messages) {
        if (selfLog != null) {
            selfLog.d(messages);
        } else {
            d(getTag(), messages);
        }
	}
	public static void e(Object... messages) {
        if (selfLog != null) {
            selfLog.e(messages);
        } else {
            e(getTag(), messages);
        }
	}
	

    /**
     * 按级别，输出格式化后的消息
     */
	public static void format(QALevel level, String format, Object... args) {
	    if (selfLog != null) {
	        selfLog.format(level, format, args);
	    } else {
	        log(level, getTag(), buildMessage(format, args));
        }
    }
    /**
     * 按级别，输出格式化后的消息+异常信息
     */
	public static void format(QALevel level, Throwable exception, String format, Object... args) {
        if (selfLog != null) {
            selfLog.format(level, exception, format, args);
        } else {
            log(level, getTag(), buildMessage(format, args), exception);
        }
    }
	
	public static void log(QALevel level, Object... messages) {
        if (selfLog != null) {
            selfLog.log(level, messages);
        } else {
            log(level, getTag(), messages);
        }
    }
	
}
