package com.adisayoga.webview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Static helper utility class.
 */
public final class Utils {
    private static final String TAG = "Utils";

    public static final String SERVER_HOST = "http://google.com";
    public static final String SERVER_HOST_LOCALHOST = "http://adi-pc:82";
    public static final String SERVER_HOST_EMULATOR = "http://10.0.2.2";

    private Utils() {
    }

    /**
     * Get server host menyesuaikan dengan debug/release mode dan real device/emulator.
     */
    public static String getHost() {
        //if (!BuildConfig.DEBUG) {
        //    if (isLoggable()) Log.d(TAG, "Release mode, get server host=" + SERVER_HOST);
            return SERVER_HOST;
        //} else if (isEmulator()) {
        //    if (isLoggable()) Log.d(TAG, "Debug mode (emulator), get server host=" + SERVER_HOST_EMULATOR);
        //    return SERVER_HOST_EMULATOR;
        //} else {
        //    if (isLoggable()) Log.d(TAG, "Debug mode (device), get server host=" + SERVER_HOST_LOCALHOST);
        //    return SERVER_HOST_LOCALHOST;
        //}
    }

    /**
     * Get versi aplikasi dari {@link PackageManager}.
     *
     * @param context Context aplikasi.
     * @return Version code aplikasi dari {@link PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Seharusnya tidak pernah terjadi
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Method untuk mengecek apakah aplikasi berjalan pada emulator/tidak.
     *
     * @return Return true jika pada emulator, atau false jika sebaliknya.
     */
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }

    /**
     * Method untuk mengecek apakah log enable/disable.
     *
     * @return Return true jika log enable, atau false jika log disable.
     */
    public static boolean isLoggable() {
        return BuildConfig.DEBUG;
    }

    /**
     * Tampilkan log error jika log enable.
     *
     * @param tag Tag log.
     * @param e   Error object.
     */
    public static void logError(String tag, Exception e) {
        logError(tag, e, null);
    }

    /**
     * Tampilkan log error jika log enable.
     *
     * @param tag     Tag log.
     * @param e       Error object.
     * @param message Pesan tambahan
     */
    public static void logError(String tag, Exception e, String message) {
        if (!isLoggable()) return;
        if (message != null) message = "Error";
        Log.e(tag, message + ": " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Get ukuran screen layar perangkat.
     */
    public static Point getDisplaySize(Activity activity) {
        final Display display = activity.getWindowManager().getDefaultDisplay();
        final Point displaySize = new Point();
        display.getSize(displaySize);
        return displaySize;
    }

    /**
     * Copy file menggunakan file stream.
     */
    public static boolean copyFile(File src, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            final FileChannel inChannel = in.getChannel();
            final FileChannel outChannel = out.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            return true;
        } catch (IOException e) {
            logError(TAG, e, "Error copying file from " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
            return false;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
            try {
                if (out != null) out.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * <p>Cari index dari objek yang diberikan pada array.</p>
     * <p>Method ini return {@code -1} untuk {@code null} input array.</p>
     */
    public static int indexOf(Object[] array, Object objectToFind) {
        if (array == null) return -1;

        if (objectToFind == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) return i;
            }
        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
            for (int i = 0; i < array.length; i++) {
                if (objectToFind.equals(array[i])) return i;
            }
        }
        return -1;
    }

    /**
     * <p>Cari index dari integer yang diberikan pada array.</p>
     * <p>Method ini return {@code -1} untuk {@code null} input array.</p>
     */
    public static int indexOf(int[] array, int toFind) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == toFind) return i;
        }
        return -1;
    }
}
