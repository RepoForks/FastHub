package com.fastaccess.ui.modules.viewer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.fastaccess.R;
import com.fastaccess.data.dao.FileModel;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.provider.rest.RestProvider;
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
    private boolean isImage;

    @Override public void onHandleIntent(@Nullable Bundle intent) {
        if (intent == null) return;
        if (intent.get(BundleConstant.EXTRA) instanceof Parcelable) {
            FilesListModel filesListModel = intent.getParcelable(BundleConstant.EXTRA);
            if (filesListModel != null) {
                if (MarkDownProvider.isArchive(filesListModel.getFilename()) || MarkDownProvider.isArchive(filesListModel.getRawUrl())) {
                    sendToView(view -> view.onShowError(R.string.archive_file_detected_error));
                    return;
                }
                //work offline first.
                onWorkOffline(filesListModel);
            }
        } else {
            String repoId = intent.getString(BundleConstant.ID);
            String login = intent.getString(BundleConstant.EXTRA_ID);
            String baseUrl = intent.getString(BundleConstant.EXTRA);
            if (!InputHelper.isEmpty(repoId) && !InputHelper.isEmpty(login)) {
                onWorkOffline(repoId, login, baseUrl);
            }
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
                            isImage = MarkDownProvider.isImage(fileModel.getFileName());
                            if (isImage) {
                                sendToView(view -> view.onSetImageUrl(fileModel.getFileName()));
                                return;
                            }
                            downloadedStream = fileModel.getContent();
                            isMarkdown = MarkDownProvider.isMarkdown(filesListModel.getFilename());
                            sendToView(view -> {
                                if (isMarkdown) {
                                    view.onSetMdText(downloadedStream, null);
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

    @Override public void onWorkOffline(@NonNull String repoId, @NonNull String login, @Nullable String baseUrl) {
        if (downloadedStream == null) {
            manageSubscription(FileModel.getFile(repoId, login)
                    .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                    .subscribe(fileModel -> {
                        if (fileModel != null) {
                            downloadedStream = fileModel.getContent();
                            isMarkdown = true;
                            isRepo = true;
                            sendToView(view -> view.onSetMdText(downloadedStream, baseUrl));
                        } else {
                            onWorkOnline(repoId, login, baseUrl);
                        }
                    }));
        }
    }

    @Override public void onWorkOnline(@NonNull FilesListModel filesListModel) {
        Logger.e(filesListModel.isNeedFetching());
        if (filesListModel.isNeedFetching()) {
            manageSubscription(RxHelper.getObserver(RestClient.getCodeFileData(filesListModel.getRawUrl()))
                    .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                    .map(file -> {
                        if (file.getContent() != null)
                            file.setContent(new String(Base64.decode(file.getContent(), Base64.DEFAULT)));
                        return file;
                    })
                    .doOnNext(file -> {
                        if (file.getContent() != null) {
                            Logger.e(file.getContent());
                            downloadedStream = file.getContent();
                            sendToView(view -> view.onSetCode(downloadedStream));
                            FileModel fileModel = new FileModel();
                            fileModel.setContent(downloadedStream);
                            fileModel.setFileName(filesListModel.getFilename());
                            fileModel.setMarkdown(false);
                            fileModel.setId(filesListModel.getId());
                            manageSubscription(FileModel.save(fileModel).subscribe());
                        }
                    })
                    .onErrorReturn(throwable -> {
                        throwable.printStackTrace();
                        int code = RestProvider.getErrorCode(throwable);
                        if (code == 404) {
                            sendToView(view -> view.onShowError(R.string.no_file_found));
                        } else {
                            sendToView(view -> view.onShowError(throwable.getMessage()));
                        }
                        return null;
                    })
                    .subscribe());
        } else {
            manageSubscription(RxHelper.getObserver(RestClient.getFileData(filesListModel.getRawUrl()))
                    .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                    .doOnNext(s -> {
                        isMarkdown = MarkDownProvider.isMarkdown(filesListModel.getFilename());
                        sendToView(view -> {
                            try {
                                downloadedStream = s.string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (isMarkdown) {
                                view.onSetMdText(downloadedStream, null);
                            } else {
                                view.onSetCode(downloadedStream);
                            }
                        });
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
                        int code = RestProvider.getErrorCode(throwable);
                        if (code == 404) {
                            sendToView(view -> view.onShowError(R.string.no_file_found));
                        } else {
                            sendToView(view -> view.onShowError(throwable.getMessage()));
                        }
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onWorkOnline(@NonNull String repoId, @NonNull String login, @Nullable String baseUrl) {
        manageSubscription(RxHelper.getObserver(RestClient.getRepoReadme(login, repoId, null))
                .doOnSubscribe(() -> sendToView(ViewerMvp.View::onShowMdProgress))
                .doOnNext(s -> {
                    isMarkdown = true;
                    isRepo = true;
                    downloadedStream = s;
                    sendToView(view -> view.onSetMdText(downloadedStream, baseUrl));
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
                    int code = RestProvider.getErrorCode(throwable);
                    if (code == 404) {
                        sendToView(view -> view.onShowError(R.string.no_readme_found));
                    } else {
                        sendToView(view -> view.onShowError(throwable.getMessage()));
                    }
                    return null;
                })
                .subscribe());
    }

    @Override public boolean isRepo() {
        return isRepo;
    }

    @Override public boolean isImage() {
        return isImage;
    }
}
