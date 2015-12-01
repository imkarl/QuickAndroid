package cn.jeesoft.qa.libcore.db;

import java.util.HashMap;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoSupportException;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

/**
 * 数据库控制器
 * @version v0.1.0 king 2015-01-19 继承自AbstractDaoMaster
 */
public class QADbMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1000;

    private final Map<Class<? extends QADao<?, ?>>, QADaoConfig> mDaoConfigMap;
    
    public QADbMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        mDaoConfigMap = new HashMap<Class<? extends QADao<?, ?>>, QADaoConfig>();
    }
    
    public QADbSession newSession() {
        return new QADbSession(db, IdentityScopeType.Session, mDaoConfigMap);
    }
    
    public QADbSession newSession(IdentityScopeType type) {
        return new QADbSession(db, type, mDaoConfigMap);
    }
    
    @Override
    protected final void registerDaoClass(Class<? extends AbstractDao<?, ?>> daoClass) {
        throw new QANoSupportException(QAException.CODE_NO_SUPPORT, "已抛弃的方法，请改用registerDao(daoClass)");
    }
    public void registerDao(Class<? extends QADao<?, ?>> daoClass) {
        super.registerDaoClass(daoClass);
        mDaoConfigMap.put(daoClass, new QADaoConfig(daoConfigMap.get(daoClass)));
    }
    
    Map<Class<? extends QADao<?, ?>>, QADaoConfig> getDaoConfig() {
        return this.mDaoConfigMap;
    }
    
}
