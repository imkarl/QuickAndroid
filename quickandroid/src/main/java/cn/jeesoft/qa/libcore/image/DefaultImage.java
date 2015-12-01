package cn.jeesoft.qa.libcore.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAImageException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.utils.lang.QAStringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片管理
 * @version v0.1.0 king 2014-11-24 实现图片加载
 */
public class DefaultImage implements QAImage {
    
    private static Boolean isInit = false;
    private static ImageLoader getImageLoader(Context context,
            int imageOnLoadingRes, int imageOnFailRes) {
        if (!isInit) {
            synchronized (isInit) {
                if (!isInit) {
                    ImageLoader.getInstance().init(QAImageLoaderConfig.getConfig(context,
                            imageOnLoadingRes, imageOnFailRes));
                }
            }
        }
        return ImageLoader.getInstance();
    }
    private static ImageLoader getImageLoader(Context context,
            Drawable imageOnLoading, Drawable imageOnFail) {
        if (!isInit) {
            synchronized (isInit) {
                if (!isInit) {
                    ImageLoader.getInstance().init(QAImageLoaderConfig.getConfig(context,
                            imageOnLoading, imageOnFail));
                }
            }
        }
        return ImageLoader.getInstance();
    }
    
    
    
    private Context context;
    private int imageOnLoadingRes;
    private int imageOnFailRes;
    private Drawable imageOnLoading;
    private Drawable imageOnFail;
    
    public DefaultImage(QAPrivateCheck check, Context context,
            int imageOnLoadingRes, int imageOnFailRes) {
        QAPrivateCheck.check(check);
        
        this.context = context;
        this.imageOnLoadingRes = imageOnLoadingRes;
        this.imageOnFailRes = imageOnFailRes;
    }
    public DefaultImage(QAPrivateCheck check, Context context,
            Drawable imageOnLoading, Drawable imageOnFail) {
        QAPrivateCheck.check(check);
        
        this.context = context;
        this.imageOnLoading = imageOnLoading;
        this.imageOnFail = imageOnFail;
    }
    
