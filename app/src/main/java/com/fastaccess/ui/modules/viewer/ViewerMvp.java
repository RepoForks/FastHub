package com.fastaccess.ui.modules.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.prettifier.pretty.PrettifyWebView;

/**
 * Created by Kosh on 27 Nov 2016, 3:41 PM
 */

public interface ViewerMvp {

    interface View extends BaseMvp.FAView, PrettifyWebView.OnContentChangedListener {

        void onSetImageUrl(@NonNull String url);

        void onSetMdText(@NonNull String text, String baseUrl);

        void onSetCode(@NonNull String text);

        void onShowError(@NonNull String msg);

        void onShowError(@StringRes int msg);

        void onShowMdProgress();

    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        void onHandleIntent(@Nullable Bundle intent);

        String downloadedStream();

        boolean isMarkDown();

        void onWorkOffline(@NonNull FilesListModel filesListModel);

        void onWorkOffline(@NonNull String repoId, @NonNull String login, @Nullable String refNo);

        void onWorkOnline(@NonNull FilesListModel filesListModel);

        void onWorkOnline(@NonNull String repoId, @NonNull String login, @Nullable String refNo);

        boolean isRepo();

        boolean isImage();
    }
}
