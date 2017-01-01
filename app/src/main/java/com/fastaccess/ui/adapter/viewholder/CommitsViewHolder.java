package com.fastaccess.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class CommitsViewHolder extends BaseViewHolder<CommitModel> {

    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.details) FontTextView details;


    private CommitsViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
    }

    public static CommitsViewHolder newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter) {
        return new CommitsViewHolder(getView(viewGroup, R.layout.issue_row_item), adapter);
    }

    @Override public void bind(@NonNull CommitModel commit) {
        title.setText(commit.getCommit().getMessage());
        String login = commit.getAuthor() != null ? commit.getAuthor().getLogin() : commit.getCommit().getAuthor().getName();
        String avatar = commit.getAuthor() != null ? commit.getAuthor().getAvatarUrl() : null;
        String date = commit.getCommit().getAuthor().getDate();
        details.setText(SpannableBuilder.builder()
                .bold(InputHelper.toNA(login))
                .append(" ")
                .append(ParseDateFormat.getTimeAgo(date != null ? date : "")));
        avatarLayout.setUrl(avatar, login);
        avatarLayout.setVisibility(View.VISIBLE);
    }
}
