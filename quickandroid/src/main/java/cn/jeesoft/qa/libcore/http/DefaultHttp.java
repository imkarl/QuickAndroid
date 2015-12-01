package cn.jeesoft.qa.libcore.http;

import android.content.Context;

import java.util.Arrays;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.utils.lang.QAClassUtils;
import cn.jeesoft.qa.utils.lang.QAStringUtils;
import cn.jeesoft.qa.utils.log.QALog;

/**
 * 默认的HTTP网络请求实现
 * @version v0.1.0 king 2015-03-09 替换网络请求底层框架，提供更完善的类型支持，更多的参数设置
 * @version v0.1.0 king 2015-01-10 HTTP网络请求包装
 */
public class DefaultHttp implements QAHttp {
    
    private HttpClient mHttpClient;
    
    public DefaultHttp(QAPrivateCheck check) {
        super();
        QAPrivateCheck.check(check);
    }

    /**
     * 获取HttpClient
     */
    private HttpClient getHttpClient() {
        if (mHttpClient == null) {
            synchronized (this) {
                if (mHttpClient == null) {
                    mHttpClient = new HttpClient();
                }
            }
        }
        return mHttpClient;
    }
    

    public <T> void load(QAHttpMethod method, String url, QARequestParams params, QAHttpCallback<T> listener) {
        getHttpClient().load(method, url, params, listener);
    }

    public <T> void load(String url, QARequestParams params, QAHttpCallback<T> listener) {
        load(QAHttpMethod.GET, url, params, listener);
    }

    public <T> void load(QAHttpMethod method, String url, QAHttpCallback<T> listener) {
        load(method, url, null, listener);
    }

    public <T> void load(String url, QAHttpCallback<T> listener) {
        load(QAHttpMethod.GET, url, null, listener);
    }

    public <T> void load(QAHttpMethod method,
            String url, QARequestParams params,
            QAStringParser<T> parser, QAHttpCallback<T> listener) {
        getHttpClient().load(method, url, params, parser, listener);
    }
    
    public <T> void load(QAHttpMethod method,
            String url, QARequestParams params,
            QAJsonParser<T> parser, QAHttpCallback<T> listener) {
        getHttpClient().load(method, url, params, parser, listener);
    }


    




    /**
     * 获取返回数据的类型
     * @param listener
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getResultType(QAHttpCallback<T> listener) {
        Class<T> type;
        try {
            Class[] typs = QACore.qclass.getGenericInterfaces(listener.getClass());
            if (typs != null) {
                type = typs[0];
            } else {
                type = QACore.qclass.getGenericSuperclass(listener.getClass())[0];
            }
        } catch (Exception e) {
            throw new QANoFoundException(QAException.CODE_NO_TYPE,
                    "找不到自动解析类型[仅支持String/QAJson/File/Bitmap]", e);
        }
        return type;
    }
    /**
     * 获取返回数据的类型
     * @param parser
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getResultType(QAParser<?, T> parser) {
        final Class<T> type;
        if (parser instanceof QAJsonParser) {
            type = (Class<T>) QAJson.class;
        } else if (parser instanceof QAStringParser) {
            type = (Class<T>) String.class;
        } else {
            try {
                type = QACore.qclass.getGenericInterfaces(parser.getClass())[1];
            } catch (Exception e) {
                throw new QANoFoundException(QAException.CODE_NO_TYPE,
                        "找不到自动解析类型 "+parser+" [仅支持String/QAJson/File/Bitmap]", e);
            }
        }
        return type;
    }

//    public void clearCookie() {
//        getHttpClient().clearCookie();
//    }
    
}
