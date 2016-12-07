package com.fastaccess.ui.modules.main.profile.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import rx.Single;

/**
 * Created by Kosh on 03 Dec 2016, 9:16 AM
 */

public class ProfileOverviewPresenter extends BasePresenter<ProfileOverviewMvp.View> implements ProfileOverviewMvp.Presenter {
    @Override public void onFragmentCreated(@Nullable Bundle bundle) {
        if (bundle == null || bundle.getString(BundleConstant.EXTRA) == null) {
            throw new NullPointerException("Either bundle or User is null");
        }
        String login = bundle.getString(BundleConstant.EXTRA);
        if (login != null) {
            manageSubscription(
                    RxHelper.getObserver(RestProvider.getUserRestService().getUser(login))
                            .doOnSubscribe(() -> sendToView(ProfileOverviewMvp.View::onShowProgress))
                            .doOnNext(userModelResponse -> {
                                UserModel userModel = userModelResponse.body();
                                onSendUserToView(userModel);
                                if (userModel != null) {
                                    UserModel.saveOtherUsers(userModel);
                                }
                            })
                            .onErrorReturn(throwable -> {
                                sendToView(view -> view.onShowMessage(throwable.getMessage()));
                                onWorkOffline(login);
                                return null;
                            })
                            .subscribe());
        }
    }

    @Override public void onWorkOffline(@NonNull String login) {
        UserModel userModel = UserModel.getUser();
        if (userModel == null) return;
        Single<UserModel> userModelSingle = UserModel.getUser(login);
        manageSubscription(userModelSingle
                .subscribe(this::onSendUserToView));
    }

    @Override public void onSendUserToView(@Nullable UserModel userModel) {
        sendToView(view -> view.onInitViews(userModel));
    }
}