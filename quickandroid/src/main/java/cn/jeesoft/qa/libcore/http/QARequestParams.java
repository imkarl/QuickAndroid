package cn.jeesoft.qa.libcore.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.libcore.http.part.FilePart;
import cn.jeesoft.qa.libcore.http.part.Part;
import cn.jeesoft.qa.libcore.http.part.StreamPart;
import cn.jeesoft.qa.libcore.http.part.StringPart;

/**
 * 网络请求参数
 * @version v0.1.0 king 2015-03-06 包装处理网络请求参数
 */
@SuppressWarnings("rawtypes")
public class QARequestParams {

    private String mBody;
    private Map<String, List<Part>> mParams;
    private Map<String, String> mHeaders;
    private boolean mPreLoad = false;
//    private String mContentType;
    private File mTargetFile;
//    private String contentEncoding;
    

    public QARequestParams() {
        this.mParams = new HashMap<String, List<Part>>();
        this.mHeaders = new HashMap<String, String>();
    }
    public QARequestParams(Part... parts) {
        this();
        this.putParams(parts);
    }

    public QARequestParams putParams(Part... parts) {
        if (parts != null && parts.length > 0) {
	        int len = parts.length;
	        for (int i = 0; i < len; i++) {
	        	putParam(parts[i]);
	        }
        }
        return this;
    }
    public QARequestParams putParam(Part part) {
        if (part != null && !part.isEmpty()) {
        	List<Part> parts;
        	if (mParams.containsKey(part.name())) {
        		parts = mParams.get(part.name());
        	} else {
        		parts = new ArrayList<Part>();
			}
            mParams.put(part.name(), parts);
        }
        return this;
    }

    public QARequestParams putParam(String key, String value) {
    	putParam(new StringPart(key, value));
        return this;
    }
    public QARequestParams putParam(String key, File file) throws FileNotFoundException {
    	putParam(new FilePart(key, file));
        return this;
    }
    public QARequestParams putParam(String key, String customFileName, File file) throws FileNotFoundException {
    	putParam(new FilePart(key, file, customFileName));
        return this;
    }
    public QARequestParams putParam(String key, InputStream stream) {
    	putParam(new StreamPart(key, stream));
        return this;
    }
    public QARequestParams putParam(String key, byte[] bytes) {
    	putParam(new StreamPart(key, new ByteArrayInputStream(bytes)));
        return this;
    }

    /**
     * 删除参数（如果key对应多个参数值，则全部删除）
     * @param key
     */
    public void removeParam(String key) {
        this.mParams.remove(key);
    }
    /**
     * 清空参数
     */
    public void clearParams() {
    	this.mParams.clear();
    }
    /**
     * 是否包含此参数
     * @param key
     * @return
     */
    public boolean containsParam(String key) {
        return this.mParams.containsKey(key);
    }
    
    /**
     * 设置Header参数
     */
    public QARequestParams putHeader(String name, String value) {
        this.mHeaders.put(name, value);
        return this;
    }
    public void removeHeader(String name) {
        this.mHeaders.remove(name);
    }
    /**
     * 清空Header参数
     */
    public void clearHeaders() {
    	this.mHeaders.clear();
    }
    /**
     * 判断Header是否包含此参数
     * @param key
     * @return
     */
    public boolean containsHeader(String key) {
        return this.mHeaders.containsKey(key);
    }


	public Map<String, List<Part>> getParams() {
        return  this.mParams;
    }
    public Map<String, String> getHeaders() {
        return mHeaders;
    }


    public QARequestParams setBody(String body) {
        if (TextUtils.isEmpty(body)) {
            body = null;
        }
        mBody = body;
        return this;
    }
    public String getBody() {
        return mBody;
    }


    /**
     * 设置是否预加载（读取缓存，通过onSuccessCache()回调通知）
     * @param enable
     */
    public QARequestParams setPreLoad(boolean enable) {
    	this.mPreLoad = enable;
        return this;
    }
    /**
     * 是否预加载
     * @return
     */
    public boolean isPreLoad() {
    	return mPreLoad;
    }
    

    // TODO
//    /**
//     * 设置内容类型
//     * @param contentType
//     */
//    public void setContentType(String contentType) {
//        if (QAStringUtils.isEmpty(contentType)) {
//            return;
//        }
//        this.mContentType = contentType;
//    }
    /**
     * 设置目标文件（当返回数据类型为File时，必须设置）
     * @param targetFile
     */
    public QARequestParams setTargetFile(File targetFile) {
        if (targetFile==null) {
			throw new QANoFoundException(QAException.CODE_IO_FILE, "'targetFile'不能为空");
        }
        this.mTargetFile = targetFile;
        return this;
    }
	public File getTargetFile() {
		return mTargetFile;
	}


    public String toString() {
        return  "headers=["+this.mHeaders.toString()+"], params=["+this.mParams.toString()+"]";
    }

}
