package com.fastaccess.ui.modules.repo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.ViewPagerView;

import butterknife.BindView;

/**
 * Created by Kosh on 09 Dec 2016, 4:17 PM
 */

public class RepoPagerView extends BaseActivity<RepoPagerMvp.View, RepoPagerPresenter> implements RepoPagerMvp.View {

    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;

    @Override protected int layout() {
        return R.layout.tabbed_pager_layout;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return true;
    }

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override protected boolean isSecured() {
        return false;
    }

    @NonNull @Override public RepoPagerPresenter providePresenter() {
        return new RepoPagerPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
        } else {
            if (!InputHelper.isEmpty(getPresenter().repoId()) && !InputHelper.isEmpty(getPresenter().login())) {
                onInitAdapter();
            } else {
                onFinishActivity();
            }
        }
    }

    @Override public void onFinishActivity() {
        finish();
    }

    @Override public void onInitAdapter() {
        tabs.setupWithViewPager(pager);
    }
}
