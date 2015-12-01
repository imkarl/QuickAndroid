package cn.jeesoft.qa.libcore.http;

import cn.jeesoft.qa.error.QAException;

/**
 * HTTP请求的回调接口
 * @param <T> 返回值类型
 * @version v0.1.0 king 2015-01-10 HTTP请求的回调接口
 */
public interface QAHttpCallback<T> {
    
    /**
     * 开始发送请求
     */
    public void onStart(String url);
    /**
     * 取消请求
     */
    public void onCancel(String url);
    /**
     * 加载缓存数据成功
     */
    public void onSuccessCache(String url, T data);
    /**
     * 加载网络数据成功
     */
    public void onSuccessNet(String url, T data);
    /**
     * 加载网络数据失败
     */
    public void onFail(String url, QAException exception);

    /**
     * 加载进度更新
     * @param current 当前进度
     * @param total 总进度
     */
    public void onProgress(String url, long current, long total);

}
