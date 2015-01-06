package cn.jeesoft.qa.utils.lang;


/**
 * Class帮助类
 * @version v0.1.0 king 2014-11-21 Class类型判断
 */
public class QAClassUtils {
    
    /**
     * 判断是否继承/接口实现关系
     * @param parent 父类
     * @param subclass 之类
     * @return
     */
    public static boolean isFrom(Class<?> parent, Class<?> subclass) {
        return parent.isAssignableFrom(subclass);
    }
    
    /**
     * 取类名
     * @param clazz
     * @return
     */
    public static String getShortClassName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
    /**
     * 取包名
     * @param clazz
     * @return 没有则返回null
     */
    public static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastIndex = className.lastIndexOf(".");
        if (lastIndex != -1) {
            return className.substring(0, lastIndex);
        } else {
            return null;
        }
    }
    
    
    
}
