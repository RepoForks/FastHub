package com.fastaccess.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.fastaccess.R;

/**
 * Created by Kosh on 12/12/15 10:51 PM
 */
public class ActivityHelper {

    public static final int SELECT_PHOTO_REQUEST = 102;


    public static boolean isLightTheme(@NonNull Context context, @NonNull ComponentName componentName) {
        boolean isLight = false;
        try {
            isLight = context.getPackageManager().getActivityInfo(componentName, 0).theme == R.style.LightTheme;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isLight;
    }

    @Nullable public static Activity getActivity(@Nullable Context cont) {
        if (cont == null) return null;
        else if (cont instanceof Activity) return (Activity) cont;
        else if (cont instanceof ContextWrapper) return getActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }

    public static void startCustomTab(@NonNull Activity context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary));
        CustomTabsIntent tabsIntent = builder.build();
        tabsIntent.launchUrl(context, Uri.parse("https://github.com/k0shk0sh/FastAccess"));//TODO
    }

    public static void startCustomTab(@NonNull Activity context, @NonNull Uri url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary));
        builder.setShowTitle(false);
        CustomTabsIntent tabsIntent = builder.build();
        tabsIntent.launchUrl(context, url);
    }

    public static void startCustomTab(@NonNull Activity context, @NonNull String url) {
        startCustomTab(context, Uri.parse(url));
    }

    @SafeVarargs public static void start(Activity activity, Class cl, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(activity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        activity.startActivity(intent, options.toBundle());
    }

    public static void start(Activity activity, Intent intent, View sharedElement) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                sharedElement, ViewHelper.getTransitionName(sharedElement));
        activity.startActivity(intent, options.toBundle());
    }

    public static void startReveal(Activity activity, Intent intent, View sharedElement) {
        Rect rect = ViewHelper.getLayoutPosition(sharedElement);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeClipRevealAnimation(sharedElement, rect.centerX(), rect.centerY(), sharedElement
                .getWidth(), sharedElement.getHeight());
        activity.startActivity(intent, options.toBundle());
    }

    @SafeVarargs public static void start(Activity activity, Intent intent, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        activity.startActivity(intent, options.toBundle());

    }

    public static void shareUrl(@NonNull Context context, @NonNull String url) {
        Activity activity = getActivity(context);
        if (activity == null) throw new IllegalArgumentException("Context give is not an instance of activity " + context.getClass().getName());
        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(context.getString(R.string.share_via))
                .setType("text/*")
                .setText(url)
                .startChooser();

    }
}
