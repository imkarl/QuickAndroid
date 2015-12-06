package cn.jeesoft.qa.libcore.http.part;

import com.squareup.okhttp.RequestBody;

public class StringPart extends Part<String> {
	public StringPart(String name, String body) {
		super(name, body);
	}
	public StringPart(String name, Object body) {
		super(name, String.valueOf(body));
	}
	
	public RequestBody body() {
		return RequestBody.create(null, this.body);
	}
	
	public String value() {
		return this.body;
	}
}
