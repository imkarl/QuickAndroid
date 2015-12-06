package cn.jeesoft.qa.libcore.http.part;

import android.text.TextUtils;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.RequestBody;

/**
 * HTTP请求内容部分
 * @param <T>
 */
public abstract class Part<T> {
	final String name;
	final T body;
	
	public Part(String name, T body) {
		this.name = name;
		this.body = body;
	}

	public String name() {
		return this.name;
	}
	public Headers header() {
		return Headers.of("Content-Disposition",
				"form-data; name=\"" + this.name + "\"");
	}
	public abstract RequestBody body();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Part<?> other = (Part<?>) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isEmpty() {
		if (TextUtils.isEmpty(name)) {
			return true;
		}
		if (body == null) {
			return true;
		}
		return false;
	}
}
