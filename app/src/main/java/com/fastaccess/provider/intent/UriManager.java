package com.fastaccess.provider.intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.ActorModel;
import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.helper.Logger;

import java.util.Arrays;

import io.mola.galimatias.GalimatiasParseException;
import io.mola.galimatias.URL;

public class UriManager {

    public static final String[] RESERVED_KEYS = new String[]{
            "/notifications", "/settings", "/blog", "/explore", "/dashboard", "/repositories", "/site",
            "/security", "/contact", "/about", "/orgs", "/"
    };

    public boolean isReserved(String uri) throws GalimatiasParseException {
        URL url = URL.parse(uri);
        return Arrays.asList(RESERVED_KEYS).contains(url.path());
    }

    public RepoModel getRepoInfo(String url) throws GalimatiasParseException {
        if (isReserved(url)) {
            return null;
        }
        RepoModel repoInfo = new RepoModel();
        URL parsedUrl = URL.parse(url);
        repoInfo.setName(parsedUrl.pathSegments().get(1));
        return repoInfo;
    }

    @Nullable public RepoModel getRepoInfo(Uri uri) {
        try {
            return getRepoInfo(uri.toString());
        } catch (GalimatiasParseException e) {
            return null;
        }
    }

    public ActorModel getUser(String url) throws GalimatiasParseException {
        if (isReserved(url)) {
            return null;
        }
        ActorModel user = new ActorModel();
        URL parsedUrl = URL.parse(url);
        user.setLogin(parsedUrl.pathSegments().get(0));
        Logger.e(user.getLogin());
        return user;
    }

    public ActorModel getUser(Uri uri) {
        try {
            return getUser(uri.toString());
        } catch (GalimatiasParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull CommitModel getCommitInfo(Uri uri) {
        CommitModel info = new CommitModel();
        info.setRepo(getRepoInfo(uri));
        info.setSha(uri.getLastPathSegment());
        return info;
    }

    @NonNull IssueModel getIssueCommentInfo(Uri uri) {
        IssueModel info = new IssueModel();

        info.setRepository(getRepoInfo(uri));

        String lastPathSegment = uri.getLastPathSegment();
        if (uri.getFragment() != null && uri.getFragment().contains("issuecomment-")) {
            String commentNum = uri.getFragment().replace("issuecomment-", "");
            info.setComments(Integer.parseInt(commentNum));
        }
        info.setNumber(Integer.parseInt(lastPathSegment));
        return info;
    }


    @NonNull IssueModel getIssueInfo(Uri uri) {
        IssueModel info = new IssueModel();
        info.setRepository(getRepoInfo(uri));
        String lastPathSegment = uri.getLastPathSegment();
        info.setNumber(Integer.parseInt(lastPathSegment));
        return info;
    }

    @NonNull FilesListModel getFileInfo(Uri uri) {
        FilesListModel info = new FilesListModel();
        info.setRawUrl(uri.getPath());
        info.setFilename(uri.getLastPathSegment());
        return info;
    }
}
