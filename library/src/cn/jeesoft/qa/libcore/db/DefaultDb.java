package cn.jeesoft.qa.libcore.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import cn.jeesoft.qa.error.QADbException;
import cn.jeesoft.qa.error.QAException;

/**
 * 默认的数据库操作实现
 * @version v0.1.0 king 2015-01-19 数据库操作实现
 */
public class DefaultDb implements QADb {
    private SQLiteDatabase database;
    private QADbSession dbSession;
    
    private final Context context;
    private final CursorFactory factory;
    private final String dbDir;
    private final String dbName;
    private final int version;
    private final QADbListener listener;

    public DefaultDb(Context context, String dbDir, String dbName,
            CursorFactory factory, int version, QADbListener listener) {
        super();
        this.context = context;
        this.factory = factory;
        this.dbDir = dbDir;
        this.dbName = dbName;
        this.version = version;
        this.listener = listener;
    }
    
    public QADbSession getSession() {
        if (dbSession == null) {
            synchronized (this) {
                if (dbSession == null) {
                    QADbMaster dbMaster = new QADbMaster(getDatabase());
                    onRegister(dbMaster);
                    dbSession = dbMaster.newSession();
                }
            }
        }
        return dbSession;
    }
    
    public <T extends QADao<?, ?>> T getDbDao(Class<T> clazz) {
        return getSession().getDbDao(clazz);
    }
    
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public SQLiteDatabase getDatabase() throws QADbException {
        if (database == null) {
            synchronized (this) {
                if (database == null) {
                    SQLiteDatabase tempdb;
                    
                    // 创建并打开数据库
                    if (TextUtils.isEmpty(dbDir)) {
                        tempdb = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, factory);
                    } else {
                        File dir = new File(dbDir);
                        if (dir.exists() || dir.mkdirs()) {
                            File dbFile = new File(dbDir, dbName);
                            tempdb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                        } else {
                            throw new NullPointerException("db file not found.");
                        }
                    }

                    // 检测是否创建数据库
                    checkOnCreate(tempdb, dbName, version, listener);
                    // 检测是否版本升级
                    checkOnUpgrade(tempdb, dbName, version, listener);
                    database = tempdb;
                }
            }
        }
        return database;
    }

    /**
     * 检测是否创建数据库
     * @param database
     * @param dbName
     * @param version
     * @param listener
     */
    public static void checkOnCreate(SQLiteDatabase database,
            String dbName, int version, QADbListener listener) throws QADbException {
        if (database.getVersion() == 0) {
            // 创建数据库
            if (database.isReadOnly()) {
                throw new QADbException(QAException.CODE_READ_ONLY,
                        "Can't upgrade read-only database from version " +
                        database.getVersion() + " to " + version + ": " + dbName);
            }
            database.beginTransaction();
            try {
                if (listener != null) {
                    listener.onCreate(database);
                }
                database.setVersion(version);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                throw new QADbException(QAException.CODE_UNKNOW, e);
            } finally {
                database.endTransaction();
            }
        }
    }
    /**
     * 检测是否版本升级
     * @param database
     * @param dbName
     * @param version
     * @param listener
     */
    public static void checkOnUpgrade(SQLiteDatabase database,
            String dbName, int version, QADbListener listener) throws QADbException {
        int oldVersion = database.getVersion();
        int newVersion = version;
        
        if (newVersion < oldVersion) {
            throw new QADbException(QAException.CODE_NO_ALLOW,
                    "Can't back database from version " +
                    database.getVersion() + " to " + newVersion + ": " + dbName);
        } else if (newVersion > oldVersion) {
            if (database.isReadOnly()) {
                throw new QADbException(QAException.CODE_READ_ONLY,
                        "Can't upgrade read-only database from version " +
                        database.getVersion() + " to " + newVersion + ": " + dbName);
            }

            database.beginTransaction();
            try {
                if (listener != null) {
                    listener.onUpgrade(database, oldVersion, newVersion);
                }
                database.setVersion(newVersion);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                throw new QADbException(QAException.CODE_UNKNOW, e);
            } finally {
                database.endTransaction();
            }
        }
        
    }
    
    public void onCreate(SQLiteDatabase db) {
        if (listener != null) {
            listener.onCreate(db);
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (listener != null) {
            listener.onUpgrade(db, oldVersion, newVersion);
        }
    }
    public void onRegister(QADbMaster master) {
        if (listener != null) {
            listener.onRegister(master);
        }
    }

}
