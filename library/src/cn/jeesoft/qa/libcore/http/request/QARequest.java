package cn.jeesoft.qa.libcore.http.request;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.jeesoft.qa.QACore;
import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAHttpException;
import cn.jeesoft.qa.error.QANoFoundException;
import cn.jeesoft.qa.error.QANullException;
import cn.jeesoft.qa.libcore.http.QAHttpCallback;
import cn.jeesoft.qa.libcore.http.QAHttpMethod;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * 支持GET、POST的请求
 * 
 * @param <V> 返回值类型
 * @version v0.1.0 king 2015-01-12 支持GET、POST
 */
public abstract class QARequest<V> extends Request<V> {
    
	private final QAHttpCallback<V> mListener;
	private final Map<String, String> mParams;
	private final String mUrl;
	private boolean isStart = false;
	

	public QARequest(QAHttpMethod method,
            final String url,
            Map<String, String> params,
            final QAHttpCallback<V> listener) {
        super((method==null ? QAHttpMethod.GET.getValue() : method.getValue()),
                parseGetUrl(method, url, params),
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    QAException exception = QAException.make(error);
                    listener.onFail(url, exception);
                }
            }
        });
        
        this.mUrl = url;
        this.mListener = listener;
        this.mParams = params;
    }
	public QARequest(QAHttpMethod method, String url, QAHttpCallback<V> listener) {
	    this(method, url, null, listener);
	}
	public QARequest(String url, Map<String, String> params, QAHttpCallback<V> listener) {
	    this(QAHttpMethod.GET, url, params, listener);
	}
	public QARequest(String url, QAHttpCallback<V> listener) {
	    this(QAHttpMethod.GET, url, null, listener);
	}
	
	/**
	 * 解析GET方式URL（补充URL请求参数）
	 * @param method
	 * @param url
	 * @param params
	 * @return 返回处理后的URL
	 */
	private static String parseGetUrl(QAHttpMethod method, String url, Map<String, String> params) {
		if (params!=null && method == QAHttpMethod.GET) {
			if (url.contains("?")) {
				if (url.endsWith("&")) {
					url = url.substring(0, url.length()-1);
				}
			} else {
				url += "?";
			}
			
			for (String key : params.keySet()) {
				url += "&"+key+"="+params.get(key);
			}
			url = url.replace("?&", "?");
		}
		return url;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (getMethod() == QAHttpMethod.GET.getValue()) {
			return super.getParams();
		}
		return (mParams!=null ? mParams : null);
	}
	
    @Override
    protected void deliverResponse(V response) {
        if (mListener != null) {
            mListener.onSuccessNet(this.mUrl, response);
        }
    }
    
    @Override
    public void addMarker(String tag) {
        if (!isStart) {
            if (mListener != null) {
                QACore.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isStart) {
                            isStart = true;
                            mListener.onStart(mUrl);
                        }
                    }
                });
            }
        }
        
        super.addMarker(tag);
    }
    
    @Override
    protected final Response<V> parseNetworkResponse(NetworkResponse response) {
        try {
            String resultData =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<V>) Response.success(parseResultData(resultData),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new QAVolleyError(new QANoFoundException(QAException.CODE_NO_ENCODING, e)));
        } catch (QAException e) {
            return Response.error(new QAVolleyError(e));
        } catch (NullPointerException e) {
            return Response.error(new QAVolleyError(new QANullException(e)));
        } catch (NoConnectionError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_NO_CONNECTION, e.getCause(), e.networkResponse)));
        } catch (NetworkError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_NETWORK, e.getCause(), e.networkResponse)));
        } catch (TimeoutError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_TIMEOUT, e.getCause(), e.networkResponse)));
        } catch (ParseError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_PARSER, e.getCause(), e.networkResponse)));
        } catch (AuthFailureError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_AUTH_FAILURE, e.getCause(), e.networkResponse)));
        } catch (ServerError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_SERVER, e.getCause(), e.networkResponse)));
        } catch (VolleyError e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_UNKNOW, e.getCause(), e.networkResponse)));
        } catch (Exception e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_UNKNOW, e, null)));
        } catch (Throwable e) {
            return Response.error(new QAVolleyError(new QAHttpException(QAException.CODE_UNKNOW, e, null)));
        }
    }
    
    /**
     * 解析数据
     * @param resultData 原始数据内容
     * @return 解析后的数据
     */
    abstract V parseResultData(String resultData) throws Throwable;
    
    
}
