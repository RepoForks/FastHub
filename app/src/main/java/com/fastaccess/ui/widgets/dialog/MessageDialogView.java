package com.fastaccess.ui.widgets.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.base.BaseBottomSheetDialog;
import com.fastaccess.ui.widgets.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 16 Sep 2016, 2:15 PM
 */

public class MessageDialogView extends BaseBottomSheetDialog {

    public static final String TAG = MessageDialogView.class.getSimpleName();

    public interface MessageDialogViewActionCallback {
        void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle);

        void onDialogDismissed();
    }

    @BindView(R.id.title) FontTextView title;

    @BindView(R.id.message) FontTextView message;

    @Nullable private MessageDialogViewActionCallback callback;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) getParentFragment();
        } else if (context instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @OnClick({R.id.cancel, R.id.ok}) public void onClick(View view) {
        if (callback != null) {
            isAlreadyHidden = true;
            callback.onMessageDialogActionClicked(view.getId() == R.id.ok, getArguments().getBundle("bundle"));
        }
        dismiss();
    }

    @Override protected int layoutRes() {
        return R.layout.message_dialog;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        title.setText(bundle.getString("bundleTitle"));
        message.setText(bundle.getString("bundleMsg"));
    }

    @Override protected void onDismissedByScrolling() {
        super.onDismissedByScrolling();
        if (callback != null) callback.onDialogDismissed();
    }

    @Override protected void onHidden() {
        if (callback != null) callback.onDialogDismissed();
        super.onHidden();
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg) {
        return newInstance(bundleTitle, bundleMsg, null);
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, @Nullable Bundle bundle) {
        MessageDialogView messageDialogView = new MessageDialogView();
        messageDialogView.setArguments(getBundle(bundleTitle, bundleMsg, bundle));
        return messageDialogView;
    }

    public static Bundle getBundle(String bundleTitle, String bundleMsg, Bundle bundle) {
        return Bundler
                .start()
                .put("bundleTitle", bundleTitle)
                .put("bundleMsg", bundleMsg)
                .put("bundle", bundle)
                .end();
    }

    public void initMessage() {
        Bundle bundle = getArguments();
        title.setText(bundle.getString("bundleTitle"));
        message.setText(bundle.getString("bundleMsg"));
    }
}
