package com.fastaccess.provider.scheme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.modules.contentviewer.gists.GistsContentView;
import com.fastaccess.ui.modules.issues.IssuesDetailsView;
import com.fastaccess.ui.modules.user.UserPagerView;

import java.util.List;
import java.util.regex.Matcher;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

/**
 * Created by Kosh on 09 Dec 2016, 4:44 PM
 */

public class SchemeParser {


    private static final String HOST_DEFAULT = "github.com";
    private static final String HOST_GISTS = "gist.github.com";
    private static final String PROTOCOL_HTTPS = "https";

    public static void launchUri(@NonNull Context context, @NonNull Intent data) {
        Intent intent = convert(context, data);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(ACTION_VIEW, data.getData()).addCategory(CATEGORY_BROWSABLE));
        }
    }

    protected boolean isMatch(@Nullable String url, @NonNull Matcher matcher) {
        return !InputHelper.isEmpty(url) && matcher.reset(url).matches();
    }

    @Nullable public static String getGistId(@NonNull Uri uri) {
        List<String> segments = uri.getPathSegments();
        return segments != null && !segments.isEmpty() ? segments.get(0) : null;
    }

    @Nullable public static String getUser(@NonNull Uri uri) {
        List<String> segments = uri.getPathSegments();
        return segments != null && !segments.isEmpty() ? segments.get(0) : null;
    }

    @Nullable public static Intent convert(@NonNull Context context, final Intent intent) {
        if (intent == null) return null;
        if (!ACTION_VIEW.equals(intent.getAction())) return null;
        Uri data = intent.getData();
        if (data == null) return null;
        if (TextUtils.isEmpty(data.getHost()) || TextUtils.isEmpty(data.getScheme())) {
            String host = data.getHost();
            if (TextUtils.isEmpty(host)) host = HOST_DEFAULT;
            String scheme = data.getScheme();
            if (TextUtils.isEmpty(scheme)) scheme = PROTOCOL_HTTPS;
            String prefix = scheme + "://" + host;
            String path = data.getPath();
            if (!TextUtils.isEmpty(path))
                if (path.charAt(0) == '/') data = Uri.parse(prefix + path);
                else data = Uri.parse(prefix + '/' + path);
            else data = Uri.parse(prefix);
            intent.setData(data);
        }

        return getIntentForURI(context, data);
    }

    static private Intent getIntentForURI(@NonNull Context context, @NonNull Uri data) {
        if (HOST_GISTS.equals(data.getHost())) {
            String gist = getGistId(data);
            if (gist != null) {
                return GistsContentView.createIntent(context, gist);
            }
        } else if (HOST_DEFAULT.equals(data.getHost())) {
//            CommitMatch commit = CommitUriMatcher.getCommit(data);
//            if (commit != null) {
//                return CommitViewActivity.createIntent(commit.repository, commit.commit);
//            }
//
            Intent issueIntent = getIssueIntent(context, data);
            if (issueIntent != null) {
                return issueIntent;
            }
//
//            Repository repository = RepositoryUriMatcher.getRepository(data);
//            if (repository != null) {
//                return RepositoryViewActivity.createIntent(repository);
//            }
            String login = getUser(data);
            if (login != null) {
                return UserPagerView.createIntent(context, login);
            }
        }
        return null;
    }

    private static Intent getIssueIntent(@NonNull Context context, @NonNull Uri uri) {
        List<String> segments = uri.getPathSegments();
        if (segments == null || segments.size() < 4) return null;
        if (!"issues".equals(segments.get(2)) && !"pull".equals(segments.get(2))) return null;
        String owner = segments.get(0);
        String repo = segments.get(1);
        String number = segments.get(3);
        if (TextUtils.isEmpty(number))
            return null;
        int issueNumber;
        try {
            issueNumber = Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            return null;
        }
        if (issueNumber < 1) return null;
        return IssuesDetailsView.createIntent(context, repo, owner, issueNumber);
    }
}
