package com.fastaccess.data.dao.types;

import com.fastaccess.R;
import com.google.gson.annotations.SerializedName;

public enum IssueEventType {
    assigned(R.drawable.ic_profile),
    closed(R.drawable.ic_issue_closed),
    commented(R.drawable.ic_comment),
    committed(R.drawable.ic_announcement),
    demilestoned(R.drawable.ic_milestone),
    head_ref_deleted(R.drawable.ic_trash),
    head_ref_restored(R.drawable.ic_redo),
    labeled(R.drawable.ic_label),
    locked(R.drawable.ic_lock),
    mentioned(R.drawable.ic_at),
    merged(R.drawable.ic_fork),
    milestoned(R.drawable.ic_milestone),
    referenced(R.drawable.ic_format_quote),
    renamed(R.drawable.ic_edit),
    reopened(R.drawable.ic_issue_opened),
    subscribed(R.drawable.ic_announcement),
    unassigned(R.drawable.ic_announcement),
    unlabeled(R.drawable.ic_label),
    unlocked(R.drawable.ic_unlock),
    unsubscribed(R.drawable.ic_announcement),
    @SerializedName("cross-referenced")crossReferenced(R.drawable.ic_format_quote);

    private int iconResId;

    IssueEventType(int iconResId) {this.iconResId = iconResId;}

    public int getIconResId() {
        return iconResId;
    }
}