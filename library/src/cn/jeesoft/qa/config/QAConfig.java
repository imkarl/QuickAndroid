package cn.jeesoft.qa.config;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.model.pair.QAKeyValue;

/**
 * 配置项管理
 * 
 * <pre>
 * 请不要在此保存占内存的数据，仅适用于保存全局常量
 * </pre>
 * 
 * @version v0.1.1 king 2015-01-06 增加配置项常量
 * @version v0.1.0 king 2015-01-05 定义常用方法
 */
public interface QAConfig {
    
    /**
     * 提示信息常量
     */
    public static class Tips {

        public static final String ERROR_REPORT_TITLE = "ERROR_REPORT_TITLE";
        public static final String ERROR_REPORT_DESCRIPTION = "ERROR_REPORT_DESCRIPTION";
        public static final String ERROR_REPORT_SUBMIT = "ERROR_REPORT_SUBMIT";
        public static final String ERROR_REPORT_CANCEL = "ERROR_REPORT_CANCEL";
        public static final String ERROR_REPORT_EMAIL = "ERROR_REPORT_EMAIL";
        public static final String ERROR_REPORT_SUBJECT = "ERROR_REPORT_SUBJECT";
        
    }
    
    
	
    /**
     * 设置配置项
     * @param keyValues 配置项
     */
	public void set(List<QAKeyValue<String, ?>> keyValues);
    /**
     * 设置配置项
     * @param keyValue 配置项
     */
    public void set(QAKeyValue<String, ?> keyValue);
    /**
     * 设置配置项
     * @param key 配置项的名字
     * @param value 配置项的值
     */
    public void set(String key, Object value);
    
    
    
    /**
     * 获取配置项的值
     * @param key 配置项的名字
     * @return 配置项的值
     */
    public <T> T getObject(String key);
    
    public int getInt(String key);
    public float getFloat(String key);
    public double getDouble(String key);
    public short getShort(String key);
    public byte getByte(String key);
    public boolean getBoolean(String key);
    public char getChar(String key);
    public long getLong(String key);
    
    public String getString(String key, Object... args);
    public String getString(String key);
    public Date getDate(String key);
    public java.sql.Date getSqlDate(String key);
    public byte[] getByteArray(String key);
    public Bitmap getBitmap(String key);
    public Drawable getDrawable(String key);
    public QAJson getJson(String key);
	
}
