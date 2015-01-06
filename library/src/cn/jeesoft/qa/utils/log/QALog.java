package cn.jeesoft.qa.utils.log;

import cn.jeesoft.qa.utils.stack.QAStackTraceInfo;

/**
 * 日志打印工具类
 * @version v0.1.0 king 2014-11-07 自动生成TAG信息
 */
public class QALog extends Log {
    public static final QALevel DEBUG = QALevel.DEBUG;
    public static final QALevel INFO = QALevel.INFO;
    public static final QALevel ERROR = QALevel.ERROR;
    
    
    
    /**
     * 设置自定义Log
     */
    public static QASelfLog selfLog;
	
	/**
	 * 自动生成TAG
	 * @return
	 */
	protected static String getTag(int index) {
		QAStackTraceInfo info = QAStackTraceInfo.getStackTraceInfo(index);
		if (info == null) {
			return "[null]";
		}
		
		String tag = "[%s]%s.%s(L:%d)";
        String classSimpleName = info.getClassSimpleName();
        return String.format(tag, getThreadName(), classSimpleName, info.getMethodName(), info.getLineNumber());
        
//		return info.getClassSimpleName() + "."
//			+ info.getMethodName() + "()"
//			+ "["+info.getClassName()+":"+info.getLineNumber()+"]";
	}
    protected static String getThreadName() {
        return Thread.currentThread().getName();
    }
	
	
	
	/*
	 * 自动生成TAG
	 */
	public static void i(Object... messages) {
        if (selfLog != null) {
            selfLog.i(messages);
        } else {
            i(getTag(3), messages);
        }
	}
	public static void d(Object... messages) {
        if (selfLog != null) {
            selfLog.d(messages);
        } else {
            d(getTag(3), messages);
        }
	}
	public static void e(Object... messages) {
        if (selfLog != null) {
            selfLog.e(messages);
        } else {
            e(getTag(3), messages);
        }
	}
	

    /**
     * 按级别，输出格式化后的消息
     */
	public static void format(QALevel level, String format, Object... args) {
	    if (selfLog != null) {
	        selfLog.format(level, format, args);
	    } else {
	        log(level, getTag(3), buildMessage(format, args));
        }
    }
    /**
     * 按级别，输出格式化后的消息+异常信息
     */
	public static void format(QALevel level, Throwable exception, String format, Object... args) {
        if (selfLog != null) {
            selfLog.format(level, exception, format, args);
        } else {
            log(level, getTag(3), buildMessage(format, args), exception);
        }
    }
	
	public static void log(QALevel level, Object... messages) {
        if (selfLog != null) {
            selfLog.log(level, messages);
        } else {
            log(level, getTag(3), messages);
        }
    }
	
}
