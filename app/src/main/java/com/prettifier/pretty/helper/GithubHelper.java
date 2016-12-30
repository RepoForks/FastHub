package com.prettifier.pretty.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public class GithubHelper {
    private static Matcher IMAGE_TAG_MATCHER = Pattern.compile("<(img|IMG)(.*?)>").matcher("");

    private static Matcher IMAGE_SRC_MATCHER = Pattern.compile("(src|SRC)=\"(.*?)\"").matcher("");

    @NonNull public static String generateContent(@NonNull String source, @Nullable String baseUrl) {
        if (baseUrl == null) {
            return mergeContent(source);
        } else {
            return mergeContent(validateImageBaseUrl(source, baseUrl));
        }
    }

    /**
     * A hacking method that appends the https://raw.githubusercontent.com/ to given image src.
     *
     * @param source
     * @param baseUrl
     * @return full image url.
     */
    @NonNull private static String validateImageBaseUrl(@NonNull String source, @NonNull String baseUrl) {
        Matcher matcher = IMAGE_TAG_MATCHER.reset(source);
        while (matcher.find()) {
            String image = matcher.group(2).trim();
            IMAGE_SRC_MATCHER.reset(image);
            String src = null;
            if (IMAGE_SRC_MATCHER.find()) {
                src = IMAGE_SRC_MATCHER.group(2).trim();
            }
            if (src == null || src.startsWith("http://") || src.startsWith("https://")) {
                continue;
            }
            Uri uri = Uri.parse(baseUrl);
            List<String> segments = uri.getPathSegments();
            if (segments == null || segments.size() < 2) break;
            String owner = segments.get(0);
            String repoName = segments.get(1);
            String finalSrc = "https://raw.githubusercontent.com/" + owner + "/" + repoName + "/master/" + src;
            source = source.replace(src, finalSrc);
        }
        return source;
    }

    private static String mergeContent(@NonNull String source) {
        return "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./github.css\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                source +
                "\n<script src=\"./intercept-touch.js\"></script>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
    }

}
