package cn.jeesoft.qa.model.pair;

/**
 * 键值对（键为字符串）
 * @version v0.1.0 king 2014-10-11 键值对
 */
public class QAStringValuePair<V> extends QAKeyValue<String, V> {
    private static final long serialVersionUID = 1L;
    
    public QAStringValuePair() {
        super();
    }
    public QAStringValuePair(String key, V value) {
        super(key, value);
    }
    


    @Override
    public String toString() {
        return "StringValuePair [key=" + getKey() + ", value=" + getValue() + "]";
    }

}
