package cn.jeesoft.qa.utils.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


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
        if (parent == subclass) {
            return true;
        }
        if (parent == null || subclass == null) {
            return false;
        }
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
    
    
    
    /**
     * 取父类泛型
     * @param clazz
     * @return 没有则返回null
     */
    @SuppressWarnings("rawtypes")
    public static Class[] getGenericSuperclass(Class<?> clazz) {
        try {
            Type typeGeneric = clazz.getGenericSuperclass();
            if (typeGeneric != null) {
                if (typeGeneric instanceof ParameterizedType) {
                    return getGeneric((ParameterizedType) typeGeneric);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * 取父接口泛型
     * @param clazz
     * @return 没有则返回null
     */
    @SuppressWarnings("rawtypes")
    public static Class[] getGenericInterfaces(Class<?> clazz) {
        try {
            Type typeGeneric = clazz.getGenericInterfaces()[0];
            if (typeGeneric != null) {
                if (typeGeneric instanceof ParameterizedType) {
                    return getGeneric((ParameterizedType) typeGeneric);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * 取泛型
     * @param clazz
     * @return 没有则返回null
     */
    @SuppressWarnings("rawtypes")
    public static Class[] getGeneric(ParameterizedType type) {
        try {
            if (type != null) {
                Type[] typeArgs = type.getActualTypeArguments();
                if (typeArgs != null && typeArgs.length > 0) {
                    Class[] args = new Class[typeArgs.length];
                    for (int i=0; i<typeArgs.length; i++) {
                        Type arg = typeArgs[i];
                        args[i] = (Class) arg;
                    }
                    return args;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    
    
}
