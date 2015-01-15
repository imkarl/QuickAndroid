package cn.jeesoft.qa.libcore.image;

import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * 图片大小
 * @version v0.1.0 king 2015-01-14 图片大小
 */
public class QAImageSize extends ImageSize {

    public QAImageSize(int width, int height) {
        super(width, height);
    }
    public QAImageSize(int width, int height, int rotation) {
        super(width, height, rotation);
    }
    
    
}
