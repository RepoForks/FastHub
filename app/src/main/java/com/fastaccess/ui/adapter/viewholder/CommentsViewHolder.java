package com.fastaccess.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class CommentsViewHolder extends BaseViewHolder<CommentsModel> {

    @BindView(R.id.avatarView) AvatarLayout avatar;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.name) FontTextView name;
    @BindView(R.id.comment) FontTextView comment;

    public CommentsViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
        comment.setOnLongClickListener(this);
    }

    public static View getView(@NonNull ViewGroup viewGroup) {
        return getView(viewGroup, R.layout.comments_row_item);
    }

    @Override public void bind(@NonNull CommentsModel commentsModel) {
        if (commentsModel.getUser() != null) {
            avatar.setUrl(commentsModel.getUser().getAvatarUrl(), commentsModel.getUser().getLogin());
        } else {
            avatar.setUrl(null, null);
        }
        MarkDownProvider.convertTextToMarkDown(comment, commentsModel.getBody());
        name.setText(commentsModel.getUser() != null ? commentsModel.getUser().getLogin() : "Anonymous");
        date.setText(ParseDateFormat.getTimeAgo(commentsModel.getCreatedAt()));
    }
}
