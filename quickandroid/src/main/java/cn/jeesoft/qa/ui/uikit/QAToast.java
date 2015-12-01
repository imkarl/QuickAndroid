package cn.jeesoft.qa.ui.uikit;

import cn.jeesoft.qa.QACore.QAPrivateCheck;
import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast辅助工具
 * @version v0.1.0 king 2014-12-18 支持更多可选参数
 * @version v0.1.0 king 2013-09-02 非队列方式的Toast实现
 */
public class QAToast {
    private static Toast mToast = null;

    protected QAToast(QAPrivateCheck check) {
    	QAPrivateCheck.check(check);
    }
    

    /*
     * 以下方法自动调用Toast的show()方法
     */
    public static void show(Context context, CharSequence text, boolean duration, boolean update) {
        makeText(context, text, duration, update).show();
    }
    public static void show(Context context, int resId, boolean duration, boolean update) {
        makeText(context, resId, duration, update).show();
    }
    
	public static void show(Context context, CharSequence text, boolean duration) {
		show(context, text, duration, true);
	}
	public static void show(Context context, int resId, boolean duration) {
	    show(context, resId, duration, true);
	}
	
	public static void show(Context context, CharSequence text) {
	    show(context, text, false);
	}
	public static void show(Context context, int resId) {
	    show(context, resId, false);
	}
	
	public static void show(Context context, CharSequence format, boolean duration, Object... args) {
	    show(context, (format==null ? null : String.format(format.toString(), args)), duration);
	}
	public static void show(Context context, int formatResourceId, boolean duration, Object... args) {
        show(context, context.getResources().getString(formatResourceId, args), duration);
	}
	
	public static void show(Context context, CharSequence format, Object... args) {
	    show(context, format, false, args);
	}
	public static void show(Context context, int formatResourceId, Object... args) {
        show(context, formatResourceId, false, args);
	}
	
	
	
	
	
    /**
     * 创建Toast实例
     * @param context {@link Context}
     * @param resId 文本内容的资源ID
     * @param duration 持续显示时长（true:长时间，false:短时间）
     * @param update 立即显示新的Toast内容，无需等待上个Toast消失（true:立即，false:队列等待）
     * @return {@link Toast}
     */
    private static Toast makeText(Context context, int resId, boolean duration, boolean update) {
    	return makeText(context, context.getText(resId), duration, update);
    }
	/**
	 * 创建Toast实例
	 * @param context {@link Context}
	 * @param text 文本内容
	 * @param duration 持续显示时长（true:长时间，false:短时间）
	 * @param update 立即显示新的Toast内容，无需等待上个Toast消失（true:立即，false:队列等待）
	 * @return {@link Toast}
	 */
	@SuppressLint("ShowToast")
    private static Toast makeText(Context context, CharSequence text, boolean duration, boolean update) {
	    if (context == null) {
	        throw new IllegalArgumentException("'context' can not be NULL.");
	    }
	    if (text == null) {
	        throw new IllegalArgumentException("'text' can not be NULL.");
	    }
	    
	    if (update) {
	        if (mToast != null) {
	            mToast.setText(text);
	            mToast.setDuration(duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
	        } else {
	            setToast(Toast.makeText(context, text, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT));
	        }
	        return mToast;
	    } else {
	        return Toast.makeText(context, text, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
	    }
	}

	
	
	
	
    /**
     * 设置当前Toast，如果已存在，则先销毁
     * @param toast
     */
    public static void setToast(Toast toast) {
        if (mToast != null)
            mToast.cancel();
        mToast = toast;
    }

    /**
     * 销毁Toast
     */
    public static void cancel() {
        if (mToast != null)
            mToast.cancel();
        mToast = null;
    }

}
