package com.fastaccess.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.ReleasesModel;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class ReleasesViewHolder extends BaseViewHolder<ReleasesModel> {


    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.details) FontTextView details;
    @BindView(R.id.download) ForegroundImageView download;
    @BindString(R.string.released) String released;
    @BindString(R.string.drafted) String drafted;
    @BindString(R.string.this_text) String thisText;

    private ReleasesViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
        download.setOnClickListener(this);
        download.setOnLongClickListener(this);
    }

    public static ReleasesViewHolder newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter) {
        return new ReleasesViewHolder(getView(viewGroup, R.layout.releases_row_item), adapter);
    }

    @Override public void bind(@NonNull ReleasesModel item) {
        title.setText(SpannableBuilder.builder().bold(!InputHelper.isEmpty(item.getName()) ? item.getName() : item.getTagName()));
        details.setText(SpannableBuilder.builder()
                .append(item.getAuthor().getLogin())
                .append(" ")
                .append(item.isDraft() ? drafted : released)
                .append(" ")
                .append(ParseDateFormat.getTimeAgo(item.getCreatedAt())));
        avatarLayout.setUrl(item.getAuthor().getAvatarUrl(), item.getAuthor().getLogin());
        avatarLayout.setVisibility(View.VISIBLE);
    }
}
