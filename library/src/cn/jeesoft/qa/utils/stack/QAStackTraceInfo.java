package cn.jeesoft.qa.utils.stack;

/**
 * 堆栈跟踪信息
 * @version v0.1.0 king 2014-11-04 获取堆栈跟踪信息
 */
public class QAStackTraceInfo {
	private String className;
	private String classSimpleName;
	private String fileName;
	private String methodName;
	private int lineNumber;
	
	
	
	/**
	 * 获取栈堆信息
	 * @param i
	 * @return
	 */
	public static QAStackTraceInfo getStackTraceInfo(int i) {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		
		if (stacks != null && stacks.length >= i) {
			StackTraceElement stack = stacks[i];
			
			QAStackTraceInfo info = new QAStackTraceInfo();
			info.className = stack.getClassName();
			info.classSimpleName = (info.className==null)
					? null : info.className.substring(info.className.lastIndexOf(".")+1);
			info.fileName = stack.getFileName();
			info.methodName = stack.getMethodName();
			info.lineNumber = stack.getLineNumber();
			return info;
		}
		return null;
	}
	/**
	 * 从指定栈堆中，获取栈堆信息
	 * @param stack
	 * @return
	 */
	public static QAStackTraceInfo getStackTraceInfo(StackTraceElement stack) {
	    if (stack != null) {
	        QAStackTraceInfo info = new QAStackTraceInfo();
	        info.className = stack.getClassName();
	        info.classSimpleName = (info.className==null)
	                ? null : info.className.substring(info.className.lastIndexOf(".")+1);
	        info.fileName = stack.getFileName();
	        info.methodName = stack.getMethodName();
	        info.lineNumber = stack.getLineNumber();
	        return info;
	    }
	    return null;
	}
	
	/**
	 * 获取当前的栈堆信息
	 * @return
	 */
    public static QAStackTraceInfo getCurrentStackTraceInfo() {
        return getStackTraceInfo(new Throwable().getStackTrace()[1]);
    }
    /**
     * 获取调用者的栈堆信息
     * @return
     */
    public static QAStackTraceInfo getCallerStackTraceInfo() {
        return getStackTraceInfo(new Throwable().getStackTrace()[2]);
    }
	
	
	
	
	@Override
	public String toString() {
		return "QAStackTraceInfo [className=" + className + ", classSimpleName="
				+ classSimpleName + ", fileName=" + fileName + ", methodName="
				+ methodName + ", lineNumber=" + lineNumber + "]";
	}



	public String getClassName() {
		return className;
	}
	public String getFileName() {
		return fileName;
	}
	public String getMethodName() {
		return methodName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public String getClassSimpleName() {
		return classSimpleName;
	}
}
