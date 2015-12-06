package cn.jeesoft.qa.libcore.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonObject;
import cn.jeesoft.qa.json.QAJsonUtils;

public class QAFastJsonHttpHandler implements QAHttpHandler<JSON> {
	
	@Override
	public JSON handlerResponse(Response response) throws Exception {
		String data = response.body().string();
		QAJson json = QAJsonUtils.fromJson(data);
		if (json != null) {
			if (json instanceof QAJsonObject) {
				return JSON.parseObject(data);
			} else {
				return JSON.parseArray(data);
			}
		} else {
			// TODO 更详细的错误提示
			return null;
		}
	}
	
}
