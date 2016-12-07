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

public interface CommentsMvp {

    interface View extends BaseMvp.FAView {
        void onSendResultAndFinish(@NonNull CommentsModel commentModel, boolean isNew);

        void onShowProgress();

        void onShowMessage(String message);

        void onSendMarkDownResult();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        void onActionClicked(@NonNull EditText editText, @IdRes int id);

        void onEditComment(long id, @Nullable CharSequence savedText, @NonNull String gistId);

        void onSubmitComment(@Nullable CharSequence savedText, @NonNull String gistId);

        void onHandleSubmission(@Nullable CharSequence savedText, @Nullable @BundleConstant.ExtraTYpe String extraType,
                                @Nullable String itemId, long id);
    }
}
