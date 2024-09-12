package com.tony.permissutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionUtils {
    /**
     * 没有的权限存储起来  进行请求
     *
     *  下来的请求方式就是将未授予的权限返回去
     *  如果list的大小为0 ，那就执行权限运行内的操作，
     *  如果大于0，就在result方法中检测权限是否完善。
     *
     *
     *  还有一种做法是，可以正常开启，但是在执行权限操作之前检验，有就正常执行，否则就给弹窗
     *
     *  使用案例：
     *
     *   private static final String[] REQUIRED_PERMISSIONS = {
     *             Manifest.permission.READ_MEDIA_AUDIO,
     *             Manifest.permission.READ_MEDIA_IMAGES,
     *             Manifest.permission.READ_MEDIA_VIDEO,
     *             Manifest.permission.POST_NOTIFICATIONS,
     *             Manifest.permission.READ_EXTERNAL_STORAGE,
     *             Manifest.permission.WRITE_EXTERNAL_STORAGE
     *     };
     *
     *     1.成功过就走这个方法
     *
     *    ArrayList<String> arrayList = PermissionUtils.requestPermission(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
 *         if (arrayList.size()<=0) {
 *             enterMainActivity();
 *         }
     *
     *     2.失败就走下面
     *      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     *          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     *          boolean flag = true;
     *         if (requestCode == REQUEST_CODE_PERMISSIONS){
     *             for (int grantResult : grantResults) {
     *                 if (grantResult != PackageManager.PERMISSION_GRANTED) {
     *                     flag = false;
     *                 }
     *             }
     *         }
     *         if (flag){
     *             enterMainActivity();
     *         }else {
     *             finish();
     *         }
     *     }
     * }
     *
     *
     */
    public static ArrayList<String> requestPermission(Activity activity, String[] permissions, int code) {
        ArrayList<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (hasPermission(activity, permission)) {
                permissionsNeeded.add(permission);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toArray(new String[0]),
                    code);
        }
        return permissionsNeeded;
    }

    //是否含义这个权限
    public static boolean hasPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (hasPermission(activity, permission)) {
                return false;
            }
        }
        return true;
    }
}