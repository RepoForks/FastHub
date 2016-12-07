package com.fastaccess.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fastaccess.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by kosh20111 on 18 Oct 2016, 9:29 PM
 */

public class AppHelper {

    public static boolean isOnline(@NonNull Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (isM()) {
                Network networks = cm.getActiveNetwork();
                NetworkInfo netInfo = cm.getNetworkInfo(networks);
                haveConnectedWifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
                haveConnectedMobile = netInfo.getType() == ConnectivityManager.TYPE_MOBILE && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            } else {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        if (ni.isConnected())
                            haveConnectedWifi = true;
                    }
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        if (ni.isConnected())
                            haveConnectedMobile = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isApplicationInstalled(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info != null;
    }

    public static boolean isM() {return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;}

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void hideKeyboard(@NonNull View view) {
        hideKeyboard(view, view.getContext());
    }

    public static void hideKeyboard(@NonNull View view, @NonNull Context activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Nullable public static Fragment getFragmentByTag(@NonNull FragmentManager fragmentManager, @NonNull String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    @Nullable public static Fragment getVisibleFragment(@NonNull FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    Logger.e(fragment.getClass().getSimpleName(), fragment.isVisible());
                    return fragment;
                }
            }
        }
        return null;
    }

    @Nullable public static String saveBitmap(Bitmap image) {
        try {
            File file = FileHelper.generateFile("fa_image_icon");
            if (file.exists()) {
                file.delete();
            }
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 70, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString") @NonNull public static String getEmailBody() {
        return new StringBuilder()
                .append("Version Code: ").append(BuildConfig.VERSION_CODE)
                .append("\n")
                .append("Version Name: ").append(BuildConfig.VERSION_NAME)
                .append("\n")
                .append("OS Version: ").append(Build.VERSION.SDK_INT)
                .append("\n")
                .append("Manufacturer: ").append(Build.MANUFACTURER)
                .append("\n")
                .append("Phone Model: ").append(Build.MODEL)
                .append("\n")
                .append("--------------------------------------------")
                .append("\n")
                .toString();
    }
}
