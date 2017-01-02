package com.fastaccess.ui.modules.main.gists.view.files;

import android.support.annotation.NonNull;

import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

/**
 * Created by Kosh on 13 Nov 2016, 1:35 PM
 */

public interface GistFilesListMvp {

    interface View extends BaseMvp.FAView {
        void onOpenFile(@NonNull FilesListModel item);
    }

    interface Presenter extends BaseMvp.FAPresenter<View>, BaseViewHolder.OnItemClickListener<FilesListModel> {}
}
