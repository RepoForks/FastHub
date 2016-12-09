package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.ui.adapter.viewholder.IssuesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class IssuesAdapter extends BaseRecyclerAdapter<IssueModel, IssuesViewHolder, BaseViewHolder.OnItemClickListener<IssueModel>> {
    public IssuesAdapter(@NonNull List<IssueModel> data) {
        super(data);
    }

    @Override protected IssuesViewHolder viewHolder(ViewGroup parent, int viewType) {
        return IssuesViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(IssuesViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
