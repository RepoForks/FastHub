package com.fastaccess.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fastaccess.BuildConfig;
import com.fastaccess.R;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.login.LoginView;
import com.fastaccess.ui.widgets.dialog.MessageDialogView;

import net.grandcentrix.thirtyinch.TiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Kosh on 24 May 2016, 8:48 PM
 */

public abstract class BaseActivity<V extends BaseMvp.FAView, P extends BasePresenter<V>> extends TiActivity<P, V> implements
        BaseMvp.FAView, BaseMvp.NavigationCallback {

    private ProgressDialog progressDialog;
    private Toast toast;

    @LayoutRes protected abstract int layout();

    protected abstract boolean hasSlideExitAnimation();

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.toolbarShadow) View view;
    @Nullable @BindView(R.id.drawerLayout) protected DrawerLayout drawerLayout;

    protected abstract boolean isTransparent();

    protected abstract boolean canBack();

    protected abstract boolean isSecured();

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && hasSlideExitAnimation()) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
        if (layout() != 0) {
            setContentView(layout());
            ButterKnife.bind(this);
        }

        Icepick.setDebug(BuildConfig.DEBUG);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        setupToolbarAndStatusBar(toolbar);
        if (!isSecured()) {
            if (!isLoggedIn()) {
                startActivity(new Intent(this, LoginView.class));
                finish();
            }
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (canBack()) {
            if (item.getItemId() == android.R.id.home) {
                supportFinishAfterTransition();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override public void onDialogDismissed() {

    }//pass

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {

    }//pass

    @Override public void showMessage(@StringRes int titleRes, @StringRes int msgRes) {
        showMessage(getString(titleRes), getString(msgRes));
    }

    @Override public void showMessage(@NonNull String titleRes, @NonNull String msgRes) {
        if (!isFinishing()) {
            try {
                getSupportFragmentManager().executePendingTransactions();
                Fragment fragment = AppHelper.getFragmentByTag(getSupportFragmentManager(), "BaseActivity");
                if (fragment == null) {
                    MessageDialogView.newInstance(titleRes, msgRes).show(getSupportFragmentManager(), "BaseActivity");
                } else {
                    MessageDialogView messageDialogView = (MessageDialogView) fragment;
                    messageDialogView.setArguments(MessageDialogView.getBundle(titleRes, msgRes, null));
                    messageDialogView.initMessage();
                }
                return;
            } catch (Exception ignored) {}
        }
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, msgRes, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override public boolean isLoggedIn() {
        return !InputHelper.isEmpty(PrefGetter.getToken()) && UserModel.getUser() != null;
    }

    @Override public void onHomeClicked() {
        if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            supportFinishAfterTransition();
        }
    }

    @Override public void onSetupToolbar(@NonNull Toolbar toolbar, @DrawableRes int drawableRes) {
        setupToolbarAndStatusBar(toolbar);
        setToolbarIcon(drawableRes);
    }

    @Override public void showProgress(@StringRes int resId) {
        if (resId != 0) {
            getProgressDialog().setMessage(getString(resId));
        }
        if (!getProgressDialog().isShowing()) getProgressDialog().show();
    }

    @Override public void hideProgress() {
        if (getProgressDialog().isShowing()) getProgressDialog().dismiss();
    }

    @NonNull private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.in_progress));
        }
        return progressDialog;
    }

    @Override public void finish() {
        super.finish();
        onFinishWithAnimation();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        onFinishWithAnimation();
    }

    private void onFinishWithAnimation() {if (hasSlideExitAnimation()) overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);}

    private void setupToolbarAndStatusBar(@Nullable Toolbar toolbar) {
        changeAppColor(isTransparent());
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (canBack()) {
                if (getSupportActionBar() != null) {
                    toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    protected void setToolbarIcon(@DrawableRes int res) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(res);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void hideShadow() {
        if (view != null) view.setVisibility(View.GONE);
    }

    protected void changeAppColor(boolean isTransparent) {
        if (!isTransparent) {
            getWindow().setStatusBarColor(ViewHelper.getPrimaryDarkColor(this));
        }
    }
}
