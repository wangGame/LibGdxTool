package com.tony;

import android.os.Build;

public class DevieceInfo {
    //设备名
    private String model = Build.MODEL;
    //设备厂商
    private String brand = Build.BRAND;
    //系统sdk-code
    private String version = String.valueOf(Build.VERSION.SDK_INT);
    //系统版本号
    private String release = Build.VERSION.RELEASE;
    //
    private String cpuAbi = Build.CPU_ABI;
}

