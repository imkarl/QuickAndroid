package cn.jeesoft.qa.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import cn.jeesoft.qa.utils.lang.QAConvert;

/**
 * APP辅助工具类
 * @version v0.1.0 king 2014-12-22 获取已安装APP信息
 */
public class QAAppUtils {
    
    /**
     * APP信息
     */
    public static class QAAppInfo {
        public final String appName;
        public final int versionCode;
        public final String versionName;
        public final String packageName;
        
        public final String sharedUserId;
        public final int sharedUserLabel;
        /** 第一次安装时间 */
        public final long firstInstallTime;
        /** 最后更新时间 */
        public final long lastUpdateTime;
        public final ApplicationInfo applicationInfo;
        
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        private QAAppInfo(Context context, PackageInfo packageInfo) {
            super();
            this.appName = QAConvert.toString(packageInfo.applicationInfo.loadLabel(context.getPackageManager()));
            this.packageName = packageInfo.packageName;
            this.versionName = packageInfo.versionName;
            this.versionCode = packageInfo.versionCode;
            this.sharedUserId = packageInfo.sharedUserId;
            this.sharedUserLabel = packageInfo.sharedUserLabel;
            
            long firstInstallTime = -1;
            long lastUpdateTime = -1;
            try {
                firstInstallTime = packageInfo.firstInstallTime;
                lastUpdateTime = packageInfo.lastUpdateTime;
            } catch (Exception e) {
                firstInstallTime = -1;
                lastUpdateTime = -1;
            }
            this.firstInstallTime = firstInstallTime;
            this.lastUpdateTime = lastUpdateTime;
            this.applicationInfo = packageInfo.applicationInfo;
        }

        public Drawable loadIcon(Context context) {
            return this.applicationInfo.loadIcon(context.getPackageManager());
        }
        
