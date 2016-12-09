package com.fastaccess;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.helper.TypeFaceHelper;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.fastaccess.provider.uil.UILProvider;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Kosh on 10 Nov 2016, 3:40 PM
 */

public class App extends MultiDexApplication {
    private static App instance;
    private static UserModel userModel;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        RxPaperBook.init(this);
        UILProvider.initUIL(this);
        TypeFaceHelper.generateTypeface(this);
    }

    @Override public void onLowMemory() {
        clearCache();
        super.onLowMemory();
    }

    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == Activity.TRIM_MEMORY_BACKGROUND || level == Activity.TRIM_MEMORY_RUNNING_LOW) {
            clearCache();
        }
    }

    private void clearCache() {
        if (ImageLoader.getInstance().isInited()) ImageLoader.getInstance().clearMemoryCache();
    }

    public static App getInstance() {
        return instance;
    }

    public static UserModel getUser() {
        if (userModel == null) {
            userModel = UserModel.getUser();
        }
        return userModel;
    }
}
