package cn.jeesoft.qa.libcore.db;

import cn.jeesoft.qa.error.QADbException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作接口
 * @version v0.1.0 king 2015-01-19 数据库操作方法定义
 */
public interface QADb {
    
    /**
     * 数据库监听器
     * @version v0.1.0 king 2015-01-19 数据库监听方法定义
     */
    public static interface QADbListener {
        public void onCreate(SQLiteDatabase db);
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
        public void onRegister(QADbMaster master);
    }
    
    public <T extends QADao<?, ?>> T getDbDao(Class<T> clazz);
    public SQLiteDatabase getDatabase() throws QADbException;
    public int getVersion();
    
}
