package cn.jeesoft.qa.libcore.image;

import android.view.View;
import android.widget.ImageView;

/**
 * 图片管理
 * @version v0.1.0 king 2014-01-24 定义图片加载接口
 */
public interface QAImage {
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     */
    public void load(ImageView view, String url);
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     */
    public void load(View view, String url);
    
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     * @param options 图片显示配置项
     */
    public void load(ImageView view, String url, QAImageDisplayConfig config);
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     * @param listener 图片加载监听器
     */
    public void load(ImageView view, String url, QAImageCallback listener);
    
    /**
     * 加载图片
     * @param url
     * @param listener 图片加载监听器
     */
    public void load(View view, String url, QAImageCallback listener);
    
    /**
     * 加载图片
     * @param url
     * @param imageSize 图片大小
     * @param options 图片显示配置项
     * @param listener 图片加载监听器
     */
    public void load(View view, String url, QAImageSize imageSize,
            QAImageDisplayConfig config, QAImageCallback listener);
    
}
