package cn.jeesoft.qa.libcore.http;

import cn.jeesoft.qa.error.QAException;

/**
 * @version 0.1 king 2015-10
 */
public abstract class QASimpleHttpCallback<T> implements QAHttpCallback<T> {

    @Override
    public void onStart(String url) {

    }

    @Override
    public void onCancel(String url) {

    }

    @Override
    public void onSuccessCache(String url, T data) {

    }

    @Override
    public void onSuccessNet(String url, T data) {

    }

    @Override
    public void onFail(String url, QAException exception) {

    }

    @Override
    public void onProgress(String url, long current, long total) {

    }
}
