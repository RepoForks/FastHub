package com.fastaccess.ui.widgets.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.adapter.SimpleListAdapter;
import com.fastaccess.ui.base.BaseBottomSheetDialog;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Kosh on 31 Dec 2016, 3:19 PM
 */

public class ListDialogView<O extends Parcelable> extends BaseBottomSheetDialog implements BaseViewHolder.OnItemClickListener<O> {

    public interface onSimpleItemSelection<O extends Parcelable> {
        void onItemSelected(O item);
    }

    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.recycler) DynamicRecyclerView recycler;

    private onSimpleItemSelection onSimpleItemSelection;

    @Override protected int layoutRes() {
        return R.layout.simple_list_dialog;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof onSimpleItemSelection) {
            onSimpleItemSelection = (onSimpleItemSelection) getParentFragment();
        } else if (context instanceof onSimpleItemSelection) {
            onSimpleItemSelection = (onSimpleItemSelection) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        onSimpleItemSelection = null;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<O> objects = getArguments().getParcelableArrayList(BundleConstant.ITEM);
        String titleText = getArguments().getString(BundleConstant.EXTRA);
        title.setText(titleText);
        if (objects != null) {
            SimpleListAdapter<O> adapter = new SimpleListAdapter<O>(objects, this);
            recycler.setAdapter(adapter);
        } else {
            dismiss();
        }
    }

    @SuppressWarnings("unchecked") @Override public void onItemClick(int position, View v, O item) {
        if (onSimpleItemSelection != null) {
            onSimpleItemSelection.onItemSelected(item);
        }
        dismiss();
    }

    @Override public void onItemLongClick(int position, View v, O item) {
        onItemClick(position, v, item);
    }

    public void initArguments(@NonNull String title, @NonNull ArrayList<O> objects) {
        setArguments(Bundler.start()
                .put(BundleConstant.EXTRA, title)
                .putParcelableArrayList(BundleConstant.ITEM, objects)
                .end());
    }
}
