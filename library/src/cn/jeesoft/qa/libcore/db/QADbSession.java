package cn.jeesoft.qa.libcore.db;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.database.sqlite.SQLiteDatabase;
import cn.jeesoft.qa.error.QADbException;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoSupportException;
import cn.jeesoft.qa.utils.lang.QAClassUtils;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;

/**
 * 数据库会话
 * @version v0.1.0 king 2015-01-19 继承自AbstractDaoSession
 */
public class QADbSession extends AbstractDaoSession {

    private final Map<Class<? extends QADao<?, ?>>, QADaoConfig> mDaoConfigMap;
    
    /**
     * 构造数据库会话
     * @param db 数据库实例
     * @param type 有效范围
     * @param daoConfigMap 数据库配置项集合
     */
    @SuppressWarnings("unchecked")
    QADbSession(SQLiteDatabase db,
            IdentityScopeType type,
            Map<Class<? extends QADao<?, ?>>, QADaoConfig> daoConfigMap) {
        super(db);

        mDaoConfigMap = new HashMap<Class<? extends QADao<?,?>>, QADaoConfig>(daoConfigMap.size());
        for (Entry<Class<? extends QADao<?, ?>>, QADaoConfig> entry : daoConfigMap.entrySet()) {
            // 拷贝
            QADaoConfig config = entry.getValue().clone();
            config.initIdentityScope(type);
            mDaoConfigMap.put((Class<? extends QADao<?, ?>>) entry.getKey(), config);

            // 注册
            Class<? extends QADao<?,?>> clazz = entry.getKey();
            try {
                Constructor<? extends QADao<?, ?>> constructor = clazz.getConstructor(QADaoConfig.class, QADbSession.class);
                QADao<?, ?> dao = constructor.newInstance(mDaoConfigMap.get(clazz), this);
                registerDao(QAClassUtils.getGenericSuperclass(clazz)[0], dao);
            } catch (Exception e) {
                throw new QADbException(QAException.CODE_INSTANTIATION, "实例化QADao失败.", e);
            }
        }
    }

    public void clear() {
        for (Entry<Class<? extends QADao<?, ?>>, QADaoConfig> entry : mDaoConfigMap.entrySet()) {
            QADaoConfig config = entry.getValue();
            config.getIdentityScope().clear();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends QADao<?, ?>> T getDbDao(Class<T> clazz) throws QADbException {
        try {
            T dao = (T) super.getDao(QAClassUtils.getGenericSuperclass(clazz)[0]);
            return dao;
        } catch (NullPointerException e) {
            throw new QADbException(QAException.CODE_NULL, "获取泛型类型失败", e);
        } catch (ClassCastException e) {
            throw new QADbException(QAException.CODE_CLASSCAST, e);
        } catch (Exception e) {
            throw new QADbException(QAException.CODE_UNKNOW, e);
        }
    }
    
    @Override
    protected final <T> void registerDao(Class<T> entityClass, AbstractDao<T, ?> dao) {
        throw new QANoSupportException(QAException.CODE_NO_SUPPORT, "已抛弃的方法，请改用registerDao(daoClass)");
    }
    protected <T> void registerDao(Class<T> entityClass, QADao<T, ?> dao) {
        super.registerDao(entityClass, dao);
    }
    
}
