package cn.jeesoft.qa.libcore.http;

import org.apache.http.Header;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.json.QAJson;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.utils.lang.QAClassUtils;

/**
 * 支持解析的网络响应处理器
 * @version v0.1.0 king 2015-03-10 实现数据解析
 * @param <Value> 返回值类型
 * @param <Data> 解析器类型
 */
public class ParserHandler<Value, Data> extends TextHandler<Value> {
    
    private QAParser<Value, Data> mParser;
    
    public ParserHandler(String url, QAHttpCallback<Value> listener,
            QAParser<Value, Data> parser) {
        super(url, listener);
        this.mParser = parser;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    void dispatchSuccess(int statusCode, Header[] headers, String data) {
        Value value = null;
        try {
            Class<Data> dataClass = DefaultHttp.getResultType(mParser);
            
            if (QAClassUtils.isFrom(QAStringParser.class, type)
                    || QAClassUtils.isFrom(String.class, dataClass)) {
                // 文本
                value = parser((Data) data);
            } else if (QAClassUtils.isFrom(QAJsonParser.class, type)
                    || QAClassUtils.isFrom(QAJson.class, dataClass)) {
                // JSON
                value = parser((Data) QAJsonUtils.fromJson(data));
            } else {
                throw new QANoFoundException(QAException.CODE_NO_SUPPORT,
                        "不支持的自动解析类型 "+type+" [仅支持String/QAJson/File/Bitmap]");
            }
        } catch (QAException e) {
            onFailure(statusCode, headers, data, e);
        } catch (Throwable e) {
            onFailure(statusCode, headers, data, e);
        } finally {
            if (value != null) {
                listener.onSuccessNet(url, value);
            } else {
                onFailure(statusCode, headers, data,
                        new QANullException("返回数据为空"));
            }
        }
    }
    
    private Value parser(Data data) {
        return mParser.parser(data);
    }
    
}
