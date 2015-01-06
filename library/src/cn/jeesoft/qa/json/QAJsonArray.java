package cn.jeesoft.qa.json;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAJsonException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.utils.version.QASdkVersion;
import cn.jeesoft.qa.utils.version.QASdkVersionCodes;

/**
 * 继承自org.json.JsonArray
 * @version v0.1.0 king 2014-11-10 增强存储的方法，去除抛异常
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class QAJsonArray extends JSONArray implements QAJson {
    
    public QAJsonArray() {
        super();
    }
    @SuppressWarnings("unchecked")
    <T>QAJsonArray(Object copyFrom) {
        this();
        
        if (copyFrom != null && copyFrom.getClass().isArray()) {
            copy((T[]) copyFrom);
        }
    }
    public <T>QAJsonArray(T[] copyFrom) {
        this();
        
        if (copyFrom != null && copyFrom.length > 0) {
            copy(copyFrom);
        }
    }
    public QAJsonArray(Collection<?> copyFrom) {
        this();
        
        if (copyFrom != null && !copyFrom.isEmpty()) {
            for (Iterator<?> it = copyFrom.iterator(); it.hasNext();) {
                put(QAJsonObject.wrapObject(it.next()));
            }
        }
    }
    public QAJsonArray(JSONTokener copyFrom) throws QAJsonException {
        this();
        
        if (copyFrom != null) {
            copy(copyFrom);
        }
    }
    public QAJsonArray(String copyFrom) throws QAJsonException {
        this();

        if (!TextUtils.isEmpty(copyFrom)) {
            copy(new JSONTokener(copyFrom));
        }
    }
    public QAJsonArray(QAJsonArray copyFrom) {
        this();
        copy(copyFrom);
    }
    QAJsonArray(JSONArray copyFrom) {
        this();
        copy(copyFrom);
    }
    
    private QAJsonArray copy(JSONArray copyFrom) {
        if (copyFrom != null && copyFrom.length() > 0) {
            for (int i=0; i<copyFrom.length(); i++) {
                Object value = copyFrom.opt(i);
                if (value != null) {
                    super.put(value);
                }
            }
        }
        return this;
    }
    private <T>QAJsonArray copy(T[] copyFrom) {
        if (copyFrom != null && copyFrom.length > 0) {
            final int length = Array.getLength(copyFrom);
            for (int i = 0; i < length; ++i) {
                put(QAJsonObject.wrapObject(Array.get(copyFrom, i)));
            }
        }
        return this;
    }
    private <T>QAJsonArray copy(JSONTokener copyFrom) {
        if (copyFrom == null) {
            throw new QANullException("'json source' can not be NULL.");
        }
        
        Object object = null;
        try {
            object = copyFrom.nextValue();
            copy((JSONArray) object);
        } catch (JSONException e) {
            throw new QAJsonException(QAException.CODE_FORMAT, e);
        } catch (ClassCastException e) {
            throw new QAJsonException(QAException.CODE_CLASSCAST, String.valueOf(object)+" cannot be cast to JSONArray");
        }
        return this;
    }
    

    /**
     * 判断某个index对应的值是否为空
     * @param name
     * @return true：不存在，或" "空字符串
     */
    public boolean isEmpty(int index) {
        return super.isNull(index) || TextUtils.isEmpty(String.valueOf(super.opt(index)));
    }
    
    
    
    
    
    
    
    /*
     * get
     */
    public boolean getBoolean(int index, boolean defValue) {
        boolean value = defValue;
        try {
            value = super.getBoolean(index);
        } catch (JSONException e) { }
        return value;
    }
    public double getDouble(int index, double defValue) {
        double value = defValue;
        try {
            value = super.getDouble(index);
        } catch (JSONException e) { }
        return value;
    }
    public int getInt(int index, int defValue) {
        int value = defValue;
        try {
            value = super.getInt(index);
        } catch (JSONException e) { }
        return value;
    }
    public QAJsonArray getJSONArray(int index, QAJsonArray defValue) {
        QAJsonArray value = defValue;
        try {
            value = new QAJsonArray(super.getJSONArray(index));
        } catch (JSONException e) { }
        if (value == null) {
            value = defValue;
        }
        return value;
    }
    public QAJsonObject getJSONObject(int index, QAJsonObject defValue) {
        QAJsonObject value = defValue;
        try {
            value = new QAJsonObject(super.getJSONObject(index));
        } catch (JSONException e) { }
        if (value == null) {
            value = defValue;
        }
        return value;
    }
    public long getLong(int index, long defValue) {
        long value = defValue;
        try {
            value = super.getLong(index);
        } catch (JSONException e) { }
        return value;
    }
    public String getString(int index, String defValue) {
        String value = defValue;
        try {
            value = super.getString(index);
        } catch (JSONException e) { }
        if (value == null) {
            value = defValue;
        }
        return value;
    }
    @Override
    public Object get(int index) {
        try {
            return super.get(index);
        } catch (JSONException e) {
            return null;
        }
    }
    @Override
    public boolean getBoolean(int index) {
        try {
            return super.getBoolean(index);
        } catch (JSONException e) {
            return false;
        }
    }
    @Override
    public double getDouble(int index) {
        try {
            return super.getDouble(index);
        } catch (JSONException e) {
            return -1;
        }
    }
    @Override
    public int getInt(int index) {
        try {
            return super.getInt(index);
        } catch (JSONException e) {
            return -1;
        }
    }
    @Override
    public QAJsonArray getJSONArray(int index) {
        try {
            return new QAJsonArray(super.getJSONArray(index));
        } catch (JSONException e) {
            return null;
        }
    }
    @Override
    public QAJsonObject getJSONObject(int index) {
        try {
            return new QAJsonObject(super.getJSONObject(index));
        } catch (JSONException e) {
            return null;
        }
    }
    @Override
    public long getLong(int index) {
        try {
            return super.getLong(index);
        } catch (JSONException e) {
            return -1;
        }
    }
    @Override
    public String getString(int index) {
        try {
            return super.getString(index);
        } catch (JSONException e) {
            return null;
        }
    }
    
    
    
    /*
     * set
     */
    public QAJsonArray put(Object value) {
        super.put(value);
        return this;
    }
    public QAJsonArray put(boolean value) {
        super.put(value);
        return this;
    }
    public QAJsonArray put(double value) {
        try {
            super.put(value);
        } catch (JSONException e) { }
        return this;
    }
    public QAJsonArray put(int value) {
        super.put(value);
        return this;
    }
    public QAJsonArray put(long value) {
        super.put(value);
        return this;
    }
    @Override
    public QAJsonArray put(int index, boolean value) {
        try {
            super.put(index, value);
        } catch (JSONException e) { }
        return this;
    }
    @Override
    public QAJsonArray put(int index, double value) {
        try {
            super.put(value);
        } catch (JSONException e) { }
        return this;
    }
    @Override
    public QAJsonArray put(int index, int value) {
        try {
            super.put(index, value);
        } catch (JSONException e) { }
        return this;
    }
    @Override
    public QAJsonArray put(int index, long value) {
        try {
            super.put(index, value);
        } catch (JSONException e) { }
        return this;
    }
    @Override
    public QAJsonArray put(int index, Object value) {
        try {
            super.put(index, value);
        } catch (JSONException e) { }
        return this;
    }
    
    
    
    @Override
    public QAJsonObject toJSONObject(JSONArray names) {
        try {
            return new QAJsonObject(super.toJSONObject(names));
        } catch (JSONException e) {
            return null;
        }
    }
    
    /**
     * 不建议低版本【API<19】调用
     * <pre>
     * 会在JsonArray中残留null值，且length()大小不会改变
     * 通过重新实例化JsonArray，可以清除所有null值
     * 例：
     *   jsonArray.remove(1);
     *   jsonArray = new JsonArray(jsonArray);
     * </pre>
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Object remove(int index) {
        if (QASdkVersion.isSupport(QASdkVersionCodes.KITKAT)) {
            return super.remove(index);
        } else {
            if (index < 0 || index >= length()) {
                return null;
            }

            Object value = get(index);
            try {
                super.put(index, (Object)null);
            } catch (JSONException e) {
                return null;
            }
            return value;
        }
    }
    

    /**
     * 判断是否数组
     * @return true
     */
    public boolean isArray() {
        return true;
    }
    
}
