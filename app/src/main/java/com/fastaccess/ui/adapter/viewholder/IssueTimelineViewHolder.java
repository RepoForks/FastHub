package com.fastaccess.ui.adapter.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.IssueEventModel;
import com.fastaccess.data.dao.LabelModel;
import com.fastaccess.data.dao.types.IssueEventType;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 13 Dec 2016, 1:42 AM
 */

public class IssueTimelineViewHolder extends BaseViewHolder<IssueEventModel> {

    @BindView(R.id.stateImage) ForegroundImageView stateImage;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.stateText) FontTextView stateText;

    private IssueTimelineViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
    }

    public static IssueTimelineViewHolder newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter) {
        return new IssueTimelineViewHolder(getView(viewGroup, R.layout.issue_timeline_row_item), adapter);
    }

    @Override public void bind(@NonNull IssueEventModel issueEventModel) {
        IssueEventType event = issueEventModel.getEvent();
        SpannableBuilder spannableBuilder = SpannableBuilder.builder()
                .bold(issueEventModel.getActor().getLogin());
        if (event != null) {
            spannableBuilder
                    .append(" ")
                    .append(event.name());
            stateImage.setImageResource(event.getIconResId());
            if (event == IssueEventType.labeled || event == IssueEventType.unlabeled) {
                LabelModel labelModel = issueEventModel.getLabel();
                spannableBuilder
                        .append(" ")
                        .background(labelModel.getName(), Color.parseColor("#" + labelModel.getColor()));
            } else if (event == IssueEventType.assigned) {
                spannableBuilder
                        .append(" ")
                        .bold(issueEventModel.getAssigner().getLogin());
            }
        } else {
            stateImage.setImageResource(R.drawable.ic_label);
        }
        avatarLayout.setUrl(issueEventModel.getActor().getAvatarUrl(), issueEventModel.getActor().getLogin());
        stateText.setText(spannableBuilder
                .append(" ")
                .append(ParseDateFormat.getTimeAgo(issueEventModel.getCreatedAt())));
    }


}
