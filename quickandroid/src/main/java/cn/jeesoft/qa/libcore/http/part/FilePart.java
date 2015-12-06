package cn.jeesoft.qa.libcore.http.part;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import android.text.TextUtils;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

public class FilePart extends Part<InputStream> {
	private final String filename;
	public FilePart(String name, File file) throws FileNotFoundException {
		this(name, file, null);
	}
	public FilePart(String name, File file, String customFileName) throws FileNotFoundException {
		super(name, new FileInputStream(file));
        if (file == null || !file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("文件不存在");
        }
		this.filename = (TextUtils.isEmpty(customFileName) ? file.getName() : customFileName);
	}
	public FilePart(String name, InputStream stream, String filename) throws FileNotFoundException {
		super(name, stream);
        if (TextUtils.isEmpty(filename)) {
            throw new FileNotFoundException("'filename'不能为空");
        }
		this.filename = filename;
	}
	@Override
	public Headers header() {
		return Headers.of("Content-Disposition",
				"form-data; name=\"" + name + "\"; filename=\"" + filename + "\"");
	}
	public RequestBody body() {
		return RequestBody.create(MediaType.parse(guessMimeType(filename)), String.valueOf(this.body));
	}
	

    private static String guessMimeType(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(filename);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
