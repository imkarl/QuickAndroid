package cn.jeesoft.qa.libcore.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import cn.jeesoft.qa.model.pair.QAStringValuePair;
import cn.jeesoft.qa.utils.lang.QAStringUtils;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 网络请求参数
 * @version v0.1.0 king 2015-03-06 包装处理网络请求参数
 */
public class QARequestParams {
    public static final String APPLICATION_OCTET_STREAM = RequestParams.APPLICATION_OCTET_STREAM;
    public static final String APPLICATION_JSON = RequestParams.APPLICATION_JSON;


    final RequestParams mParams;
    Map<String, String> mHeaders;
    String mContentType;
    File mTargetFile;
    

    public QARequestParams() {
        this.mParams = new RequestParams();
    }

    public QARequestParams(Map<String,String> source) {
        this.mParams = new RequestParams(source);
    }

    public QARequestParams(String key, String value) {
        this.mParams = new RequestParams(key, value);
    }

    public QARequestParams(QAStringValuePair<String>... keysAndValues) {
        this.mParams = new RequestParams();

        int len = keysAndValues.length;
        for (int i = 0; i < len; i++) {
            QAStringValuePair<String> keyAndValue = keysAndValues[i];
            String key = keyAndValue.getKey();
            String val = keyAndValue.getValue();
            put(key, val);
        }
    }

    public void setContentEncoding(String encoding) {
        this.mParams.setContentEncoding(encoding);
    }

    public void put(String key, String value) {
        this.mParams.put(key, value);
    }

    public void put(String key, File file) throws FileNotFoundException {
        this.mParams.put(key, file);
    }

    public void put(String key, String customFileName, File file) throws FileNotFoundException {
        this.mParams.put(key, customFileName, file);
    }

    public void put(String key, File file, String contentType) throws FileNotFoundException {
        this.mParams.put(key, file, contentType);
    }

    public void put(String key, File file, String contentType, String customFileName) throws FileNotFoundException {
        this.mParams.put(key, file, contentType, customFileName);
    }

    public void put(String key, InputStream stream) {
        this.mParams.put(key, stream);
    }

    public void put(String key, InputStream stream, String name) {
        this.mParams.put(key, stream, name);
    }

    public void put(String key, InputStream stream, String name, String contentType) {
        this.mParams.put(key, stream, name, contentType);
    }

    public void put(String key, InputStream stream, String name, String contentType, boolean autoClose) {
        this.mParams.put(key, stream, name, contentType, autoClose);
    }

    public void put(String key, Object value) {
        this.mParams.put(key, value);
    }

    public void put(String key, int value) {
        this.mParams.put(key, value);
    }

    public void put(String key, long value) {
        this.mParams.put(key, value);
    }

    public void add(String key, String value) {
        this.mParams.add(key, value);
    }

    public void remove(String key) {
        this.mParams.remove(key);
    }

    public boolean has(String key) {
        return  this.mParams.has(key);
    }

    public void setHttpEntityIsRepeatable(boolean flag) {
        this.mParams.setHttpEntityIsRepeatable(flag);
    }

    public void setUseJsonStreamer(boolean flag) {
        this.mParams.setUseJsonStreamer(flag);
    }

    public void setElapsedFieldInJsonStreamer(String value) {
        this.mParams.setElapsedFieldInJsonStreamer(value);
    }

    public void setAutoCloseInputStreams(boolean flag) {
        this.mParams.setAutoCloseInputStreams(flag);
    }

    public HttpEntity getEntity(ResponseHandlerInterface progressHandler) throws IOException {
        return this.mParams.getEntity(progressHandler);
    }
    
    
    /*
     * 设置Header参数
     */
    public void addHeader(String name, String value) {
        if (this.mHeaders == null) {
            synchronized (this) {
                if (this.mHeaders == null) {
                    this.mHeaders = new HashMap<String, String>();
                }
            }
        }
        this.mHeaders.put(name, value);
    }
    public void removeHeader(String name) {
        if (this.mHeaders == null) {
            return;
        }
        
        this.mHeaders.remove(name);
    }
    
    
    Header[] toHeaderArray() {
        if (this.mHeaders == null) {
            return null;
        }
        
        Header[] headers = new Header[this.mHeaders.size()];
        
        int i=0;
        for (Map.Entry<String, String> entry : this.mHeaders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            headers[i++] = new BasicHeader(key, value);
        }
        return headers;
    }

    

    /**
     * 设置内容类型
     * @param contentType
     */
    public void setContentType(String contentType) {
        if (QAStringUtils.isEmpty(contentType)) {
            return;
        }
        this.mContentType = contentType;
    }
    /**
     * 设置目标文件（当请求类型为File时，必须设置）
     * @param targetFile
     */
    public void setTargetFile(File targetFile) {
        if (targetFile==null) {
            return;
        }
        this.mTargetFile = targetFile;
    }
    
    

    public String toString() {
        return  this.mParams.toString();
    }

}