    /**
     * 获取图片加载器
     * @return
     * @throws QANullException
     */
    private ImageLoader getImageLoader() {
        if (context == null) {
            throw new QANullException("'context' can not be NULL.");
        }
        
        ImageLoader imageLoader;
        if (imageOnLoading != null && imageOnFail != null) {
            imageLoader = getImageLoader(context, imageOnLoading, imageOnFail);
        } else if (imageOnLoadingRes > 0 && imageOnFailRes > 0) {
            imageLoader = getImageLoader(context, imageOnLoadingRes, imageOnFailRes);
        } else {
            throw new QANullException("'Default Display Image' can not be Empty.");
        }
        return imageLoader;
    }
    /**
     * 显示加载中的默认图片
     * @param view
     */
    @SuppressWarnings("deprecation")
    private void showLoading(View view) {
        if (imageOnLoading != null) {
            view.setBackgroundDrawable(imageOnLoading);
        } else if (imageOnLoadingRes > 0) {
            view.setBackgroundResource(imageOnLoadingRes);
        }
    }
    /**
     * 显示加载失败的默认图片
     * @param view
     */
    @SuppressWarnings("deprecation")
    private void showLoadfail(View view) {
        if (imageOnFail != null) {
            view.setBackgroundDrawable(imageOnFail);
        } else if (imageOnFailRes > 0) {
            view.setBackgroundResource(imageOnFailRes);
        }
    }
    /**
     * 创建图片加载监听器
     * @param view
     * @param listener
     * @return
     */
    @SuppressWarnings("rawtypes")
    private QAImageLoadingListener createQAImageLoadingListener(View view, final QAImageCallback listener) {
        return new QAImageLoadingListener<View>(view) {
            @Override
            public void onLoadingCancelled(View view, String imageUri) {
                if (listener != null) {
                    listener.onCancel(imageUri, view);
                }
            }
            @Override
            public void onLoadingComplete(View view, String imageUri,
                    Bitmap loadedImage) {
                if (listener != null) {
                    listener.onSuccess(imageUri, view, loadedImage);
                }
            }
            @Override
            public void onLoadingFailed(View view, String imageUri,
                    FailReason failReason) {
                if (listener != null) {
                    FailType type = failReason.getType();
                    
                    int code = QAException.CODE_UNKNOW;
                    if (type == FailType.IO_ERROR) {
                        code = QAException.CODE_IO_FILE;
                    } else if (type == FailType.DECODING_ERROR) {
                        code = QAException.CODE_DECODE;
                    } else if (type == FailType.NETWORK_DENIED) {
                        code = QAException.CODE_NETWORK;
                        
                    } else if (type == FailType.OUT_OF_MEMORY) {
                        code = QAException.CODE_OOM;
                        
                    } else if (type == FailType.UNKNOWN) {
                        code = QAException.CODE_UNKNOW;
                    }
                
                    QAImageException exception = new QAImageException(code, failReason.getCause());
                    listener.onFail(imageUri, view, exception);
                }
            }
            @Override
            public void onLoadingStarted(View view, String imageUri) {
                if (listener != null) {
                    listener.onStart(imageUri, view);
                }
            }
        };
    }
    
    
    
    
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     */
    @Override
    public void load(ImageView view, String url) {
        url = checkImageUri(url);
        getImageLoader().displayImage(url, view);
    }
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     */
    @Override
    public void load(final View view, String url) {
        url = checkImageUri(url);

        if (view instanceof ImageView) {
            load((ImageView)view, url);
            return;
        }
        
        getImageLoader().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View viewNull) {
                showLoading(view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View viewNull,
                                        FailReason failReason) {
                showLoadfail(view);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onLoadingComplete(String imageUri, View viewNull, Bitmap loadedImage) {
                if (loadedImage == null) {
                    onLoadingFailed(imageUri, viewNull, new FailReason(FailType.UNKNOWN, new NullPointerException("'bitmap' is NULL.")));
                    return;
                }
                view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                showLoadfail(view);
            }
        });
    }
    
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     * @param config 图片显示配置项
     */
    @Override
    public void load(ImageView view, String url, QAImageDisplayConfig config) {
        url = checkImageUri(url);
        getImageLoader().displayImage(url, view, config.build());
    }
    /**
     * 加载图片
     * @param view 将图片显示到该控件
     * @param url
     * @param listener 图片加载监听器
     */
    @Override
    public void load(ImageView view, String url, QAImageCallback listener) {
        url = checkImageUri(url);
        getImageLoader().displayImage(url, view, createQAImageLoadingListener(view, listener));
    }
    /**
     * 加载图片
     * @param url
     * @param listener 图片加载监听器
     */
    @Override
    public void load(View view, String url, QAImageCallback listener) {
        url = checkImageUri(url);
        getImageLoader().loadImage(url, createQAImageLoadingListener(view, listener));
    }
    /**
     * 加载图片
     * @param url
     * @param imageSize 图片大小
     * @param config 图片显示配置项
     * @param listener 图片加载监听器
     */
    @Override
    public void load(View view, String url, QAImageSize imageSize,
            QAImageDisplayConfig config, final QAImageCallback listener) {
        url = checkImageUri(url);
        getImageLoader().loadImage(url, imageSize, config.build(),
                createQAImageLoadingListener(view, listener));
    }

    private static String checkImageUri(String imageUri) {
        if (!QAStringUtils.isEmpty(imageUri)) {
            if (ImageDownloader.Scheme.ofUri(imageUri) == ImageDownloader.Scheme.UNKNOWN) {
                if (imageUri.toLowerCase().startsWith("/mnt/")
                        || imageUri.toLowerCase().startsWith("/mnt/")
                        || imageUri.toLowerCase().startsWith("/storage/")
                        || imageUri.toLowerCase().startsWith("/sdcard")
                        || imageUri.toLowerCase().startsWith("mnt/")
                        || imageUri.toLowerCase().startsWith("storage/")
                        || imageUri.toLowerCase().startsWith("sdcard")) {
                    imageUri = "file://"+imageUri;
                } else {
                    File file = new File(imageUri);
                    if (file.exists()) {
                        imageUri = "file://" + file.getAbsolutePath();
                    }
                }
            }
        }
        return imageUri;
    }
    
}
