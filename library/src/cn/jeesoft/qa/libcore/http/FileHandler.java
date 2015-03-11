package cn.jeesoft.qa.libcore.http;

import java.io.File;

import org.apache.http.Header;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAHttpException;
import cn.jeesoft.qa.error.QANullException;

import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;

/**
 * 文件响应处理器
 * @version v0.1.0 king 2015-03-09 文件响应处理
 */
public class FileHandler extends RangeFileAsyncHttpResponseHandler {
    
    private final QAHttpCallback<File> listener;
    private final String url;
    
    public FileHandler(String url, QAHttpCallback<File> listener, File targetFile) {
        super(targetFile);
        checkTargetFile(targetFile);
        
        this.url = url;
        this.listener = listener;
    }
    
    /**
     * 检验目标文件是否有效
     * @param targetFile
     * @throws QANullException
     */
    private void checkTargetFile(File targetFile) throws QANullException {
        if (targetFile == null) {
            throw new QANullException("'targetFile' cannot be empty.");
        } else if (targetFile.isDirectory()) {
            throw new QANullException("'targetFile' must be File.");
        }
    }
    
    @Override
    public void onStart() {
        this.listener.onStart(this.url);
    }
    @Override
    public void onCancel() {
        this.listener.onCancel(this.url);
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers,
            File file) {
        if (file==null) {
            onFailure(statusCode, headers, null, file);
        } else {
            dispatchSuccess(statusCode, headers, file);
        }
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
            Throwable error, File file) {
        this.listener.onFail(this.url,
                new QAHttpException(QAException.CODE_UNKNOW, statusCode, error));
    }
    
    private void dispatchSuccess(int statusCode, Header[] headers, File file) {
        try {
            listener.onSuccessNet(url, file);
        } catch (Throwable e) {
            onFailure(statusCode, headers, e, file);
        }
    }

}
