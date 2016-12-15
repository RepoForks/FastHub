package com.fastaccess.ui.modules.comment;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 27 Nov 2016, 1:31 AM
 */

public interface CommentsEditorMvp {

    interface View extends BaseMvp.FAView {
        void onSendResultAndFinish(@NonNull CommentsModel commentModel, boolean isNew);

        void onShowProgress();

        void onShowMessage(String message);

        void onSendMarkDownResult();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        void onActionClicked(@NonNull EditText editText, @IdRes int id);

        void onEditGistComment(long id, @Nullable CharSequence savedText, @NonNull String gistId);

        void onSubmitGistComment(@Nullable CharSequence savedText, @NonNull String gistId);

        void onSubmitIssueComment(CharSequence savedText, @NonNull String itemId, @NonNull String login, int issueNumber);

        void onEditIssueComment(CharSequence savedText, @NonNull String itemId, long id, @NonNull String login, int issueNumber);

        void onHandleSubmission(@Nullable CharSequence savedText, @Nullable @BundleConstant.ExtraTYpe String extraType,
                                @Nullable String itemId, long commentId, @Nullable String login, int issueNumber);
    }
}
