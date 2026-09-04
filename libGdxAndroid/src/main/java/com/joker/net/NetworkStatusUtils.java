package com.joker.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public final class NetworkStatusUtils {
    private NetworkStatusUtils() {
    }

    static NetworkInfo getCurrentNetworkInfo(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager == null) {
            return null;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null) {
            return activeNetworkInfo;
        }
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo.getType() != 7) {
                if (activeNetworkInfo == null && networkInfo.getType() == 0) {
                    activeNetworkInfo = networkInfo;
                }
                if (networkInfo.isAvailable() && (activeNetworkInfo == null || !activeNetworkInfo.isAvailable())) {
                    activeNetworkInfo = networkInfo;
                }
                if (networkInfo.isConnected() && (activeNetworkInfo == null || !activeNetworkInfo.isConnected())) {
                    return networkInfo;
                }
            }
        }
        return activeNetworkInfo;
    }

    static int getConnectedOrConnectingNetworkType(Context context) {
        return getConnectedOrConnectingNetworkType(getCurrentNetworkInfo(context));
    }

    static int getConnectedOrConnectingNetworkType(NetworkInfo networkInfo) {
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return 0;
        }
        return getNetworkType(networkInfo);
    }

    static int getNetworkType(Context context) {
        return getNetworkType(getCurrentNetworkInfo(context));
    }

    static int getNetworkType(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return 0;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return 2;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return 1;
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return 3;
        }
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ? 4 : 0;
    }

    static int getNetworkType(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return 0;
        }
        int type = networkInfo.getType();
        if (type == 0) {
            return 1;
        }
        if (type == 1) {
            return 2;
        }
        if (type != 9) {
            return type != 17 ? 0 : 4;
        }
        return 3;
    }

    static boolean isConnected(NetworkCapabilities networkCapabilities) {
        return getNetworkType(networkCapabilities) != 0 && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isConnected();
    }

    static boolean isConnectedOrConnecting(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    static boolean isInternetReachable(Context context) {
        return isConnected(getCurrentNetworkInfo(context));
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        if (context == null) {
            return null;
        }
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}