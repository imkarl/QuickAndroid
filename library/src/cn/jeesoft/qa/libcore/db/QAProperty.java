package cn.jeesoft.qa.libcore.db;

import de.greenrobot.dao.Property;

/**
 * 数据库列属性
 * @version v0.1.0 king 2015-01-19 继承自{@linkplain de.greenrobot.dao.Property Property}
 */
public class QAProperty extends Property {

    public QAProperty(int ordinal, Class<?> type, String name, boolean primaryKey, String columnName) {
        super(ordinal, type, name, primaryKey, columnName);
    }

}
