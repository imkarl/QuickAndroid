package cn.jeesoft.qa.libcore.image;

import android.graphics.Bitmap;
import android.view.View;
import cn.jeesoft.qa.error.QAImageException;

/**
 * 图片加载监听器
 * @version v0.1.0 king 2015-01-14 定义回调方法
 */
public interface QAImageCallback {
    
    /**
     * 开始加载
     */
    public void onStart(String url, View view);
    /**
     * 取消加载
     */
    public void onCancel(String url, View view);
    /**
     * 加载图片成功
     */
    public void onSuccess(String url, View view, Bitmap bitmap);
    /**
     * 加载图片失败
     */
    public void onFail(String url, View view, QAImageException exception);

}
