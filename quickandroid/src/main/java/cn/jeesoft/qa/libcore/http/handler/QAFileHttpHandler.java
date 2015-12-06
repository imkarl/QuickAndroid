package cn.jeesoft.qa.libcore.http.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.jeesoft.qa.error.QAException;
import cn.jeesoft.qa.error.QAIOException;
import cn.jeesoft.qa.error.QANoFoundException;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class QAFileHttpHandler implements QAHttpHandler<File> {
	private File targetFile;
	
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}
	
	@Override
	public File handlerResponse(Response response) throws Exception {
		if (targetFile == null) {
			throw new QANoFoundException(QAException.CODE_IO_FILE, "'targetFile'不能为空");
		}
		
		File parentFile = targetFile.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
		
		Exception exception = null;

        InputStream stream = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            ResponseBody responseBody = response.body();
            stream = responseBody.byteStream();
            fos = new FileOutputStream(targetFile);
            while ((len = stream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
        	exception = e;
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }

        if (exception == null) {
            return targetFile;
        }
		throw new QAIOException(QAException.CODE_IO_FILE, exception);
	}
	
}
