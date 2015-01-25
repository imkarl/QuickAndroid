package cn.jeesoft.qa;

import cn.jeesoft.qa.QACore.QAPrivateCheck;
import cn.jeesoft.qa.json.QAJsonUtils;
import cn.jeesoft.qa.manager.QAFileManager;
import cn.jeesoft.qa.ui.uikit.QAToast;
import cn.jeesoft.qa.utils.lang.QAClassUtils;
import cn.jeesoft.qa.utils.lang.QAConvert;
import cn.jeesoft.qa.utils.lang.QAStringUtils;
import cn.jeesoft.qa.utils.log.QALog;
import cn.jeesoft.qa.utils.stack.QAStackTraceInfo;
import cn.jeesoft.qa.utils.type.QATypeToken;
import cn.jeesoft.qa.utils.version.QASdkVersion;

/**
 * 统一管理对外提供的API
 * @author v0.1.0 king 2015-1-25
 */
interface QACoreUtils {
	
	public static final class json extends QAJsonUtils {
		private json(QAPrivateCheck check) {
			super(check);
		}
	}

	public static final class file extends QAFileManager {
		private file(QAPrivateCheck check) {
			super(check);
		}
	}
	
	public static final class log extends QALog {
		private log(QAPrivateCheck check) {
			super(check);
		}
	}
	
	public static final class toast extends QAToast {
		private toast(QAPrivateCheck check) {
			super(check);
		}
	}
	
	public static final class version extends QASdkVersion {
		private version(QAPrivateCheck check) {
			super(check);
		}
	}
	
	public static final class stack extends QAStackTraceInfo {
		private stack(QAPrivateCheck check) {
			QAPrivateCheck.check(check);
		}
	}
	
	public static final class convert extends QAConvert {
		private convert(QAPrivateCheck check) {
			super(check);
		}
	}

	public static final class qclass extends QAClassUtils {
		private qclass(QAPrivateCheck check) {
			super(check);
		}
	}
	
	public static final class qstring extends QAStringUtils {
		private qstring(QAPrivateCheck check) {
			super(check);
		}
	}

	
	public static final class TypeToken<T> extends QATypeToken<T> {
		protected TypeToken() {
			super();
		}
	}
	
}
