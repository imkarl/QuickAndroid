package cn.jeesoft.qa.libcore.db;

import android.database.Cursor;
import de.greenrobot.dao.AbstractDao;

/**
 * 数据访问对象
 * @version v0.1.0 king 2015-01-19 继承自AbstractDao
 * @param <T> 实体类类型
 * @param <K> 主键类型
 */
public abstract class QADao<T, K> extends AbstractDao<T, K> {

    public QADao(QADaoConfig config, QADbSession session) {
        super(config.getConfig(), session);
    }
    
    public QADbSession getSession() {
        return (QADbSession) session;
    }
    protected final <O> O loadCurrentOther(QADao<O, ?> dao, Cursor cursor, int offset) {
        return super.loadCurrentOther(dao, cursor, offset);
    }
    
}
