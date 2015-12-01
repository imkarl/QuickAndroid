package cn.jeesoft.qa.libcore.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * 图片显示配置
 * @version v0.1.0 king 2015-1-14 图片显示配置
 */
public class QAImageDisplayConfig extends DisplayImageOptions.Builder {

    public QAImageDisplayConfig() {
        super();
    }


    /**
     * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} during image loading
     *
     * @param imageRes Image resource
     */
    public QAImageDisplayConfig showImageOnLoading(int imageRes) {
        super.showImageOnLoading(imageRes);
        return this;
    }

    /**
     * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} during image loading.
     * This option will be ignored if {@link QAImageDisplayConfig#showImageOnLoading(int)} is set.
     */
    public QAImageDisplayConfig showImageOnLoading(Drawable drawable) {
        super.showImageOnLoading(drawable);
        return this;
    }

    /**
     * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} if empty URI (null or empty
     * string) will be passed to <b>ImageLoader.displayImage(...)</b> method.
     *
     * @param imageRes Image resource
     */
    public QAImageDisplayConfig showImageForEmptyUri(int imageRes) {
        super.showImageForEmptyUri(imageRes);
        return this;
    }

    /**
     * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} if empty URI (null or empty
     * string) will be passed to <b>ImageLoader.displayImage(...)</b> method.
     * This option will be ignored if {@link QAImageDisplayConfig#showImageForEmptyUri(int)} is set.
     */
    public QAImageDisplayConfig showImageForEmptyUri(Drawable drawable) {
        super.showImageForEmptyUri(drawable);
        return this;
    }

    /**
     * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} if some error occurs during
     * requested image loading/decoding.
     *
     * @param imageRes Image resource
     */
    public QAImageDisplayConfig showImageOnFail(int imageRes) {
        super.showImageOnFail(imageRes);
        return this;
    }

    /**
     * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} if some error occurs during
     * requested image loading/decoding.
     * This option will be ignored if {@link QAImageDisplayConfig#showImageOnFail(int)} is set.
     */
    public QAImageDisplayConfig showImageOnFail(Drawable drawable) {
        super.showImageOnFail(drawable);
        return this;
    }

    /**
     * Sets whether {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
     * image aware view} will be reset (set <b>null</b>) before image loading start
     */
    public QAImageDisplayConfig resetViewBeforeLoading(boolean resetViewBeforeLoading) {
        super.resetViewBeforeLoading(resetViewBeforeLoading);
        return this;
    }

    /** Sets whether loaded image will be cached in memory */
    public QAImageDisplayConfig cacheInMemory(boolean cacheInMemory) {
        super.cacheInMemory(cacheInMemory);
        return this;
    }

    /** Sets whether loaded image will be cached on disk */
    public QAImageDisplayConfig cacheOnDisk(boolean cacheOnDisk) {
        super.cacheOnDisk(cacheOnDisk);
        return this;
    }

    /**
     * Sets {@linkplain ImageScaleType scale type} for decoding image. This parameter is used while define scale
     * size for decoding image to Bitmap. Default value - {@link ImageScaleType#IN_SAMPLE_POWER_OF_2}
     */
    public QAImageDisplayConfig imageScaleType(ImageScaleType imageScaleType) {
        super.imageScaleType(imageScaleType);
        return this;
    }

    /** Sets {@link Bitmap.Config bitmap config} for image decoding. Default value - {@link Bitmap.Config#ARGB_8888} */
    public QAImageDisplayConfig bitmapConfig(Bitmap.Config bitmapConfig) {
        super.bitmapConfig(bitmapConfig);
        return this;
    }

    /**
     * Sets options for image decoding.<br />
     * <b>NOTE:</b> {@link Options#inSampleSize} of incoming options will <b>NOT</b> be considered. Library
     * calculate the most appropriate sample size itself according yo {@link #imageScaleType(ImageScaleType)}
     * options.<br />
     * <b>NOTE:</b> This option overlaps {@link #bitmapConfig(Bitmap.Config) bitmapConfig()}
     * option.
     */
    public QAImageDisplayConfig decodingOptions(Options decodingOptions) {
        super.decodingOptions(decodingOptions);
        return this;
    }

    /** Sets delay time before starting loading task. Default - no delay. */
    public QAImageDisplayConfig delayBeforeLoading(int delayInMillis) {
        super.delayBeforeLoading(delayInMillis);
        return this;
    }

    /** Sets auxiliary object which will be passed to {@link ImageDownloader#getStream(String, Object)} */
    public QAImageDisplayConfig extraForDownloader(Object extra) {
        super.extraForDownloader(extra);
        return this;
    }

    /** Sets whether ImageLoader will consider EXIF parameters of JPEG image (rotate, flip) */
    public QAImageDisplayConfig considerExifParams(boolean considerExifParams) {
        super.considerExifParams(considerExifParams);
        return this;
    }

    /**
     * Sets bitmap processor which will be process bitmaps before they will be cached in memory. So memory cache
     * will contain bitmap processed by incoming preProcessor.<br />
     * Image will be pre-processed even if caching in memory is disabled.
     */
    public QAImageDisplayConfig preProcessor(BitmapProcessor preProcessor) {
        super.postProcessor(preProcessor);
        return this;
    }

    /**
     * Sets bitmap processor which will be process bitmaps before they will be displayed in
     * {@link com.nostra13.universalimageloader.core.imageaware.ImageAware image aware view} but
     * after they'll have been saved in memory cache.
     */
    public QAImageDisplayConfig postProcessor(BitmapProcessor postProcessor) {
        super.postProcessor(postProcessor);
        return this;
    }

    /**
     * Sets custom {@link BitmapDisplayer displayer} for image loading task. Default value -
     * {@link DefaultConfigurationFactory#createBitmapDisplayer()}
     */
    public QAImageDisplayConfig displayer(BitmapDisplayer displayer) {
        super.displayer(displayer);
        return this;
    }

    /**
     * Sets custom {@linkplain Handler handler} for displaying images and firing {@linkplain ImageLoadingListener
     * listener} events.
     */
    public QAImageDisplayConfig handler(Handler handler) {
        super.handler(handler);
        return this;
    }

    /** Sets all options equal to incoming options */
    public QAImageDisplayConfig cloneFrom(QAImageDisplayConfig options) {
        super.cloneFrom(options.build());
        return this;
    }

    /** Builds configured {@link DisplayImageOptions} object */
    public DisplayImageOptions build() {
        return super.build();
    }

    
}
