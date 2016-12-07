package com.fastaccess.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fastaccess.R;
import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.ui.modules.contentviewer.gists.comments.GistCommentsView;
import com.fastaccess.ui.modules.contentviewer.gists.files.GistFilesListView;

/**
 * Created by Kosh on 13 Nov 2016, 1:41 PM
 */

public class GistsPagerAdapter extends FragmentStatePagerAdapter {
    private GistsModel gistsModel;
    private Context context;

    public GistsPagerAdapter(FragmentManager fm, GistsModel gistsModel, Context context) {
        super(fm);
        this.gistsModel = gistsModel;
        this.context = context;
    }

    @Override public Fragment getItem(int position) {
        return position == 0 ? GistCommentsView.newInstance(gistsModel.getGistId()) : GistFilesListView.newInstance(gistsModel);
    }

    @Override public int getCount() {
        return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
        return position == 0 ? context.getString(R.string.comments) : context.getString(R.string.gist_files);
    }
}
