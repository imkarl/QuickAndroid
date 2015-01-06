package cn.jeesoft.qa.model.pair;

/**
 * 键值对（键为数值）
 * @version v0.1.0 king 2014-10-11 键值对
 */
public class QAIntegerValue<V> extends QAKeyValue<Integer, V> {
    private static final long serialVersionUID = 1L;
    
    public QAIntegerValue() {
        super();
    }
    public QAIntegerValue(Integer key, V value) {
        super(key, value);
    }



    @Override
    public String toString() {
        return "IntegerValuePair [key=" + getKey() + ", value=" + getValue() + "]";
    }

}
