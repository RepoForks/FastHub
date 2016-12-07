package com.fastaccess.ui.modules.user;

import android.support.annotation.NonNull;

import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 03 Dec 2016, 8:00 AM
 */

public class UserPagerPresenter extends BasePresenter<UserPagerMvp.View> implements UserPagerMvp.Presenter {
    private boolean isSuccessResponse;
    private boolean isFollowing;

    @Override public void onCheckFollowStatus(@NonNull String login) {
        manageSubscription(RxHelper.getObserver(RestClient.getFollowStatus(login))
                .doOnNext(response -> {
                    isSuccessResponse = true;
                    isFollowing = response.code() == 204;
                    sendToView(UserPagerMvp.View::onInvalidateMenuItem);
                })
                .onErrorReturn(throwable -> {
                    throwable.printStackTrace();
                    return null;
                })
                .subscribe());
    }

    @Override public boolean isSuccessResponse() {
        return isSuccessResponse;
    }

    @Override public boolean isFollowing() {
        return isFollowing;
    }

    @Override public void onFollowMenuItemClicked(@NonNull String login) {
        manageSubscription(RxHelper.getObserver(RestClient.followUnfollowUser(login, isFollowing))
                .doOnNext(booleanResponse -> {
                    if (booleanResponse.code() == 204) {
                        isFollowing = !isFollowing;
                        sendToView(UserPagerMvp.View::onInvalidateMenuItem);
                    }
                })
                .onErrorReturn(throwable -> {
                    sendToView(UserPagerMvp.View::onInvalidateMenuItem);
                    return null;
                })
                .subscribe());
    }
}
