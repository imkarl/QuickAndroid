package cn.jeesoft.qa.simple;

import java.io.File;
import java.util.Date;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonObject;
import cn.jeesoft.qa.libcore.db.QADb;
import cn.jeesoft.qa.libcore.db.QADb.QADbListener;
import cn.jeesoft.qa.libcore.db.QADbMaster;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;
import cn.jeesoft.qa.libcore.http.QAJsonParser;
import cn.jeesoft.qa.libcore.http.QARequestParams;
import cn.jeesoft.qa.simple.adapter.AdapterActivity;
import cn.jeesoft.qa.simple.db.Note;
import cn.jeesoft.qa.simple.db.NoteDao;
import cn.jeesoft.qa.utils.log.QALog;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QACore.getManager().addActivity(this);
        
        setContentView(R.layout.activity_main);
        
        try {
            QACore.log.e(this);
            QACore.log.e(QACore.getApp());
            QACore.log.e(QACore.getConfig());
            QACore.log.e(QACore.getManager());
            QACore.log.e(QACore.getHandler());
            QACore.log.e();
            QACore.log.e("日志一", "日志二", "日志三");

            QACore.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    QACore.log.e(this);
                }
            }, 1000);
        } catch (QAException e) {
        	QACore.log.e(e);
        }
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        QACore.getManager().finishActivity(this);
    }
    
    /**
     * 测试异常拦截
     */
    public void testException(View view) {
        throw new NullPointerException("这是测试的异常");
    }
    
    /**
     * 测试HTTP请求
     */
    public void testHttp(View view) {
        String url = "http://www.baidu.com/s";
        QARequestParams params = new QARequestParams();
        params.put("wd", "android");
        QAHttpCallback<QAJson> listener = new QAHttpCallback<QAJson>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            // data不会为NULL
            @Override
            public void onSuccessNet(String url, QAJson data) {
                QALog.e(url, "onSuccessNet", data.getClass());
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, QAJson data) {
                QALog.e(url, "onSuccessCache", data.getClass());
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url, params, listener);
    }
    
    /**
     * 测试HTTP请求（Parser）
     */
    public void testHttpParser(final View view) {
        String url = "http://www.kuaidi100.com/query";
        
        QARequestParams params = new QARequestParams();
        params.put("type", "快递公司代号");
        params.put("postid", "快递单号");
        
        /**
         * 快递单信息
         */
        class Express implements QAJsonParser<Express> {
            int status;
            String message;
            
            @Override
            public Express parser(QAJson data) {
                QAJsonObject root = (QAJsonObject) data;
                this.status = root.getInt("status");
                this.message = root.getString("message");
                return this;
            }

            @Override
            public String toString() {
                return "Express [status=" + status + ", message=" + message + "]";
            }
        }
        
        QAHttpCallback<Express> listener = new QAHttpCallback<Express>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            @Override
            public void onSuccessNet(String url, Express data) {
                QALog.e(url, "onSuccessNet", data);
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, Express data) {
                QALog.e(url, "onSuccessCache", data);
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(null, QAHttpMethod.GET, url, params, new Express(), listener);
    }
    /**
     * 测试HTTP请求（File）
     */
    public void testHttpFile(final View view) {
        String url = "https://github.com/alafighting/QuickAndroid/archive/master.zip";
        
        QARequestParams params = new QARequestParams();
        params.setTargetFile(new File(QACore.file.getUsableDir(getPackageName()), "QuickAndroid-master.zip"));
        
        QAHttpCallback<File> listener = new QAHttpCallback<File>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            @Override
            public void onSuccessNet(String url, File data) {
                QALog.e(url, "onSuccessNet", data);
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, File data) {
                QALog.e(url, "onSuccessCache", data);
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url, params, listener);
    }
    
    /**
     * 测试HTTP请求（Bitmap）
     */
    public void testHttpImage(final View view) {
        String url = "https://raw.githubusercontent.com/alafighting/QuickAndroid/master/library/res/drawable-hdpi/qa_ic_logo.png";
        
        QAHttpCallback<Bitmap> listener = new QAHttpCallback<Bitmap>() {
            @Override
            public void onStart(String url) {
                QALog.e(url, "onStart");
            }
            @Override
            public void onCancel(String url) {
                QALog.e(url, "onCancel");
            }
            @SuppressWarnings("deprecation")
            @Override
            public void onSuccessNet(String url, Bitmap data) {
                QALog.e(url, "onSuccessNet", data);
                
                // 设置按钮的背景图片
                view.setBackgroundDrawable(new BitmapDrawable(getResources(), data));
            }
            // TODO 暂未实现
            @Override
            public void onSuccessCache(String url, Bitmap data) {
                QALog.e(url, "onSuccessCache", data);
            }
            @Override
            public void onFail(String url, QAException exception) {
                QALog.e(url, "onFail", exception);
            }
        };
        
        QACore.getHttp().load(QAHttpMethod.GET, url, null, listener);
    }
    
    /**
     * 测试Image图片加载
     */
    public void testImage(View view) {
        String url = "https://raw.githubusercontent.com/alafighting/QuickAndroid/master/library/res/drawable-hdpi/qa_ic_logo.png";
        QACore.getImage().load(view, url);
    }
    
    /**
     * 测试DB读写
     */
    public void testDb(View view) {
        QADb helper = QACore.getDb(this, new QADbListener() {
            @Override
            public void onCreate(SQLiteDatabase db) {
                QALog.e("第一次创建数据库");
                NoteDao.createTable(db, false);
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                QALog.e("升级数据库");
                NoteDao.dropTable(db, true);
                NoteDao.createTable(db, false);
            }
            @Override
            public void onRegister(QADbMaster master) {
                master.registerDao(NoteDao.class);
            }
        });
        NoteDao noteDao = helper.getDbDao(NoteDao.class);
        
        // 插入数据
        QALog.e("insert=>"+noteDao.insert(new Note(null, "text", "comment", new Date())));
        // 读取数据
        QALog.e("loadAll=>"+noteDao.loadAll());
        
        QACore.toast.show(this, "执行完成，请通过LogCat查看结果");
    }

    /**
     * 测试极简Adapter
     */
    public void testAdapter(View view) {
        startActivity(new Intent(this, AdapterActivity.class));
    }
}
