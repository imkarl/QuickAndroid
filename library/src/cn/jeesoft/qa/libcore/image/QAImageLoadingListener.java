package cn.jeesoft.qa.libcore.image;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片加载监听器
 * @version v0.1.0 king 2014-11-24 解决回调方法的View为空的bug
 * @param <T> 控件类型
 */
class QAImageLoadingListener<T extends View> implements ImageLoadingListener {
    
    private T view;
    
    public QAImageLoadingListener(T view) {
        super();
        this.view = view;
    }

    @Override
    public final void onLoadingStarted(String imageUri, View view) {
        onLoadingStarted(this.view, imageUri);
    }
    @Override
    public final void onLoadingFailed(String imageUri, View view,
            FailReason failReason) {
        onLoadingFailed(this.view, imageUri, failReason);
    }
    @Override
    public final void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        onLoadingComplete(this.view, imageUri, loadedImage);
    }
    @Override
    public final void onLoadingCancelled(String imageUri, View view) {
        onLoadingCancelled(this.view, imageUri);
    }
    
    
    public void onLoadingStarted(T view, String imageUri) {
    }
    public void onLoadingFailed(T view, String imageUri, FailReason failReason) {
    }
    public void onLoadingComplete(T view, String imageUri, Bitmap loadedImage) {
    }
    public void onLoadingCancelled(T view, String imageUri) {
    }

}
