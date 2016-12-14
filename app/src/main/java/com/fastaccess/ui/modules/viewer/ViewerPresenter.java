package com.fastaccess.ui.modules.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.R;
import com.fastaccess.data.dao.FileModel;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.viewer.ViewerMvp.Presenter;

import java.io.IOException;

/**
 * Created by Kosh on 27 Nov 2016, 3:43 PM
 */

public class ViewerPresenter extends BasePresenter<ViewerMvp.View> implements Presenter {
    private String downloadedStream;
    private boolean isMarkdown;
    private boolean isRepo;

    @Override public void onHandleIntent(@Nullable Bundle intent) {
        if (intent == null) return;
        FilesListModel filesListModel = intent.getParcelable(BundleConstant.EXTRA);
        String repoId = intent.getString(BundleConstant.ID);
        String login = intent.getString(BundleConstant.EXTRA_ID);
        String refNo = intent.getString(BundleConstant.EXTRA);
        if (filesListModel != null) {
            if (MarkDownProvider.isArchive(filesListModel.getFilename()) || MarkDownProvider.isArchive(filesListModel.getRawUrl())) {
                sendToView(view -> view.onShowError(R.string.archive_file_detected_error));
                return;
            }
            //work offline first.
            onWorkOffline(filesListModel);
        } else if (!InputHelper.isEmpty(repoId) && !InputHelper.isEmpty(login)) {
            onWorkOffline(repoId, login, refNo);
        }
    }

    @Override public String downloadedStream() {
        return downloadedStream;
    }

    @Override public boolean isMarkDown() {
        return isMarkdown;
    }

    @Override public void onWorkOffline(@NonNull FilesListModel filesListModel) {
        if (downloadedStream == null) {
            manageSubscription(FileModel.getFile(filesListModel.getId(), filesListModel.getFilename())
                    .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                    .subscribe(fileModel -> {
                        if (fileModel != null) {
                            downloadedStream = fileModel.getContent();
                            isMarkdown = MarkDownProvider.isMarkdown(filesListModel.getFilename());
                            sendToView(view -> {
                                if (isMarkdown) {
                                    view.onSetMdText(downloadedStream);
                                } else {
                                    view.onSetCode(downloadedStream);
                                }
                            });
                        } else {
                            onWorkOnline(filesListModel);
                        }
                    }));
        }
    }

    @Override public void onWorkOffline(@NonNull String repoId, @NonNull String login, @Nullable String refNo) {
        if (downloadedStream == null) {
            manageSubscription(FileModel.getFile(repoId, login)
                    .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                    .subscribe(fileModel -> {
                        if (fileModel != null) {
                            downloadedStream = fileModel.getContent();
                            isMarkdown = true;
                            isRepo = true;
                            sendToView(view -> view.onSetMdText(downloadedStream));
                        } else {
                            onWorkOnline(repoId, login, refNo);
                        }
                    }));
        }
    }

    @Override public void onWorkOnline(@NonNull FilesListModel filesListModel) {
        manageSubscription(RxHelper.getObserver(RestClient.getFileData(filesListModel.getRawUrl()))
                .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                .doOnNext(s -> {
                    if (MarkDownProvider.isMarkdown(filesListModel.getFilename())) {
                        isMarkdown = true;
                        sendToView(view -> {
                            try {
                                downloadedStream = s.string();
                                view.onSetMdText(downloadedStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                                view.onShowError(e.getMessage());
                            }
                        });
                    } else {
                        isMarkdown = false;
                        sendToView(view -> {
                            try {
                                downloadedStream = s.string();
                                view.onSetCode(downloadedStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                                view.onShowError(e.getMessage());
                            }
                        });
                    }
                    if (downloadedStream != null) {
                        FileModel fileModel = new FileModel();
                        fileModel.setContent(downloadedStream);
                        fileModel.setFileName(filesListModel.getFilename());
                        fileModel.setMarkdown(isMarkdown);
                        fileModel.setId(filesListModel.getId());
                        manageSubscription(FileModel.save(fileModel).subscribe());
                    }
                })
                .onErrorReturn(throwable -> {
                    throwable.printStackTrace();
                    sendToView(view -> view.onShowError(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @Override public void onWorkOnline(@NonNull String repoId, @NonNull String login, @Nullable String refNo) {
        manageSubscription(RxHelper.getObserver(RestClient.getRepoReadme(login, repoId, refNo))
                .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                .doOnNext(s -> {
                    isMarkdown = true;
                    isRepo = true;
                    downloadedStream = s;
                    sendToView(view -> view.onSetMdText(downloadedStream));
                    FileModel fileModel = new FileModel();
                    fileModel.setContent(downloadedStream);
                    fileModel.setId(repoId);
                    fileModel.setFileName(login);
                    fileModel.setMarkdown(true);
                    fileModel.setRepo(true);
                    manageSubscription(FileModel.save(fileModel).subscribe());
                })
                .onErrorReturn(throwable -> {
                    throwable.printStackTrace();
                    sendToView(view -> view.onShowError(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @Override public boolean isRepo() {
        return isRepo;
    }
}
