package cn.jeesoft.qa.libcore.handle;

import android.os.Handler;
import android.os.Looper;

import com.badoo.mobile.util.WeakHandler;

/**
 * 可避免内存泄漏的Handle
 * @version v0.1.0 king 2015-02-27 避免Handle内存泄漏
 */
public class QAHandle extends WeakHandler {

    public QAHandle() {
        super();
    }
    public QAHandle(Handler.Callback callback) {
        super(callback);
    }
    public QAHandle(Looper looper) {
        super(looper);
    }
    public QAHandle(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
    
}
