package com.fastaccess.ui.modules.search;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.EditText;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.search.users.SearchUsersView;

/**
 * Created by Kosh on 08 Dec 2016, 8:20 PM
 */

public class SearchPresenter extends BasePresenter<SearchMvp.View> implements SearchMvp.Presenter {
    @Override public void onSearchClicked(@NonNull ViewPager viewPager, @NonNull EditText editText) {
        boolean isEmpty = InputHelper.isEmpty(editText) || InputHelper.toString(editText).length() < 3;
        editText.setError(isEmpty ? editText.getResources().getString(R.string.minimum_three_chars) : null);
        if (!isEmpty) {
            SearchUsersView repos = (SearchUsersView) viewPager.getAdapter().instantiateItem(viewPager, 0);
            SearchUsersView users = (SearchUsersView) viewPager.getAdapter().instantiateItem(viewPager, 1);
            SearchUsersView issues = (SearchUsersView) viewPager.getAdapter().instantiateItem(viewPager, 2);
            SearchUsersView code = (SearchUsersView) viewPager.getAdapter().instantiateItem(viewPager, 3);
            repos.onSetSearchQuery(InputHelper.toString(editText));
            users.onSetSearchQuery(InputHelper.toString(editText));
            issues.onSetSearchQuery(InputHelper.toString(editText));
            code.onSetSearchQuery(InputHelper.toString(editText));
        }
    }
}
