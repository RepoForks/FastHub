package com.fastaccess.ui.modules.main.profile.followers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class ProfileFollowersPresenter extends BasePresenter<ProfileFollowersMvp.View> implements ProfileFollowersMvp.Presenter {

    private ArrayList<UserModel> users = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;

    @Override public int getCurrentPage() {
        return page;
    }

    @Override public int getPreviousTotal() {
        return previousTotal;
    }

    @Override public void setCurrentPage(int page) {
        this.page = page;
    }

    @Override public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    @Override public void onCallApi(int page, @Nullable String parameter) {
        if (parameter == null) {
            throw new NullPointerException("Username is null");
        }
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(ProfileFollowersMvp.View::onHideProgress);
            return;
        }
        manageSubscription(RxHelper.getObserver(RestClient.getFollowers(parameter, page))
                .doOnSubscribe(() -> sendToView(ProfileFollowersMvp.View::onShowProgress))
                .doOnNext(repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    if (page == 1) {
                        users.clear();
                        manageSubscription(UserModel.saveFollowers(parameter, repoModelPageable.getItems()).subscribe());
                    }
                    users.addAll(repoModelPageable.getItems());
                    sendToView(ProfileFollowersMvp.View::onNotifyAdapter);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    onWorkOffline(parameter);
                    return null;
                })
                .subscribe());
    }

    @NonNull @Override public ArrayList<UserModel> getUsers() {
        return users;
    }

    @Override public void onWorkOffline(@NonNull String login) {
        if (users.isEmpty()) {
            manageSubscription(UserModel
                    .getFollowers(login)
                    .subscribe(models -> {
                        if (models != null) {
                            users.addAll(models);
                            sendToView(ProfileFollowersMvp.View::onNotifyAdapter);
                        }
                    }));
        }
    }

    @Override public void onItemClick(int position, View v, UserModel item) {

    }

    @Override public void onItemLongClick(int position, View v, UserModel item) {}
}
