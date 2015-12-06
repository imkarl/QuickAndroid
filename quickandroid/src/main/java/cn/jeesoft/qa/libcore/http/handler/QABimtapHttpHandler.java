package cn.jeesoft.qa.libcore.http.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAIOException;

public class QABimtapHttpHandler implements QAHttpHandler<Bitmap> {

    @Override
    public Bitmap handlerResponse(Response response) throws Exception {
        Exception exception = null;
        Bitmap bitmap = null;

        InputStream stream = null;
        try {
            stream = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            exception = e;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        if (exception == null) {
            return bitmap;
        }
        throw new QAIOException(QAException.CODE_IO_FILE, exception);
    }

}
