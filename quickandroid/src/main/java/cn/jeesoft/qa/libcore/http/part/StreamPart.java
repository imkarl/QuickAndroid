package cn.jeesoft.qa.libcore.http.part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.squareup.okhttp.RequestBody;

public class StreamPart extends Part<InputStream> {
	public StreamPart(String name, InputStream body) {
		super(name, body);
	}

	public RequestBody body() {
		byte[] result = null;
		
		ByteArrayOutputStream baos = null;
		byte[] buf = new byte[2048];
		int len = 0;
		try {
			baos = new ByteArrayOutputStream();
			while ((len = body.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			result = baos.toByteArray();
		} catch (IOException e) {
		} finally {
			try {
				if (body != null)
					body.close();
			} catch (IOException e) {
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
			}
		}
		
		if (result != null) {
			return RequestBody.create(null, result);
		} else {
			return null;
		}
	}
}
