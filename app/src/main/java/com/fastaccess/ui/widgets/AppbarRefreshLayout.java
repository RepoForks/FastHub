package com.fastaccess.ui.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.fastaccess.R;


/**
 * Created by kosh on 7/30/2015. CopyRights @ Innov8tif
 */
public class AppbarRefreshLayout extends SwipeRefreshLayout {

    public AppbarRefreshLayout(Context context) {
        super(context, null);
    }

    public AppbarRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.bluish_primary, R.color.bluish_primary_dark, R.color.bluish_primary_light, R.color.bluish_accent);
    }
}
