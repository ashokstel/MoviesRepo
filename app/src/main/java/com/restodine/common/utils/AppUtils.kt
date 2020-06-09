package com.restodine.common.utils

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.restodine.App
import java.io.File
import java.util.*



object AppUtils {

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("HardwareIds")
            /**
             * Android Id
             * (Note: All our existing services are using this Android ID)
             */
    fun getAndroidId(): String {
        return Settings.Secure.getString(App.app.contentResolver, Settings.Secure.ANDROID_ID)
    }


    fun getAppVersion(context: Context): Int {
        try {
            val packageInfo = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            // should never happen
            throw RuntimeException("Could not get package name: $e")
        }

    }


    /**
     * To check App update
     */
   /* fun checkAppUpdateAvailable(context: BaseActivity) {
        val versionCode = BuildConfig.VERSION_CODE
        val rcMinVersion = RemoteConfigUtils.getAppMinVersion()
        val rcCurrentVersion = RemoteConfigUtils.getAppCurrentVersion()
        if (versionCode < rcCurrentVersion) {
            if (versionCode < rcMinVersion) {
                showAppUpdateDialog(true, context)
            } else {
                showAppUpdateDialog(false, context)
            }
        }
    }*/

    @SuppressLint("MissingPermission")
    fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo

        return info?.isConnected ?: false
    }

    fun goToSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @SuppressWarnings
    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }

    fun getPackageManager(): PackageManager = App.app.packageManager

    fun getPackageName(): String = App.app.packageName

    fun checkIfSdCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    }

    fun megabytesAvailableOnSdcard(): Float {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
        } else {
            @Suppress("DEPRECATION")
            bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
        }
        return bytesAvailable / (1024f * 1024f)
    }

    fun megabytesAvailableOnInternal(): Float {
        val stat = StatFs(Environment.getDataDirectory().path)
        val bytesAvailable: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
        } else {
            @Suppress("DEPRECATION")
            bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
        }
        return bytesAvailable / (1024f * 1024f)
    }

    fun getOsVersionName(): String = Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name

    fun getAppVersionName(): String {
        val packageManager = App.app.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(App.app.packageName, 0)
            return packageInfo.versionName
        } catch (ex: PackageManager.NameNotFoundException) {
        } catch (e: Exception) {
        }

        return ""
    }



    /**
     * To get application version code
     */
    fun getAppVersionCode(): Int {
        val context = App.app.applicationContext
        val packageManager = App.app.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (ex: PackageManager.NameNotFoundException) {
        } catch (e: Exception) {
        }

        return 0
    }


    // fun getLastAppUpdateTime() = PrefHelper.getUserPref().getLong(Pref.LAST_APP_UPDATE_TIME, 0L)

    fun getNetworkClass(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null || !info.isConnected)
            return "NOT_CONNECTED" //not connected
        if (info.type == ConnectivityManager.TYPE_WIFI)
            return "WIFI"
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            return when (info.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
                -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B //api<9 : replace by 14
                    , TelephonyManager.NETWORK_TYPE_EHRPD  //api<11 : replace by 12
                    , TelephonyManager.NETWORK_TYPE_HSPAP  //api<13 : replace by 15
                -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE    //api<11 : replace by 13
                -> "4G"
                else -> "OTHERS"
            }
        }
        return "OTHERS"
    }

    ///For Intrusive notification
    fun isScreenLocked(): Boolean {
        val keyguardManager = App.app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardLocked
    }

}