        public boolean isSystemApp() {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                return false;
            } else {
                // 系统应用
                return true;
            }
        }
        
    }
    
    
    /**
     * 获取已安装的APP列表
     * @param context
     * @return
     */
    public static List<QAAppInfo> getInstalledApps(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (packages == null || packages.isEmpty()) {
            return null;
        }

        List<QAAppInfo> appInfoList = new ArrayList<QAAppInfo>(); // 用来存储获取的应用信息数据
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            appInfoList.add(new QAAppInfo(context, packageInfo));
        }
        return appInfoList;
    }

    /**
     * 获取APP安装包信息
     * @return
     */
    public static QAAppInfo getCurrentAppInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (packageInfo == null) {
            return new QAAppInfo(context, packageInfo);
        }
        return new QAAppInfo(context, packageInfo);
    }
    
    
    
    
    
    /**
     * 分享的APP信息
     */
    public static class QAShareAppInfo {
        public final String appName;
        public final String packageName;
        public final ApplicationInfo applicationInfo;
        
        
        public final ComponentInfo componentInfo;
        public final IntentFilter filter;
        public final int priority;
        public final int preferredOrder;
        public final int match;

        public final int specificIndex;
        public final boolean isDefault;
        public final int labelRes;
        public final CharSequence nonLocalizedLabel;
        public final int icon;
        public final String resolvePackageName;
        
        
        @TargetApi(Build.VERSION_CODES.KITKAT)
        private QAShareAppInfo(Context context, ResolveInfo resolveInfo) {
//            super(context, packageInfo);
            
            ComponentInfo componentInfo = null;
            try {
                if (resolveInfo.activityInfo != null) {
                    componentInfo = resolveInfo.activityInfo;
                } else if (resolveInfo.serviceInfo != null) {
                    componentInfo = resolveInfo.serviceInfo;
                } else if (resolveInfo.providerInfo != null) {
                    componentInfo = resolveInfo.providerInfo;
                }
            } catch (Exception e) { }
            this.componentInfo = componentInfo;
            
            this.filter = resolveInfo.filter;
            this.priority = resolveInfo.priority;
            this.preferredOrder = resolveInfo.preferredOrder;
            this.match = resolveInfo.match;

            this.specificIndex = resolveInfo.specificIndex;
            this.isDefault = resolveInfo.isDefault;
            this.labelRes = resolveInfo.labelRes;
            this.nonLocalizedLabel = resolveInfo.nonLocalizedLabel;
            this.icon = resolveInfo.icon;
            this.resolvePackageName = resolveInfo.resolvePackageName;
            
            
            if (this.componentInfo != null) {
                this.applicationInfo = componentInfo.applicationInfo;
            } else {
                this.applicationInfo = null;
            }
            this.appName = QAConvert.toString(applicationInfo.loadLabel(context.getPackageManager()));
            this.packageName = applicationInfo.packageName;
        }
        
        public Drawable loadIcon(Context context, ResolveInfo resolve) {
            return resolve.loadIcon(context.getPackageManager());
        }
        
        public boolean isSystemApp() {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                return false;
            } else {
                // 系统应用
                return true;
            }
        }
        
    }
    
    
    /** 
     * 查询手机内所有支持分享的应用
     * @param contex
     * @return
     */
    public static List<QAShareAppInfo> getShareApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        List<ResolveInfo> resolveList = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        
        if (resolveList == null || resolveList.isEmpty()) {
            return null;
        }
        
        List<QAShareAppInfo> shareAppInfos = new ArrayList<QAShareAppInfo>();
        for (int i = 0; i < resolveList.size(); i++) {
            ResolveInfo resolve = resolveList.get(i);
            QAShareAppInfo shareAppInfo = new QAShareAppInfo(context, resolve);
            shareAppInfos.add(shareAppInfo);
        }
        return shareAppInfos;
    }
    
    
    
    
    /**
     * 获取系统服务
     * @param context
     * @param name
     * @return
     * 
     * @see #WINDOW_SERVICE
     * @see android.view.WindowManager
     * @see #LAYOUT_INFLATER_SERVICE
     * @see android.view.LayoutInflater
     * @see #ACTIVITY_SERVICE
     * @see android.app.ActivityManager
     * @see #POWER_SERVICE
     * @see android.os.PowerManager
     * @see #ALARM_SERVICE
     * @see android.app.AlarmManager
     * @see #NOTIFICATION_SERVICE
     * @see android.app.NotificationManager
     * @see #KEYGUARD_SERVICE
     * @see android.app.KeyguardManager
     * @see #LOCATION_SERVICE
     * @see android.location.LocationManager
     * @see #SEARCH_SERVICE
     * @see android.app.SearchManager
     * @see #SENSOR_SERVICE
     * @see android.hardware.SensorManager
     * @see #STORAGE_SERVICE
     * @see android.os.storage.StorageManager
     * @see #VIBRATOR_SERVICE
     * @see android.os.Vibrator
     * @see #CONNECTIVITY_SERVICE
     * @see android.net.ConnectivityManager
     * @see #WIFI_SERVICE
     * @see android.net.wifi.WifiManager
     * @see #AUDIO_SERVICE
     * @see android.media.AudioManager
     * @see #MEDIA_ROUTER_SERVICE
     * @see android.media.MediaRouter
     * @see #TELEPHONY_SERVICE
     * @see android.telephony.TelephonyManager
     * @see #INPUT_METHOD_SERVICE
     * @see android.view.inputmethod.InputMethodManager
     * @see #UI_MODE_SERVICE
     * @see android.app.UiModeManager
     * @see #DOWNLOAD_SERVICE
     * @see android.app.DownloadManager
     * @see #BATTERY_SERVICE
     * @see android.os.BatteryManager
     * @see #JOB_SCHEDULER_SERVICE
     * @see android.app.job.JobScheduler
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSystemService(Context context, String name) {
        try {
            return (T) context.getSystemService(name);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
}
