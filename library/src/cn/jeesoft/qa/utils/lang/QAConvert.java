package cn.jeesoft.qa.utils.lang;

import android.text.TextUtils;

/**
 * 类型转换-工具类
 * @version v0.1.0 king 2014-11-18 类型转换处理
 */
public class QAConvert {

    /**
     * 字符串转长整形
     * @param obj
     * @param defValue 转换异常返回值
     * @return
     */
    public static long toLong(String obj, long defValue) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转双精度浮点型
     * @param str
     * @param defValue
     * @return
     */
    public static double toDouble(String str, double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转布尔值
     * @param b
     * @param defValue 转换异常返回值
     * @return
     */
    public static boolean toBoolean(String b, boolean defValue) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return defValue;
    }
    

    public static String toString(CharSequence value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return String.valueOf(value);
    }
    public static String toString(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }


    
    /**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) { }
        return defValue;
    }
    /**
     * 对象转整数
     * @param obj
     * @return 
     */
    public static int toInt(Object obj, int defValue) {
        if (obj == null)
            return defValue;
        return toInt(toString(obj), defValue);
    }

    public static char toChar(String str, char defValue) {
        try {
            return (char) toInt(str, defValue);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static short toShort(String str, short defValue) {
        try {
            return Short.parseShort(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static float toFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static byte toByte(String str, byte defValue) {
        try {
            return Byte.parseByte(str);
        } catch (Exception e) {
        }
        return defValue;
    }
    
    
    
    
    /**
     * 二进制转换为十六进制
     * @param binary
     * @return
     */
    public static String binary2hex(String binary) {
        String hexStr[] = { "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F" };
        int length = binary.length();
        int temp = length % 4;

        if (temp != 0) {
            for (int i = 0; i < 4 - temp; i++) {
                binary = "0" + binary;
            }
        }

        length = binary.length();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length / 4; i++) {
            int num = 0;

            for (int j = i * 4; j < i * 4 + 4; j++) {
                num <<= 1;// 左移
                num |= (binary.charAt(j) - '0');
            }
            sb.append(hexStr[num]);

        }
        return sb.toString();
    }
    
    /**
     * 全角转为半角（如，转换为,）
     */
    public static String toSingleByte(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    
}
