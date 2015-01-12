package cn.jeesoft.qa.utils.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import android.text.TextUtils;

/**
 * 打印信息控制
 * <p>
 * Log.DEBUG控制是否打印
 * </p>
 * @version v0.1.1 king 2014-11-07 方法整理，简化调用方式；美化打印内容（空/换行/制表符）
 * @version v0.1.0 king 2013-09-18 实现日志打印，增加Level枚举类，整理重载方法
 */
class Log {

    /** debug开关. */
    private static boolean D = true;
	
	/** info开关. */
    private static boolean I = true;
	
	/** error开关. */
    private static boolean E = true;

	/**
	 * 设置日志的开关
	 * @param d
	 * @param i
	 * @param e
	 */
    public static void setVerbose(boolean d, boolean i, boolean e) {
		D  = d;
		I  = i;
		E  = e;
	}
	
	/**
	 * 打开所有日志，默认全打开
	 */
    public static void openAll() {
		D  = true;
		I  = true;
		E  = true;
	}
	
	/**
	 * 关闭所有日志
	 */
    public static void closeAll() {
		D  = false;
		I  = false;
		E  = false;
	}
	
	
	protected static void log(QALevel level, String tag, Object... messages) {
		boolean isLog = false;
		
		// 判断是否打印日志
		switch (level) {
		case INFO:
			if (Log.I) {
				isLog = true;
			}
			break;
			
		case DEBUG:
			if (Log.D) {
				isLog = true;
			}
			break;
			
		case ERROR:
			if (Log.E) {
				isLog = true;
			}
			break;

		default:
			break;
		}
		
		if (isLog) {
			// 打印日志
			String logTag = TextUtils.isEmpty(tag) ? "[null]" : tag;
			String logMessage = "";
			
			if (messages == null || messages.length == 0) {
				logMessage = "[NULL]";
			} else if (messages.length == 1) {
			    logMessage = toLogString(messages[0]);
			    logMessage = TextUtils.isEmpty(logMessage) ? "[ ]" : logMessage;
			    if (logMessage.equals("\n")) {
                    logMessage = "[\\n]";
                }
			} else {
				// 换行分割符 
				final String lineSplit = " \n ➜ \t ";
				for (Object message : messages) {
				    if (message instanceof Throwable) {
				        logMessage += toLogString(message)+lineSplit;
				    } else {
	                    logMessage += toLogString(message)+lineSplit;
                    }
				}
				logMessage = logMessage.substring(0, logMessage.length() - lineSplit.length());
			}
			
			android.util.Log.println(level.getLevel(), logTag, logMessage);
		}
	}
	
	
	/**
	 * 按级别，输出格式化后的消息
	 */
	protected static void format(QALevel level, String tag, String format, Object... args) {
        log(level, tag, buildMessage(format, args));
    }
	/**
	 * 按级别，输出格式化后的消息+异常信息
	 */
    protected static void format(QALevel level, String tag, Throwable exception, String format, Object... args) {
        log(level, tag, buildMessage(format, args), exception);
    }
	
	
	/*
	 * 根据Object/Class/String，获取TAG
	 */
	protected static void i(String tag, Object... messages) {
		log(QALevel.INFO, tag, messages);
	}
	protected static void d(String tag, Object... messages) {
		log(QALevel.DEBUG, tag, messages);
	}
	protected static void e(String tag, Object... messages) {
		log(QALevel.ERROR, tag, messages);
	}
	
	
	
	/**
	 * 将Object转换为String【便于输出】
	 * @param message 消息内容
	 * @return 为null时返回“<code>[null]</code>”
	 */
	public static String toLogString(Object message) {
		if (message == null) {
		    if (message instanceof Exception) {
		        return "[NULL (Exception)]";
		    } else if (message instanceof Error) {
	            return "[NULL (Error)]";
		    } else {
		        return "[NULL]";
		    }
		}
		
		if (message instanceof Throwable) {
			return getStackTraceString((Throwable)message);
		}
		
		if (message instanceof byte[]) {
			return new String((byte[]) message);
		}
		
		if (message instanceof char[]) {
			return new String((char[]) message);
		}
		
		return String.valueOf(message);
	}
    /**
     * Formats the caller's provided message and prepends useful info like calling thread ID and method name.
     */
	protected static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        if (trace.length >= 2) {
            String callingClass = trace[2].getClassName();
            callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
            callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

            caller = callingClass + "." + trace[2].getMethodName();
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }
	/**
	 * 获取异常信息
	 * @param throwable
	 * @return 
	 */
	protected static String getStackTraceString(Throwable throwable) {
	    if (throwable == null) {
	        return null;
	    }
	    
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        
        String strErrorMessage = sw.toString();
        return strErrorMessage;
    }
    
}
