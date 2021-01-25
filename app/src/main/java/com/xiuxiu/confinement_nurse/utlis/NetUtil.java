package com.xiuxiu.confinement_nurse.utlis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * Created by $xuzhili on 2017/12/29.
 * dangbei network
 */

public final class NetUtil {

    /**
     * Unknown network class
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    /**
     * wifi net work
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * "2G" networks
     */
    public static final int NETWORK_CLASS_2_G = 2;

    /**
     * "3G" networks
     */
    public static final int NETWORK_CLASS_3_G = 3;

    /**
     * "4G" networks
     */
    public static final int NETWORK_CLASS_4_G = 4;


    public static final int NETWORK_SIMPLE_MOBILE = 0x3344;
    public static final int NETWORK_SIMPLE_WIFI = 0x3345;
    public static final int NETWORK_SIMPLE_ETHERNET = 0x3345;
    public static final int NETWORK_SIMPLE_NONE = 0x3346;


    private static final String TAG = "NetUtil";

    public static final String NET_UNAVAILABLE = "unavailable";
    private static final String UNKNOWN = Build.UNKNOWN;
    private static final String FILE_ADDRESS_WIFI_MAC = "/sys/class/net/wlan0/address";
    private static final String FILE_ADDRESS_ETHERNET = "/sys/class/net/eth0/address";


    /**
     * APP启动时才能进行监听回调
     * @return
     */
    public static boolean isNetworkAvailable() {
        return NetworkUtils.isConnected();
    }


    /**
     * 网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager) {
            return NET_UNAVAILABLE;
        }
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NET_UNAVAILABLE;
        }
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "wifi";
                }
            }
        }
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "mobile";
                }
            }
        }
        networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "ethernet";
                }
            }
        }
        return NET_UNAVAILABLE;
    }


    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        return !NET_UNAVAILABLE.equals(getNetworkState(context));
    }


    /**
     * wifi mac
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        String mac;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || !isNetAvailable(context)) {
            mac = getMacByFile(FILE_ADDRESS_WIFI_MAC);
        } else {
            mac = getWifiMacBySystemService(context);
        }
        return mac;
    }


    /**
     * ethernet mac
     *
     * @param context
     * @return
     */
    public static String getEthernetMac(Context context) {
        String netState = getNetworkState(context);
        String mac;
        switch (netState) {
            case "ethernet":
                mac = getEthernetMacBySystem();
                break;
            default:
                mac = getMacByFile(FILE_ADDRESS_ETHERNET);
                break;
        }
        return mac;
    }


    /**
     * 获取系统有线网络mac
     *
     * @return
     */
    private static String getEthernetMacBySystem() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> netInterface = NetworkInterface.getNetworkInterfaces();
            while (netInterface.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterface.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    ip = enIp.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        break;
                    } else {
                        ip = null;
                    }
                }
                if (ip != null) {
                    break;
                }
            }
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(ip);
            if (byInetAddress!=null) {
                byte[] b = byInetAddress.getHardwareAddress();
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < b.length; i++) {
                    if (i != 0) {
                        buffer.append(':');
                    }
                    String str = Integer.toHexString(b[i] & 0xFF);
                    buffer.append(str.length() == 1 ? 0 + str : str);
                }
                return buffer.toString().toUpperCase();
            }
        } catch (SocketException e) {
        }
        return UNKNOWN;
    }


    /**
     * wifimac地址
     *
     * @param context
     * @return
     */
    private static String getWifiMacBySystemService(Context context) {
        if (context == null) {
            return UNKNOWN;
        }
        try {
            WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiMan != null) {
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                if (wifiInf != null) {
                    return wifiInf.getMacAddress() == null ? UNKNOWN : wifiInf.getMacAddress();
                }
            }
        } catch (Exception e) {
        }
        return UNKNOWN;
    }


    /**
     * 读文件的方式获取 mac地址
     *
     * @return
     */
    private static String getMacByFile(String address) {
        File fl = new File(address);
        if (!fl.exists()) {
            return UNKNOWN;
        }
        try {
            FileInputStream fin = new FileInputStream(fl);
            Writer crunchifyWriter = new StringWriter();
            char[] crunchifyBuffer = new char[1024];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } catch (Exception e) {
                LogUtils.e(TAG, e.toString());
            } finally {
                fin.close();
            }
            String str = crunchifyWriter.toString();
            return str.trim();
        } catch (Exception e) {
        }
        return UNKNOWN;
    }

    /**
     * 是否存在有效的移动连接
     *
     * @return boolean
     */
    public static boolean isMobileConnected() {
        NetworkInfo net = getConnectivityManager().getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

    /**
     * 是否存在有效的WIFI连接
     */
    public static boolean isWifiConnected() {
        NetworkInfo net = getConnectivityManager().getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }


    /**
     * 获取ConnectivityManager
     */
    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public static int getNetWorkSimpleStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();
        }
        if (ni != null && ni.isConnectedOrConnecting()) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_SIMPLE_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return NETWORK_SIMPLE_ETHERNET;
            } else {
                // 移动网络
                return NETWORK_SIMPLE_MOBILE;
            }
        } else {
            // 无网络
            return NETWORK_SIMPLE_NONE;
        }
    }


    /**
     * 返回状态：当前的网络链接状态 0：其他 1：WIFI 2：2G 3：3G 4：4G
     *
     * @return 没有网络，2G，3G，4G，WIFI
     */
    public static int getNetWorkStatus(Context context) {
        int netWorkType = NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }

    /**
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     *
     * @return 2G、3G、4G、未知四种状态
     */
    public static int getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;

            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * make true current connect service is wifi
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = null;
        if (connectivityManager != null) {
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


}