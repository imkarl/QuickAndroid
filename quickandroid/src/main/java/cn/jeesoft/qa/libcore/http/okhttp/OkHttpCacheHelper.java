package cn.jeesoft.qa.libcore.http.okhttp;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.Version;
import com.squareup.okhttp.internal.http.AuthenticatorAdapter;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RequestException;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.tls.OkHostnameVerifier;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.ProtocolException;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import cn.jeesoft.qa.utils.log.QALog;

import static com.squareup.okhttp.internal.http.HttpEngine.MAX_FOLLOW_UPS;

/**
 * OkHttp缓存读取辅助类
 *
 * @author king
 */
class OkHttpCacheHelper {
    private OkHttpCacheHelper() {
    }

    /**
     * 读取缓存
     *
     * @param request
     * @return
     */
    public static final Response getCache(OkHttpClient client, Request request) {
        try {
            Response response = getResponse(client, request);
            if (response != null && response.isSuccessful()) {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static OkHttpClient copyWithDefaults(OkHttpClient result) {
        try {
            Method method = OkHttpClient.class.getMethod("copyWithDefaults");
            method.setAccessible(true);
            method.invoke(result);
        } catch (Exception e) {
            QALog.e(e);

            if (result.getProxySelector() == null) {
                result.setProxySelector(ProxySelector.getDefault());
            }
            if (result.getCookieHandler() == null) {
                result.setCookieHandler(CookieHandler.getDefault());
            }
            if (result.getSocketFactory() == null) {
                result.setSocketFactory(SocketFactory.getDefault());
            }
            if (result.getSslSocketFactory() == null) {
                result.setSslSocketFactory(getDefaultSSLSocketFactory());
            }
            if (result.getHostnameVerifier() == null) {
                result.setHostnameVerifier(OkHostnameVerifier.INSTANCE);
            }
            if (result.getCertificatePinner() == null) {
                result.setCertificatePinner(CertificatePinner.DEFAULT);
            }
            if (result.getAuthenticator() == null) {
                result.setAuthenticator(AuthenticatorAdapter.INSTANCE);
            }
            if (result.getConnectionPool() == null) {
                result.setConnectionPool(ConnectionPool.getDefault());
            }
            if (result.getProtocols() == null) {
                result.setProtocols(DEFAULT_PROTOCOLS);
            }
            if (result.getConnectionSpecs() == null) {
                result.setConnectionSpecs(DEFAULT_CONNECTION_SPECS);
            }
//			if (result.getDns() == null) {
//				result.setDns(Dns.SYSTEM);
//			}
        }
        return result;
    }

    private static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableList(
            Protocol.HTTP_2, Protocol.SPDY_3, Protocol.HTTP_1_1);
    private static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS = Util
            .immutableList(ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
    private static SSLSocketFactory defaultSslSocketFactory;

    private synchronized static SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSslSocketFactory == null) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                defaultSslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new AssertionError(); // The system has no TLS. Just give
                // up.
            }
        }
        return defaultSslSocketFactory;
    }

    private static Response getResponse(OkHttpClient client, Request request)
            throws IOException {
        // Copy body metadata to the appropriate request headers.
        RequestBody body = request.body();
        if (body != null) {
            Request.Builder requestBuilder = request.newBuilder();

            MediaType contentType = body.contentType();
            if (contentType != null) {
                requestBuilder.header("Content-Type", contentType.toString());
            }

            long contentLength = body.contentLength();
            if (contentLength != -1) {
                requestBuilder.header("Content-Length",
                        Long.toString(contentLength));
                requestBuilder.removeHeader("Transfer-Encoding");
            } else {
                requestBuilder.header("Transfer-Encoding", "chunked");
                requestBuilder.removeHeader("Content-Length");
            }

            request = requestBuilder.build();
        }

        copyWithDefaults(client);

        // Create the initial HTTP engine. Retries and redirects need new engine
        // for each attempt.
        HttpEngine engine = new HttpEngine(client, request, false, false,
                false, null, null, null, null);

        int followUpCount = 0;
        while (true) {
            try {
                engine.sendRequest();
                engine.readResponse();
            } catch (RequestException e) {
                // The attempt to interpret the request failed. Give up.
                throw e.getCause();
            } catch (RouteException e) {
                // The attempt to connect via a route failed. The request will
                // not have been sent.
                HttpEngine retryEngine = engine.recover(e);
                if (retryEngine != null) {
                    engine = retryEngine;
                    continue;
                }
                // Give up; recovery is not possible.
                throw e.getLastConnectException();
            } catch (IOException e) {
                // An attempt to communicate with a server failed. The request
                // may have been sent.
                HttpEngine retryEngine = engine.recover(e, null);
                if (retryEngine != null) {
                    engine = retryEngine;
                    continue;
                }

                // Give up; recovery is not possible.
                throw e;
            }

            Response response = engine.getResponse();
            Request followUp = engine.followUpRequest();

            if (followUp == null) {
                return response;
            }

            if (++followUpCount > MAX_FOLLOW_UPS) {
                throw new ProtocolException("Too many follow-up requests: "
                        + followUpCount);
            }

            if (!engine.sameConnection(followUp.httpUrl())) {
                engine.releaseConnection();
            }

            Connection connection = engine.close();
            request = followUp;
            engine = new HttpEngine(client, request, false, false, false,
                    connection, null, null, response);
        }
    }

    private static Request networkRequest(OkHttpClient client, Request request) throws IOException {
        Request.Builder result = request.newBuilder();

        if (request.header("Host") == null) {
            result.header("Host", Util.hostHeader(request.httpUrl()));
        }

        if (request.header("Connection") == null) {
            result.header("Connection", "Keep-Alive");
        }

        if (request.header("Accept-Encoding") == null) {
            result.header("Accept-Encoding", "gzip");
        }

        CookieHandler cookieHandler = client.getCookieHandler();
        if (cookieHandler != null) {
            // Capture the request headers added so far so that they can be offered to the CookieHandler.
            // This is mostly to stay close to the RI; it is unlikely any of the headers above would
            // affect cookie choice besides "Host".
            Map<String, List<String>> headers = OkHeaders.toMultimap(result.build().headers(), null);

            Map<String, List<String>> cookies = cookieHandler.get(request.uri(), headers);

            // Add any new cookies to the request.
            OkHeaders.addCookies(result, cookies);
        }

        if (request.header("User-Agent") == null) {
            result.header("User-Agent", Version.userAgent());
        }

        return result.build();
    }
}
