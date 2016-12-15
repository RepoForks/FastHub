package com.fastaccess.ui.modules.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.FontButton;
import com.fastaccess.ui.widgets.FontEditText;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import icepick.State;

/**
 * Created by Kosh on 27 Nov 2016, 1:32 AM
 */

public class CommentsEditorView extends BaseActivity<CommentsEditorMvp.View, CommentsEditorPresenter> implements CommentsEditorMvp.View {

    private RichText richText;
    private CharSequence savedText;

    @BindView(R.id.cancel) FontButton cancel;
    @BindView(R.id.ok) FontButton ok;
    @BindView(R.id.view) ForegroundImageView viewCode;
    @BindView(R.id.editText) FontEditText editText;

    @State @BundleConstant.ExtraTYpe String extraType;
    @State String itemId;
    @State String login;
    @State int issueNumber;
    @State long commentId = 0;

    @Override protected int layout() {
        return R.layout.add_comment_dialog_layout;
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

    @NonNull @Override public CommentsEditorPresenter providePresenter() {
        return new CommentsEditorPresenter();
    }

    @OnTextChanged(value = R.id.editText, callback = OnTextChanged.Callback.TEXT_CHANGED) void onEdited(CharSequence charSequence) {
        if (viewCode.getTag() != null) {
            return;
        }
        savedText = charSequence;
    }

    @OnClick(R.id.view) void onViewMarkDown(View v) {
        if (InputHelper.isEmpty(editText)) return;
        if (v.getTag() == null) {
            v.setTag("whatever");
            richText = MarkDownProvider.convertTextToMarkDown(editText, InputHelper.toString(editText));
            ViewHelper.hideKeyboard(editText);
        } else {
            richText.clear();
            v.setTag(null);
            editText.setText(savedText);
        }
    }

    @OnClick({R.id.headerOne, R.id.headerTwo, R.id.headerThree, R.id.bold, R.id.italic,
            R.id.strikethrough, R.id.bullet, R.id.header, R.id.code, R.id.numbered,
            R.id.quote, R.id.link, R.id.image}) void onActions(View v) {
        if (viewCode.getTag() != null) return;
        getPresenter().onActionClicked(editText, v.getId());
    }

    @OnClick(value = {R.id.ok, R.id.cancel}) void onClick(View view) {
        if (view.getId() == R.id.ok) {
            getPresenter().onHandleSubmission(savedText, extraType, itemId, commentId, login, issueNumber);
        } else {
            finish();
        }
    }

    @OnClick(R.id.back) void onBack() {
        finish();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                itemId = bundle.getString(BundleConstant.ID);
                commentId = bundle.getLong(BundleConstant.EXTRA_ID);
                login = bundle.getString(BundleConstant.EXTRA2_ID);
                issueNumber = bundle.getInt(BundleConstant.EXTRA3_ID);
                //noinspection WrongConstant
                extraType = bundle.getString(BundleConstant.EXTRA_TYPE);
                editText.setText(bundle.getString(BundleConstant.EXTRA));
            }
        }
    }

    @Override protected void onDestroy() {
        if (richText != null) richText.clear();
        super.onDestroy();
    }

    @Override public void onSendResultAndFinish(@NonNull CommentsModel commentModel, boolean isNew) {
        hideProgress();
        Intent intent = new Intent();
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ITEM, commentModel)
                .put(BundleConstant.EXTRA, isNew)
                .end());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override public void onShowProgress() {
        hideProgress();
        showProgress(R.string.loading_please_wait);
    }

    @Override public void onShowMessage(String message) {
        hideProgress();
        showMessage(getString(R.string.error), message);
    }

    @Override public void onSendMarkDownResult() {
        Intent intent = new Intent();
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, savedText).end());
        setResult(RESULT_OK, intent);
        finish();
    }
}
