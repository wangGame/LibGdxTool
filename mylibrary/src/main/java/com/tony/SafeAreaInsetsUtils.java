package com.tony;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.kw.gdx.constant.Configuration;

import java.util.List;

/**
 * call focurs method
 */
public class SafeAreaInsetsUtils {
    public static void getSafeAreaInsets(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (layoutParams.layoutInDisplayCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT) {
                // 默认模式，可能有刘海屏或挖孔
            }
            View decorView = window.getDecorView();
            if (decorView != null) {
                WindowInsets windowInsets = decorView.getRootWindowInsets();
                if (windowInsets != null) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    if (displayCutout != null) {
                        // 存在刘海屏或挖孔
                        List<Rect> boundingRects = displayCutout.getBoundingRects();
                        if (!boundingRects.isEmpty()) {
                            Rect rect = boundingRects.get(0);
                            // 通过刘海屏或挖孔的位置和大小进行UI适配
                            System.out.println("-------------------------");
                            Configuration.left = rect.left;
                            Configuration.right = rect.right;
                            Configuration.top = rect.top;
                            Configuration.bottom = rect.bottom;
                        }
                    }
                }
            }
        }
    }
}
