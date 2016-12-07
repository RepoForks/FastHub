package com.fastaccess.ui.base.mvp;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

import com.fastaccess.ui.widgets.dialog.MessageDialogView;

import net.grandcentrix.thirtyinch.TiView;

import rx.Subscription;

/**
 * Created by Kosh on 25 May 2016, 9:09 PM
 */

public interface BaseMvp {
    interface FAView extends TiView, MessageDialogView.MessageDialogViewActionCallback {}

    interface FAPresenter<V extends BaseMvp.FAView> {

        boolean isAttached();

        @Nullable V getView();

        void manageSubscription(@Nullable Subscription subscription);

    }

    interface NavigationCallback {
        void showProgress(@StringRes int resId);

        void hideProgress();

        void showMessage(@StringRes int titleRes, @StringRes int msgRes);

        void showMessage(@NonNull String titleRes, @NonNull String msgRes);

        void onHomeClicked();

        void onSetupToolbar(@NonNull Toolbar toolbar, @DrawableRes int drawableRes);

        boolean isLoggedIn();
    }

    interface PaginationListener<P> {
        int getCurrentPage();

        int getPreviousTotal();

        void setCurrentPage(int page);

        void setPreviousTotal(int previousTotal);

        void onCallApi(int page, @Nullable P parameter);
    }
}
