package cn.jeesoft.qa.libcore.http;

/**
 * 数据解析器
 * @param <T> 返回值类型
 * @param <D> 原始数据类型
 * @version v0.1.0 king 2015-01-12 定义数据解析器方法
 */
public interface QAParser<T, D> {
    
    public T parser(D data);
    
}
