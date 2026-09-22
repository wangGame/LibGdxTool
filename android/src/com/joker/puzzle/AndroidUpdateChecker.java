package com.joker.puzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Checks GitHub Releases for a newer APK and hands installation to Android. */
final class AndroidUpdateChecker {
    private static final String LATEST_URL =
            "https://api.github.com/repos/wangGame/LibGdxTool/releases/latest";
    private static final int REQUEST_UNKNOWN_SOURCES = 7001;
    // This repository currently tags releases as "release2.0.3".
    private static final Pattern VERSION = Pattern.compile("(?:release)?[vV]?(\\d+)\\.(\\d+)(?:\\.(\\d+))?",
            Pattern.CASE_INSENSITIVE);

    private final Activity activity;
    private volatile boolean cancelled;
    private AlertDialog progressDialog;
    private ProgressBar progressBar;
    private File downloadedApk;

    AndroidUpdateChecker(Activity activity) {
        this.activity = activity;
    }

    void checkForUpdate() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                connection = open(LATEST_URL);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return;
                JSONObject release = new JSONObject(readText(connection.getInputStream()));
                String tag = release.optString("tag_name", "");
                String apkUrl = firstApkUrl(release.optJSONArray("assets"));
                if (apkUrl == null || !isNewer(tag, installedVersion())) return;
                activity.runOnUiThread(() -> showUpdateDialog(tag, apkUrl));
            } catch (Exception ignored) {
                // Updates are optional; an offline or rate-limited check must not block launch.
            } finally {
                if (connection != null) connection.disconnect();
            }
        }, "apk-update-check").start();
    }

    private void showUpdateDialog(String tag, String apkUrl) {
        if (activity.isFinishing() || (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed())) return;
        new AlertDialog.Builder(activity)
                .setTitle("发现新版本 " + tag)
                .setMessage("是否下载并安装最新 APK？")
                .setNegativeButton("稍后", null)
                .setPositiveButton("更新", (dialog, which) -> download(apkUrl))
                .show();
    }

    private void download(String apkUrl) {
        cancelled = false;
        progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressDialog = new AlertDialog.Builder(activity)
                .setTitle("正在下载更新")
                .setView(progressBar)
                .setNegativeButton("取消", (dialog, which) -> cancelDownload())
                .setOnCancelListener(dialog -> cancelDownload())
                .show();

        new Thread(() -> {
            HttpURLConnection connection = null;
            File apk = null;
            try {
                File directory = new File(activity.getCacheDir(), "update");
                if (!directory.exists() && !directory.mkdirs()) throw new IllegalStateException("cache unavailable");
                apk = new File(directory, "LibGdxTool-update.apk");
                downloadedApk = null;
                if (apk.exists() && !apk.delete()) throw new IllegalStateException("old APK cannot be replaced");

                connection = open(apkUrl);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) throw new IllegalStateException("HTTP error");
                long expected = connection.getContentLength();
                long received = 0;
                try (InputStream input = connection.getInputStream();
                     FileOutputStream output = new FileOutputStream(apk)) {
                    byte[] buffer = new byte[32 * 1024];
                    int count;
                    while ((count = input.read(buffer)) != -1) {
                        if (cancelled) break;
                        output.write(buffer, 0, count);
                        received += count;
                        if (expected > 0) {
                            int percent = (int) Math.min(100, received * 100 / expected);
                            activity.runOnUiThread(() -> {
                                if (progressBar != null) progressBar.setProgress(percent);
                            });
                        }
                    }
                }

                if (cancelled || received == 0 || (expected > 0 && received != expected))
                    throw new IllegalStateException("incomplete download");
                downloadedApk = apk;
                File readyApk = apk;
                activity.runOnUiThread(() -> {
                    dismissProgress();
                    promptInstall(readyApk);
                });
            } catch (Exception ignored) {
                if (apk != null) apk.delete();
                if (!cancelled) activity.runOnUiThread(() -> {
                    dismissProgress();
                    new AlertDialog.Builder(activity).setMessage("更新下载失败，请稍后重试。")
                            .setPositiveButton("确定", null).show();
                });
            } finally {
                if (connection != null) connection.disconnect();
            }
        }, "apk-update-download").start();
    }

    void cancelDownload() {
        cancelled = true;
        dismissProgress();
    }

    private void dismissProgress() {
        if (progressDialog != null) progressDialog.dismiss();
        progressDialog = null;
        progressBar = null;
    }

    void onSettingsResult(int requestCode) {
        if (requestCode == REQUEST_UNKNOWN_SOURCES && canInstall() && downloadedApk != null
                && downloadedApk.isFile()) promptInstall(downloadedApk);
    }

    private void promptInstall(File apk) {
        if (activity.isFinishing() || (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed())) return;
        try {
            if (!canInstall()) {
                Intent settings = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(settings, REQUEST_UNKNOWN_SOURCES);
                return;
            }
            Uri uri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".fileprovider", apk);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(install);
        } catch (Exception ignored) {
            new AlertDialog.Builder(activity).setMessage("无法打开系统安装界面。")
                    .setPositiveButton("确定", null).show();
        }
    }

    private boolean canInstall() {
        return Build.VERSION.SDK_INT < 26 || activity.getPackageManager().canRequestPackageInstalls();
    }

    private String installedVersion() {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return info.versionName;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static boolean isNewer(String tag, String current) {
        Matcher candidate = VERSION.matcher(tag == null ? "" : tag);
        Matcher installed = VERSION.matcher(current == null ? "" : current);
        if (!candidate.matches() || !installed.matches()) return false;
        for (int i = 1; i <= 3; i++) {
            int a = Integer.parseInt(candidate.group(i) == null ? "0" : candidate.group(i));
            int b = Integer.parseInt(installed.group(i) == null ? "0" : installed.group(i));
            if (a != b) return a > b;
        }
        return false;
    }

    private static String firstApkUrl(JSONArray assets) {
        if (assets == null) return null;
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.optJSONObject(i);
            if (asset == null) continue;
            String name = asset.optString("name", "");
            String url = asset.optString("browser_download_url", "");
            if (name.toLowerCase(java.util.Locale.ROOT).endsWith(".apk")
                    && url.startsWith("https://")) return url;
        }
        return null;
    }

    private static HttpURLConnection open(String address) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty("User-Agent", "LibGdxTool-Android");
        connection.setRequestProperty("Accept", "application/vnd.github+json");
        return connection;
    }

    private static String readText(InputStream input) throws Exception {
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = stream.read(buffer)) != -1) output.write(buffer, 0, count);
            return output.toString("UTF-8");
        }
    }
}
