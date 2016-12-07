package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.ui.adapter.viewholder.ReposViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class ReposAdapter extends BaseRecyclerAdapter<RepoModel, ReposViewHolder, BaseViewHolder.OnItemClickListener<RepoModel>> {
    private boolean isStarred;

    public ReposAdapter(@NonNull List<RepoModel> data, boolean isStarred) {
        super(data);
        this.isStarred = isStarred;
    }

    @Override protected ReposViewHolder viewHolder(ViewGroup parent, int viewType) {
        return ReposViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(ReposViewHolder holder, int position) {
        holder.bind(getItem(position), isStarred);
    }
}
