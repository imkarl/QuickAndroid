package cn.jeesoft.qa.libcore.http.handler;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonObject;
import cn.jeesoft.qa.json.QAJsonUtils;

public class QAJSONArrayHttpHandler implements QAHttpHandler<JSONArray> {
	
	@Override
	public JSONArray handlerResponse(Response response) throws Exception {
		String data = response.body().string();
		QAJson json = QAJsonUtils.fromJson(data);
		if (json != null && json instanceof JSONArray) {
			return (JSONArray) json;
		} else {
			// TODO 更详细的错误提示
			return null;
		}
	}
	
}
