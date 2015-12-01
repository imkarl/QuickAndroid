package cn.jeesoft.qa.utils.type;

/**
 * 类型获取条件检测
 * @version v0.1.0 google 2014-11-04 摘自Gson
 */
class $Type$Preconditions {
    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
