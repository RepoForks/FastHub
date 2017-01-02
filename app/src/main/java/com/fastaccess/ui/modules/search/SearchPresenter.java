package com.fastaccess.ui.modules.search;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.AutoCompleteTextView;

import com.annimon.stream.Stream;
import com.fastaccess.R;
import com.fastaccess.data.dao.SearchHintsModel;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.search.code.SearchCodeView;
import com.fastaccess.ui.modules.search.issues.SearchIssuesView;
import com.fastaccess.ui.modules.search.repos.SearchReposView;
import com.fastaccess.ui.modules.search.users.SearchUsersView;

import java.util.ArrayList;


/**
 * Created by Kosh on 08 Dec 2016, 8:20 PM
 */
public class SearchPresenter extends BasePresenter<SearchMvp.View> implements SearchMvp.Presenter {
    private ArrayList<String> hints = new ArrayList<>();

    @Override protected void onAttachView(@NonNull SearchMvp.View view) {
        super.onAttachView(view);
        manageSubscription(SearchHintsModel.getHints()
                .toObservable()
                .limit(11)
                .subscribe(strings -> {
                    if (strings != null) hints.addAll(strings);
                    view.onNotifyAdapter(null);
                }));
    }

    @NonNull @Override public ArrayList<String> getHints() {
        return hints;
    }

    @Override public void onSearchClicked(@NonNull ViewPager viewPager, @NonNull AutoCompleteTextView editText) {
        boolean isEmpty = InputHelper.isEmpty(editText) || InputHelper.toString(editText).length() < 3;
        editText.setError(isEmpty ? editText.getResources().getString(R.string.minimum_three_chars) : null);
        if (!isEmpty) {
            editText.dismissDropDown();
            AppHelper.hideKeyboard(editText);
            SearchReposView repos = (SearchReposView) viewPager.getAdapter().instantiateItem(viewPager, 0);
            SearchUsersView users = (SearchUsersView) viewPager.getAdapter().instantiateItem(viewPager, 1);
            SearchIssuesView issues = (SearchIssuesView) viewPager.getAdapter().instantiateItem(viewPager, 2);
            SearchCodeView code = (SearchCodeView) viewPager.getAdapter().instantiateItem(viewPager, 3);
            String query = InputHelper.toString(editText);
            repos.onSetSearchQuery(query);
            users.onSetSearchQuery(query);
            issues.onSetSearchQuery(query);
            code.onSetSearchQuery(query);
            boolean noneMatch = Stream.of(hints).noneMatch(value -> value.equalsIgnoreCase(query));
            if (noneMatch) {
                manageSubscription(SearchHintsModel.saveHints(query).subscribe());
                sendToView(view -> view.onNotifyAdapter(query));
            }
        }
    }
}
