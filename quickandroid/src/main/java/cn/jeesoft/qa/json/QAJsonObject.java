package cn.jeesoft.qa.json;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAJsonException;
import cn.jeesoft.qa.error.QANullException;

/**
 * 继承自org.json.JSONObject
 *
 * @version v0.1.0 king 2014-11-10 增强存储的方法，去除抛异常
 */
public class QAJsonObject extends JSONObject implements QAJson {

    public QAJsonObject(String copyFrom) throws QAJsonException {
        this();

        if (!TextUtils.isEmpty(copyFrom)) {
            copy(new JSONTokener(copyFrom));
        }
    }

    public QAJsonObject(Map<?, ?> copyFrom) {
        super(copyFrom);
    }

    public QAJsonObject() {
        super();
    }

    public QAJsonObject(JSONTokener copyFrom) throws QAJsonException {
        super();

        copy(copyFrom);
    }

    public QAJsonObject(QAJsonObject copyFrom, String[] names) {
        super();

        if (names != null && names.length > 0) {
            copy(copyFrom, Arrays.asList(names).iterator());
        }
    }

    public QAJsonObject(QAJsonObject copyFrom) {
        super();
        copy(copyFrom);
    }

    public QAJsonObject(JSONObject copyFrom) {
        super();
        copy(copyFrom);
    }

    private QAJsonObject copy(JSONObject copyFrom) {
        if (copyFrom != null) {
            return copy(copyFrom, copyFrom.keys());
        } else {
            return this;
        }
    }

    private QAJsonObject copy(JSONObject copyFrom, Iterator<String> names) {
        if (copyFrom != null && names != null) {
            while (names.hasNext()) {
                String name = names.next();
                Object value = copyFrom.opt(name);
                if (value != null) {
                    try {
                        super.put(name, value);
                    } catch (JSONException e) {
                    }
                }
            }
        }
        return this;
    }

    private <T> QAJsonObject copy(JSONTokener copyFrom) {
        if (copyFrom == null) {
            throw new QANullException("'json source' can not be NULL.");
        }

        Object object = null;
        try {
            object = copyFrom.nextValue();
            copy((JSONObject) object);
        } catch (JSONException e) {
            throw new QAJsonException(QAException.CODE_FORMAT, e);
        } catch (ClassCastException e) {
            throw new QAJsonException(QAException.CODE_CLASSCAST, String.valueOf(object) + " cannot be cast to JSONArray");
        }
        return this;
    }


    /**
     * 判断某个name对应的值是否为空
     *
     * @param name
     * @return true：不存在，或" "空字符串
     */
    public boolean isEmpty(String name) {
        return super.isNull(name) || TextUtils.isEmpty(String.valueOf(super.opt(name)));
    }


    /*
     * getter
     */
    public boolean getBoolean(String name, boolean defValue) {
        boolean value = defValue;
        try {
            value = super.getBoolean(name);
        } catch (JSONException e) {
        }
        return value;
    }

    public double getDouble(String name, double defValue) {
        double value = defValue;
        try {
            value = super.getDouble(name);
        } catch (JSONException e) {
        }
        return value;
    }

    public int getInt(String name, int defValue) {
        int value = defValue;
        try {
            value = super.getInt(name);
        } catch (JSONException e) {
        }
        return value;
    }

    public QAJsonArray getJSONArray(String name, QAJsonArray defValue) {
        QAJsonArray value = defValue;
        try {
            value = new QAJsonArray(super.getJSONArray(name));
        } catch (JSONException e) {
        }
        if (value == null) {
            value = defValue;
        }
        return value;
    }

    public QAJsonObject getJSONObject(String name, QAJsonObject defValue) {
        QAJsonObject value = defValue;
        try {
            value = new QAJsonObject(super.getJSONObject(name));
        } catch (JSONException e) {
        }
        if (value == null) {
            value = defValue;
        }
        return value;
    }

    public long getLong(String name, long defValue) {
        long value = defValue;
        try {
            value = super.getLong(name);
        } catch (JSONException e) {
        }
        return value;
    }

    public Date getDate(String name) {
        return getDate(name, null);
    }

    public Date getDate(String name, Date defValue) {
        Date value = defValue;
        long time = getLong(name, -1);

        if (time > new GregorianCalendar(1997, 1, 1).getTimeInMillis()) {
            value = new Date(time);
        }
        return value;
    }

    public String getString(String name, String defValue) {
        try {
            return super.getString(name);
        } catch (JSONException e) {
            return defValue;
        }
    }

    @Override
    public QAJsonArray getJSONArray(String name) {
        try {
            return new QAJsonArray(super.getJSONArray(name));
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public QAJsonObject getJSONObject(String name) {
        try {
            return new QAJsonObject(super.getJSONObject(name));
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public Object get(String name) {
        try {
            return super.get(name);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public boolean getBoolean(String name) {
        try {
            return super.getBoolean(name);
        } catch (JSONException e) {
            return false;
        }
    }

    @Override
    public double getDouble(String name) {
        try {
            return super.getDouble(name);
        } catch (JSONException e) {
            return -1;
        }
    }

    @Override
    public int getInt(String name) {
        try {
            return super.getInt(name);
        } catch (JSONException e) {
            return -1;
        }
    }

    @Override
    public long getLong(String name) {
        try {
            return super.getLong(name);
        } catch (JSONException e) {
            return -1;
        }
    }

    @Override
    public String getString(String name) {
        try {
            return super.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }


    @Override
    public QAJsonArray names() {
        return new QAJsonArray(super.names());
    }

    @Override
    public QAJsonArray optJSONArray(String name) {
        return new QAJsonArray(super.optJSONArray(name));
    }

    @Override
    public QAJsonObject optJSONObject(String name) {
        return new QAJsonObject(super.optJSONObject(name));
    }

    @Override
    public QAJsonArray toJSONArray(JSONArray names) {
        try {
            return new QAJsonArray(super.toJSONArray(names));
        } catch (JSONException e) {
            return null;
        }
    }


    /*
     * set
     */
    public QAJsonObject putOpt(String name, Object value) {
        try {
            super.putOpt(name, value);
        } catch (JSONException e) {
        }
        return this;
    }

    public QAJsonObject put(String name, Object value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
        }
        return this;
    }

    public QAJsonObject put(String name, boolean value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
        }
        return this;
    }

    public QAJsonObject put(String name, double value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
        }
        return this;
    }

    public QAJsonObject put(String name, int value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
        }
        return this;
    }

    public QAJsonObject put(String name, long value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
        }
        return this;
    }


    /**
     * 判断是否含有该Key值
     */
    @Override
    public boolean has(String name) {
        return super.has(name) && !isNull(name);
    }

    @Override
    public QAJsonObject accumulate(String name, Object value) {
        try {
            return new QAJsonObject(super.accumulate(name, value));
        } catch (JSONException e) {
            return null;
        }
    }


    /**
     * wrap
     */
    public static Object wrapObject(Object o) {
        if (o == null) {
            return NULL;
        }
        if (o instanceof JSONArray) {
            return new QAJsonArray((JSONArray) o);
        } else if (o instanceof JSONObject) {
            return new QAJsonObject((JSONObject) o);
        }
        if (o.equals(NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new QAJsonArray((Collection<?>) o);
            } else if (o.getClass().isArray()) {
                return new QAJsonArray(o);
            }
            if (o instanceof Map) {
                return new QAJsonObject((Map<?, ?>) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }


    /**
     * 判断是否数组
     *
     * @return false
     */
    public boolean isArray() {
        return false;
    }

}
