package cn.jeesoft.qa.libcore.http.handler;

import com.squareup.okhttp.Response;

public class QAStringHttpHandler implements QAHttpHandler<String> {
	
	@Override
	public String handlerResponse(Response response) throws Exception {
		return response.body().string();
	}
	
}
