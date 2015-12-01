package cn.jeesoft.qa.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import cn.jeesoft.qa.BuildConfig;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.model.pair.QAKeyValue;
import cn.jeesoft.qa.utils.lang.QAStringUtils;

/**
 * 配置管理类-抽象类
 * @version v0.1.0 king 2015-01-05 实现配置项管理接口
 */
@SuppressWarnings("serial")
abstract class Config extends HashMap<String, Object> implements QAConfig {
	
	/** 是否DeBug模式（区分是否打印日志） */
    public static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 是否调试模式
     * @return true:调试模式
     */
    public boolean isDebug() {
        return DEBUG;
    }
    
    /**
     * 设置配置项
     * @param keyValues 配置项
     */
    public void set(List<QAKeyValue<String, ?>> keyValues) {
        if (keyValues == null) {
            return;
        }
        
        for (QAKeyValue<String, ?> keyValue : keyValues) {
            set(keyValue);
        }
    }
    /**
     * 设置配置项
     * @param keyValue 配置项
     */
    public void set(QAKeyValue<String, ?> keyValue) {
        if (keyValue == null) {
            return;
        }
        set(keyValue.getKey(), keyValue.getValue());
    }
    /**
     * 设置配置项
     * @param key 配置项的名字
     * @param value 配置项的值
     */
    public void set(String key, Object value) {
        if (QAStringUtils.isEmpty(key)) {
            return;
        }
        this.put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    /**
     * 获取配置项的值
     * @param key 配置项的名字
     * @return 配置项的值
     */
    private <T> T get(String key) {
        try {
            return (T) super.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }
	
    
    /**
     * 初始化配置项
     */
    protected abstract void initialize();
	
	


    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        try {
            return (T) get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }
    
    public int getInt(String key) {
        try {
            return (Integer) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public float getFloat(String key) {
        try {
            return (Float) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public double getDouble(String key) {
        try {
            return (Double) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public short getShort(String key) {
        try {
            return (Short) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public byte getByte(String key) {
        try {
            return (Byte) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public boolean getBoolean(String key) {
        try {
            return (Boolean) getObject(key);
        } catch (Exception e) {
            return false;
        }
    }
    public char getChar(String key) {
        try {
            return (Character) getObject(key);
        } catch (Exception e) {
            return (char) -1;
        }
    }
    public long getLong(String key) {
        try {
            return (Long) getObject(key);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public String getString(String key, Object... args) {
        return String.format(getString(key), args);
    }
    public String getString(String key) {
        return (String) getObject(key);
    }
    public Date getDate(String key) {
        return (Date) getObject(key);
    }
    public java.sql.Date getSqlDate(String key) {
        return (java.sql.Date) getObject(key);
    }
    public byte[] getByteArray(String key) {
        return (byte[]) getObject(key);
    }
    public Bitmap getBitmap(String key) {
        return (Bitmap) getObject(key);
    }
    public Drawable getDrawable(String key) {
        return (Drawable) getObject(key);
    }
    public QAJson getJson(String key) {
        return (QAJson) getObject(key);
    }
	
}
