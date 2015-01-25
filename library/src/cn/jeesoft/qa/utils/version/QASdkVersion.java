package cn.jeesoft.qa.utils.version;

import cn.jeesoft.qa.QACore.QAPrivateCheck;

/**
 * Android SDK 版本鉴别
 * @version v0.1.0 king 2014-11-06 版本号常量，判断是否支持某个版本API
 */
public class QASdkVersion extends QASdkVersionCodes {

    protected QASdkVersion(QAPrivateCheck check) {
    	QAPrivateCheck.check(check);
    }
	
	/**
	 * 是否支持指定版本
	 * @param version
	 * @return
	 * @see QASdkVersion.QASdkVersionCodes
	 */
	public static boolean isSupport(int version) {
		if (android.os.Build.VERSION.SDK_INT >= version) {
			return true;
		}
		return false;
	}

}
