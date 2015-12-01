package cn.jeesoft.qa.libcore.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.manager.QAFileManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 全局图片加载配置管理
 * @version v0.1.0 king 2014-11-24 全局图片加载配置
 */
class QAImageLoaderConfig {
    
    private QAImageLoaderConfig() {
    }
    
    /**
     * 获取加载配置
     * @param context
     * @param imageOnLoadingRes 加载中显示的图片
     * @param imageOnFailRes 加载失败显示的图片
     * @return
     */
    public static ImageLoaderConfiguration getConfig(Context context,
            int imageOnLoadingRes, int imageOnFailRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageOnLoadingRes) // 加载中
                .showImageForEmptyUri(imageOnFailRes) // 图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imageOnFailRes) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(50) // 加载前延迟时长
                .cacheInMemory(true) // default = false 是否使用内存缓存
                .cacheOnDisk(true) // default = false 是否使用文件缓存
                .considerExifParams(false) // default = false
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new FadeInBitmapDisplayer(100)) // default = new SimpleBitmapDisplayer()
                //.handler(new Handler()) // default
                .build();

        File cacheDir = new File(QAFileManager.getUsableDir(context.getPackageName()));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //.memoryCacheExtraOptions(480, 800) // default = device screen dimensions 缓存尺寸
                .threadPoolSize(3) // default 线程池大小
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 线程优先级
                .denyCacheImageMultipleSizesInMemory() // 拒绝缓存图片的多个大小
                .memoryCache(new LruMemoryCache(8 * 1024 * 1024)) // 内存缓存
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 磁盘缓存
                .diskCacheSize(30 * 1024 * 1024) // 磁盘缓存大小
                //.diskCacheFileCount(300) //缓存的文件数量
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default 磁盘缓存文件名
                .imageDownloader(new BaseImageDownloader(context)) // default 图片下载器
                .imageDecoder(new BaseImageDecoder(QACore.isDebug())) // default 图片解码器
                .defaultDisplayImageOptions(options) // default=DisplayImageOptions.createSimple() 图片显示选项
                .build();
        
        return config;
    }
    
    /**
     * 获取加载配置
     * @param context
     * @param imageOnLoading 加载中显示的图片
     * @param imageOnFail 加载失败显示的图片
     * @return
     */
    public static ImageLoaderConfiguration getConfig(Context context,
            Drawable imageOnLoading, Drawable imageOnFail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageOnLoading) // 加载中
                .showImageForEmptyUri(imageOnFail) // 图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imageOnFail) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(50) // 加载前延迟时长
                .cacheInMemory(true) // default = false 是否使用内存缓存
                .cacheOnDisk(true) // default = false 是否使用文件缓存
                .considerExifParams(false) // default = false
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new FadeInBitmapDisplayer(100)) // default = new SimpleBitmapDisplayer()
                //.handler(new Handler()) // default
                .build();
        
        File cacheDir = new File(QAFileManager.getUsableDir(context.getPackageName()));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //.memoryCacheExtraOptions(480, 800) // default = device screen dimensions 缓存尺寸
                .threadPoolSize(3) // default 线程池大小
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 线程优先级
                .denyCacheImageMultipleSizesInMemory() // 拒绝缓存图片的多个大小
                .memoryCache(new LruMemoryCache(8 * 1024 * 1024)) // 内存缓存
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 磁盘缓存
                .diskCacheSize(30 * 1024 * 1024) // 磁盘缓存大小
                //.diskCacheFileCount(300) //缓存的文件数量
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default 磁盘缓存文件名
                .imageDownloader(new BaseImageDownloader(context)) // default 图片下载器
                .imageDecoder(new BaseImageDecoder(QACore.isDebug())) // default 图片解码器
                .defaultDisplayImageOptions(options) // default=DisplayImageOptions.createSimple() 图片显示选项
                .build();
        
        return config;
    }
    
}
