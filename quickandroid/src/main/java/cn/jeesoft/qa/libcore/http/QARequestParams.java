package cn.jeesoft.qa.libcore.http;

import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import cn.jeesoft.qa.model.pair.QAStringValuePair;
import cn.jeesoft.qa.utils.lang.QAStringUtils;

/**
 * 网络请求参数
 * @version v0.1.0 king 2015-03-06 包装处理网络请求参数
 */
public class QARequestParams {

    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");

    final MultipartBuilder mBuilder;
    private String mBody;
    Map<String, String> mParams;
    Headers.Builder mHeaders;
    String mContentType;
    File mTargetFile;
    String contentEncoding;
    

    public QARequestParams() {
        this.mBuilder = new MultipartBuilder();
        this.mParams = new HashMap<>();
    }

    public QARequestParams(Map<String,String> source) {
        this();
        this.put(source);
    }

    public QARequestParams(String key, String value) {
        this();
        this.put(key, value);
    }

    public QARequestParams(QAStringValuePair<String>... keysAndValues) {
        this();
        this.put(keysAndValues);
    }

    public void setContentEncoding(String encoding) {
        this.contentEncoding = encoding;
    }

    public void put(QAStringValuePair<String>... keysAndValues) {
        if (keysAndValues == null || keysAndValues.length <= 0) {
            return;
        }

        int len = keysAndValues.length;
        for (int i = 0; i < len; i++) {
            QAStringValuePair<String> keyAndValue = keysAndValues[i];
            if (keyAndValue != null) {
                String key = keyAndValue.getKey();
                String val = keyAndValue.getValue();
                put(key, val);
            }
        }
    }
    public void put(Map<String,String> source) {
        if (source == null || source.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    public void put(String key, long value) {
        this.put(key, String.valueOf(value));
    }
    public void put(String key, String value) {
        if (QAStringUtils.isEmpty(value)) {
//            throw new NullPointerException("param can't be null.");
            return;
        }
//        mBuilder.add(key, value);
        mBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                RequestBody.create(null, value));
        mParams.put(key, value);
        mBody = null;
    }


    public void put(String key, File file) throws FileNotFoundException {
        put(key, null, file);
    }
    public void put(String key, String customFileName, File file) throws FileNotFoundException {
        if (file == null || !file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("文件不存在");
        }

        String fileName = file.getName();
        RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
        if (TextUtils.isEmpty(customFileName)) {
            customFileName = fileName;
        }
        mBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + customFileName + "\""),
                fileBody);
        mParams.put(key, file.getAbsolutePath());
        mBody = null;
    }

    // TODO
//    public void put(String key, InputStream stream) {
//        this.mBuilder.put(key, stream);
//    }
//
//    public void remove(String key) {
//        this.mBuilder.remove(key);
//    }
//
//    public boolean has(String key) {
//        return this.mBuilder.has(key);
//    }

    public void setBody(String body) {
        if (TextUtils.isEmpty(body)) {
            body = null;
        }
        mBody = body;
    }

    
    /*
     * 设置Header参数
     */
    public void addHeader(String name, String value) {
        if (this.mHeaders == null) {
            synchronized (this) {
                if (this.mHeaders == null) {
                    this.mHeaders = new Headers.Builder();
                }
            }
        }
        this.mHeaders.add(name, value);
    }
    public void removeHeader(String name) {
        if (this.mHeaders == null) {
            return;
        }
        
        this.mHeaders.removeAll(name);
    }
    
    
    Headers toHeaderArray() {
        if (this.mHeaders == null) {
            return new Headers.Builder().build();
        }
        return mHeaders.build();
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

    public RequestBody build() {
        if (!TextUtils.isEmpty(mBody)) {
            return RequestBody.create(MEDIA_TYPE_TEXT, mBody);
        }
        try {
            return mBuilder.build();
        } catch (IllegalStateException e) {
            return new FormEncodingBuilder().build();
        }
    }
    

    public String toString() {
        return  this.mParams.toString();
    }

    private static String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
