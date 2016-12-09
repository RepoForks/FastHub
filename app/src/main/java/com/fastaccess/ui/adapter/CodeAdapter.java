package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.fastaccess.data.dao.SearchCodeModel;
import com.fastaccess.ui.adapter.viewholder.CodeViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 2:07 PM
 */

public class CodeAdapter extends BaseRecyclerAdapter<SearchCodeModel, CodeViewHolder, BaseViewHolder.OnItemClickListener<SearchCodeModel>> {
    public CodeAdapter(@NonNull List<SearchCodeModel> data) {
        super(data);
    }

    @Override protected CodeViewHolder viewHolder(ViewGroup parent, int viewType) {
        return CodeViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(CodeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
