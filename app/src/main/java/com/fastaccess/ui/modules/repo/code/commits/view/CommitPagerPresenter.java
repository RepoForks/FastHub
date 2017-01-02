package com.fastaccess.ui.modules.repo.code.commits.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class CommitPagerPresenter extends BasePresenter<CommitPagerMvp.View> implements CommitPagerMvp.Presenter {
    private CommitModel commitModel;

    @Nullable @Override public CommitModel getCommit() {
        return commitModel;
    }

    @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            commitModel = intent.getExtras().getParcelable(BundleConstant.ITEM);
            String sha = intent.getExtras().getString(BundleConstant.ID);
            String login = intent.getExtras().getString(BundleConstant.EXTRA_ID);
            String repoId = intent.getExtras().getString(BundleConstant.EXTRA2_ID);
            if (commitModel != null) {
                sendToView(CommitPagerMvp.View::onSetup);
                return;
            } else if (!InputHelper.isEmpty(sha) && !InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
                manageSubscription(RxHelper.getObserver(RestClient.getCommit(login, repoId, sha))
                        .doOnSubscribe(() -> sendToView(CommitPagerMvp.View::onShowProgress))
                        .doOnNext(commit -> {
                            commitModel = commit;
                            commitModel.setRepoId(repoId);
                            commit.setLogin(login);
                            sendToView(CommitPagerMvp.View::onSetup);
                        })
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline(sha, repoId, login);
                            return null;
                        })
                        .subscribe());
                return;
            }
        }
        sendToView(CommitPagerMvp.View::onSetup);
    }

    @Override public void onWorkOffline(@NonNull String sha, @NonNull String repoId, @NonNull String login) {
        //TODO
    }

}
