package com.fastaccess.ui.widgets;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Kosh on 20 Nov 2016, 12:21 AM
 */

public class StateLayout extends NestedScrollView {

    private static final int SHOW_PROGRESS_STATE = 1;
    private static final int HIDE_PROGRESS_STATE = 2;
    private static final int HIDE_RELOAD_STATE = 3;
    private static final int SHOW_RELOAD_STATE = 4;
    private static final int HIDDEN = 5;
    private static final int SHOWEN = 6;

    private OnClickListener onReloadListener;

    @BindView(R.id.empty_text) FontTextView emptyText;
    @BindView(R.id.reload) FontButton reload;
    @BindView(R.id.progressBar) ProgressBar progressBar;


    @State int layoutState = HIDDEN;
    @State String emptyTextValue;
    @State int adapterSize;

    @OnClick(R.id.reload) void onReload() {
        if (onReloadListener != null && !progressBar.isShown()) {
            onReloadListener.onClick(reload);
        }
    }

    public StateLayout(Context context) {
        super(context);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showProgress() {
        layoutState = SHOW_PROGRESS_STATE;
        setVisibility(VISIBLE);
        emptyText.setVisibility(GONE);
        reload.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        layoutState = HIDE_PROGRESS_STATE;
        if (InputHelper.isEmpty(emptyTextValue)) {
            setEmptyText(R.string.no_data);
        }
        emptyText.setVisibility(VISIBLE);
        reload.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        setVisibility(GONE);
    }

    public void hideReload() {
        layoutState = HIDE_RELOAD_STATE;
        reload.setVisibility(GONE);
        emptyText.setVisibility(GONE);
        setVisibility(GONE);
    }

    public void showReload(int adapterCount) {
        this.adapterSize = adapterCount;
        hideProgress();
        if (adapterCount == 0) {
            layoutState = SHOW_RELOAD_STATE;
            reload.setVisibility(VISIBLE);
            emptyText.setVisibility(VISIBLE);
            if (InputHelper.isEmpty(emptyTextValue)) {
                setEmptyText(R.string.no_data);
            }
            setVisibility(VISIBLE);
        }
    }

    public void setEmptyText(@StringRes int resId) {
        setEmptyText(getResources().getString(resId));
    }

    public void setEmptyText(@NonNull String text) {
        this.emptyTextValue = text;
        emptyText.setText(text);
    }

    public void setOnReloadListener(OnClickListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    @Override public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            layoutState = HIDDEN;
        } else {
            layoutState = SHOWEN;
        }
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.empty_layout, this);
        if (isInEditMode()) return;
        ButterKnife.bind(this);
    }

    @Override protected void onDetachedFromWindow() {
        onReloadListener = null;
        super.onDetachedFromWindow();
    }

    @Override public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        onHandleLayoutState();
    }

    private void onHandleLayoutState() {
        setEmptyText(emptyTextValue);
        switch (layoutState) {
            case SHOW_PROGRESS_STATE:
                showProgress();
                break;
            case HIDE_PROGRESS_STATE:
                hideProgress();
                break;
            case HIDE_RELOAD_STATE:
                hideReload();
                break;
            case SHOW_RELOAD_STATE:
                showReload(adapterSize);
                break;
            case HIDDEN:
                setVisibility(GONE);
                break;
            case SHOWEN:
                setVisibility(VISIBLE);
                showReload(adapterSize);
                break;
        }
    }
}

