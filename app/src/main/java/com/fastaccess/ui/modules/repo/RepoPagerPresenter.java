package com.fastaccess.ui.modules.repo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fastaccess.R;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repo.code.RepoCodePagerView;
import com.fastaccess.ui.modules.repo.issues.RepoIssuesPagerView;
import com.fastaccess.ui.modules.repo.pull_request.RepoPullRequestPagerView;

import retrofit2.Response;
import rx.Observable;

import static com.fastaccess.helper.AppHelper.getVisibleFragment;

/**
 * Created by Kosh on 09 Dec 2016, 4:17 PM
 */

public class RepoPagerPresenter extends BasePresenter<RepoPagerMvp.View> implements RepoPagerMvp.Presenter {
    private boolean isWatched;
    private boolean isStarred;
    private boolean isForked;

    private String login;
    private String repoId;
    private RepoModel repo;

    @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            repo = bundle.getParcelable(BundleConstant.ITEM);
            login = bundle.getString(BundleConstant.EXTRA_ID);
            repoId = bundle.getString(BundleConstant.ID);
            if (repo != null) {
                sendToView(RepoPagerMvp.View::onInitRepo);
                onCheckStarring();
                onCheckWatching();
                return;
            } else if (!InputHelper.isEmpty(login()) && !InputHelper.isEmpty(repoId())) {
                manageSubscription(RxHelper.getObserver(RestClient.getRepo(login, repoId))
                        .doOnSubscribe(() -> sendToView(RepoPagerMvp.View::onShowProgress))
                        .doOnNext(repoModel -> {
                            repo = repoModel;
                            sendToView(view -> {
                                view.onInitRepo();
                                view.onNavigationChanged(RepoPagerMvp.CODE);
                            });
                            onCheckStarring();
                            onCheckWatching();
                        })
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline();
                            return null;
                        })
                        .subscribe()
                );
                return;
            }
        }
        sendToView(RepoPagerMvp.View::onFinishActivity);
    }

    @NonNull @Override public String repoId() {
        return repoId;
    }

    @NonNull @Override public String login() {
        return login;
    }

    @Nullable @Override public RepoModel getRepo() {
        return repo;
    }

    @Override public boolean isWatched() {
        return isWatched;
    }

    @Override public boolean isStarred() {
        return isStarred;
    }

    @Override public boolean isForked() {
        return isForked;
    }

    @Override public void onWatch() {
        if (getRepo() == null) return;
        String login = getRepo().getOwner().getLogin();
        String name = getRepo().getName();
        Observable<Response<Boolean>> observable = RxHelper
                .getObserver(!isWatched ? RestClient.watchRepo(login, name) : RestClient.unwatchRepo(login, name));
        manageSubscription(observable
                .doOnSubscribe(() -> sendToView(view -> view.onEnableDisableWatch(false)))
                .doOnNext(booleanResponse -> {
                    if (!isWatched) {
                        isWatched = booleanResponse.code() == 204;
                    } else {
                        isWatched = booleanResponse.code() != 204;
                    }
                    sendToView(view -> {
                        view.onRepoWatched(isWatched);
                        view.onChangeWatchedCount(isWatched);
                    });
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onEnableDisableWatch(true));
                    return null;
                })
                .subscribe());
    }

    @Override public void onStar() {
        if (getRepo() == null) return;
        String login = getRepo().getOwner().getLogin();
        String name = getRepo().getName();
        Observable<Response<Boolean>> observable = RxHelper
                .getObserver(!isStarred ? RestClient.starRepo(login, name) : RestClient.unstarRepo(login, name));
        manageSubscription(observable
                .doOnSubscribe(() -> sendToView(view -> view.onEnableDisableStar(false)))
                .doOnNext(booleanResponse -> {
                    if (!isStarred) {
                        isStarred = booleanResponse.code() == 204;
                    } else {
                        isStarred = booleanResponse.code() != 204;
                    }
                    sendToView(view -> {
                        view.onRepoStarred(isStarred);
                        view.onChangeStarCount(isStarred);
                    });
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onEnableDisableStar(true));
                    return null;
                })
                .subscribe());
    }

    @Override public void onFork() {
        if (!isForked && getRepo() != null) {
            String login = getRepo().getOwner().getLogin();
            String name = getRepo().getName();
            manageSubscription(RxHelper.getObserver(RestClient.forkRepo(login, name))
                    .doOnSubscribe(() -> sendToView(view -> view.onEnableDisableFork(false)))
                    .doOnNext(repoModel -> sendToView(view -> {
                        view.onRepoForked(isForked = repoModel != null);
                        view.onChangeForkCount(isForked);
                    }))
                    .onErrorReturn(throwable -> {
                        sendToView(view -> view.onEnableDisableFork(true));
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onCheckWatching() {
        if (getRepo() != null) {
            manageSubscription(RxHelper.getObserver(RestClient.checkWatchingRepo(repo.getOwner().getLogin(), repo.getName()))
                    .doOnSubscribe(() -> sendToView(view -> view.onEnableDisableWatch(false)))
                    .doOnNext(subscriptionModel -> sendToView(view -> view.onRepoWatched(isWatched = subscriptionModel.code() == 204)))
                    .onErrorReturn(throwable -> {
                        isWatched = false;
                        sendToView(view -> view.onRepoWatched(isWatched));
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onCheckStarring() {
        if (getRepo() != null) {
            manageSubscription(RxHelper.getObserver(RestClient.checkStarringRepo(repo.getOwner().getLogin(), repo.getName()))
                    .doOnSubscribe(() -> sendToView(view -> view.onEnableDisableStar(false)))
                    .doOnNext(response -> sendToView(view -> view.onRepoStarred(isStarred = response.code() == 204)))
                    .onErrorReturn(throwable -> {
                        isStarred = false;
                        sendToView(view -> view.onRepoStarred(isStarred));
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onWorkOffline() {
        if (!InputHelper.isEmpty(login()) && !InputHelper.isEmpty(repoId()) && repo == null) {
            manageSubscription(RepoModel.getRepo(repoId(), login())
                    .subscribe(repoModel -> {
                        repo = repoModel;
                        sendToView(view -> {
                            view.onInitRepo();
                            view.onNavigationChanged(RepoPagerMvp.CODE);
                        });
                    }));
        }
    }

    @Override public void onModuleChanged(@NonNull FragmentManager fragmentManager, @RepoPagerMvp.RepoNavigationType int type) {
        Fragment currentVisible = getVisibleFragment(fragmentManager);
        RepoCodePagerView codePagerView = (RepoCodePagerView) AppHelper.getFragmentByTag(fragmentManager, RepoCodePagerView.TAG);
        RepoIssuesPagerView repoIssuesPagerView = (RepoIssuesPagerView) AppHelper.getFragmentByTag(fragmentManager, RepoIssuesPagerView.TAG);
        RepoPullRequestPagerView pullRequestPagerView = (RepoPullRequestPagerView) AppHelper.getFragmentByTag(fragmentManager,
                RepoPullRequestPagerView.TAG);
        if (getRepo() == null) return;
        if (currentVisible == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, RepoCodePagerView.newInstance(getRepo()), RepoCodePagerView.TAG)
                    .commit();
            return;
        }
        switch (type) {
            case RepoPagerMvp.CODE:
                if (codePagerView == null) {
                    onAddAndHide(fragmentManager, RepoCodePagerView.newInstance(getRepo()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, codePagerView, currentVisible);
                }
                break;
            case RepoPagerMvp.ISSUES:
                if (repoIssuesPagerView == null) {
                    onAddAndHide(fragmentManager, RepoIssuesPagerView.newInstance(getRepo().getName(),
                            getRepo().getOwner().getLogin()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, repoIssuesPagerView, currentVisible);
                }
                break;
            case RepoPagerMvp.PULL_REQUEST:
                if (pullRequestPagerView == null) {
                    onAddAndHide(fragmentManager, RepoPullRequestPagerView.newInstance(getRepo().getName(),
                            getRepo().getOwner().getLogin()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, pullRequestPagerView, currentVisible);
                }
                break;
        }
    }

    @Override public void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
        toShow.onHiddenChanged(false);
    }

    @Override public void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .add(R.id.container, toAdd, toAdd.getClass().getSimpleName())
                .commit();
        toAdd.onHiddenChanged(false);
    }

    @Override public void onMenuItemSelect(@IdRes int id, int position) {
        if (getView() != null && isAttached()) {
            getView().onNavigationChanged(position);
        }
    }

    @Override public void onMenuItemReselect(@IdRes int id, int position) {}
}
