package cn.jeesoft.qa.libcore.db;

import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * 数据访问配置
 * @version v0.1.0 king 2015-01-20 数据访问配置包装
 */
public class QADaoConfig {
    
    private DaoConfig mDaoConfig;
    
    QADaoConfig(DaoConfig config) {
        this.mDaoConfig = config;
    }
    public QADaoConfig(QADaoConfig config) {
        this.mDaoConfig = config.getConfig();
    }
    
    DaoConfig getConfig() {
        return this.mDaoConfig;
    }
    
    void initIdentityScope(IdentityScopeType type) {
        this.mDaoConfig.initIdentityScope(type);
    }
    IdentityScope<?, ?> getIdentityScope() {
        return this.mDaoConfig.getIdentityScope();
    }
    
    public QADaoConfig clone() {
        this.mDaoConfig = this.mDaoConfig.clone();
        return this;
    }
    
}
