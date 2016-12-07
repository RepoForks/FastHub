package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.ui.adapter.viewholder.FilesViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class FilesAdapter extends BaseRecyclerAdapter<FilesListModel, FilesViewHolder, BaseViewHolder
        .OnItemClickListener<FilesListModel>> {

    public FilesAdapter(@NonNull List<FilesListModel> data, @Nullable BaseViewHolder.OnItemClickListener<FilesListModel> listener) {
        super(data, listener);
    }

    @Override protected FilesViewHolder viewHolder(ViewGroup parent, int viewType) {
        return FilesViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(FilesViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
