package cn.jeesoft.qa.libcore.http.handler;

import com.squareup.okhttp.Response;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;

public class QAJsonHttpHandler implements QAHttpHandler<QAJson> {
	
	@Override
	public QAJson handlerResponse(Response response) throws Exception {
		String data = response.body().string();
		return QAJsonUtils.fromJson(data);
	}
	
}
