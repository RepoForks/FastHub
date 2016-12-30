package com.fastaccess.ui.modules.repo.code;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.FragmentPagerAdapterModel;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.adapter.FragmentsPagerAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.ViewPagerView;

import butterknife.BindView;

/**
 * Created by Kosh on 31 Dec 2016, 1:36 AM
 */

public class RepoCodePagerView extends BaseFragment<RepoCodePagerMvp.View, RepoCodePagerPresenter> implements RepoCodePagerMvp.View {
    public static final String TAG = RepoCodePagerView.class.getSimpleName();

    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;

    public static RepoCodePagerView newInstance(@NonNull RepoModel repoModel) {
        RepoCodePagerView view = new RepoCodePagerView();
        view.setArguments(Bundler.start()
                .put(BundleConstant.EXTRA, repoModel)
                .end());
        return view;
    }

    @Override protected int fragmentLayout() {
        return R.layout.inner_tabbed_pager_layout;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RepoModel repoModel = getArguments().getParcelable(BundleConstant.EXTRA);
        if (repoModel == null) throw new NullPointerException("Repo is null???");
        pager.setAdapter(new FragmentsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapterModel.buildForRepoCode(getContext(), repoModel)));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(pager);
    }

    @NonNull @Override public RepoCodePagerPresenter providePresenter() {
        return new RepoCodePagerPresenter();
    }
}
