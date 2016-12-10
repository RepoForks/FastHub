package com.fastaccess.helper;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kosh on 12 Nov 2016, 3:55 PM
 */

public class BundleConstant {
    public static final String ITEM = "item";
    public static final String ID = "id";
    public static final String EXTRA = "extra";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA2_ID = "extra2_id";
    public static final String EXTRA_TYPE = "extra_type";
    public static final int REQUEST_CODE = 2016;


    public static final String NEW_COMMENT_EXTRA = "new_comment_extra";

    public static final String EDIT_COMMENT_EXTRA = "edit_comment_extra";

    public static final String FOR_RESULT_EXTRA = "for_result_extra";

    @StringDef({
            NEW_COMMENT_EXTRA,
            EDIT_COMMENT_EXTRA,
            FOR_RESULT_EXTRA
    })
    @Retention(RetentionPolicy.SOURCE) public @interface ExtraTYpe {}
}
