package com.fastaccess.ui.modules.search;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.EditText;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 08 Dec 2016, 8:19 PM
 */

public interface SearchMvp {

    interface View extends BaseMvp.FAView {}

    interface Presenter extends BaseMvp.FAPresenter<View> {

        void onSearchClicked(@NonNull ViewPager viewPager, @NonNull EditText editText);

    }
}
