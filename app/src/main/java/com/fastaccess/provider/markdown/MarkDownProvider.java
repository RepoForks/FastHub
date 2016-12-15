package com.fastaccess.provider.markdown;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.IntStream;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.InputHelper;
import com.zzhoujay.richtext.RichText;

/**
 * Created by Kosh on 24 Nov 2016, 7:43 PM
 */

public class MarkDownProvider {
    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif"};
    private static final String[] MARKDOWN_EXTENSIONS = {
            ".md", ".mkdn", ".mdwn", ".mdown", ".markdown", ".mkd", ".mkdown", ".ron", ".rst"
    };

    private static final String[] ARCHIVE_EXTENSIONS = {
            ".zip", ".7z", ".rar", ".tar.gz", ".tgz", ".tar.Z", ".tar.bz2", ".tbz2", ".tar.lzma", ".tlz", ".apk", ".jar", ".dmg"
    };

    private MarkDownProvider() {}

    public static RichText convertTextToMarkDown(@NonNull TextView textView, @NonNull String text) {
        RichText richText = RichText.fromMarkdown(text)
                .clickable(true)
                .fix(holder -> {
                    if (holder.isGif()) {
                        holder.setAutoFix(true);
                        holder.setAutoPlay(true);
                    }

                })
                .urlClick(url -> {
                    ActivityHelper.startCustomTab((Activity) textView.getContext(), Uri.parse(url));
                    return true;
                });
        richText.into(textView);
        return richText;
    }

    public static void addList(@NonNull EditText editText, @NonNull String list) {
        String tag = list + " ";
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(0, selectionStart);
        int line = substring.lastIndexOf(10);
        if (line != -1) {
            selectionStart = line + 1;
        } else {
            selectionStart = 0;
        }
        substring = source.substring(selectionStart, selectionEnd);
        String[] split = substring.split("\n");
        StringBuilder stringBuffer = new StringBuilder();
        if (split.length > 0)
            for (String s : split) {
                if (s.length() == 0 && stringBuffer.length() != 0) {
                    stringBuffer.append("\n");
                    continue;
                }
                if (!s.trim().startsWith(tag)) {
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(tag).append(s);
                } else {
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(s);
                }
            }

        if (stringBuffer.length() == 0) {
            stringBuffer.append(tag);
        }
        editText.getText().replace(selectionStart, selectionEnd, stringBuffer.toString());
        editText.setSelection(stringBuffer.length() + selectionStart);

    }

    public static void addHeader(@NonNull EditText editText, int level) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        StringBuilder result = new StringBuilder();
        String substring = source.substring(selectionStart, selectionEnd);
        if (!hasNewLine(source, selectionStart))
            result.append("\n");
        IntStream.range(0, level).forEach(integer -> result.append("#"));
        result.append(" ").append(substring);
        editText.getText().replace(selectionStart, selectionEnd, result.toString());
        editText.setSelection(selectionStart + result.length());

    }

    public static void addItalic(@NonNull EditText editText) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = "_" + substring + "_ ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 2);

    }

    public static void addBold(@NonNull EditText editText) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = "__" + substring + "__ ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 3);

    }

    public static void addCode(@NonNull EditText editText) {
        try {
            String source = editText.getText().toString();
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            String substring = source.substring(selectionStart, selectionEnd);
            String result;
            if (hasNewLine(source, selectionStart))
                result = "```\n" + substring + "\n```\n";
            else
                result = "\n```\n" + substring + "\n```\n";

            editText.getText().replace(selectionStart, selectionEnd, result);
            editText.setSelection(result.length() + selectionStart - 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addStrikeThrough(@NonNull EditText editText) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = "~~" + substring + "~~ ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 3);

    }

    public static void addQuote(@NonNull EditText editText) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "> " + substring;
        } else {
            result = "\n> " + substring;

        }
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart);

    }

    public static void addDivider(@NonNull EditText editText) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "-------\n";
        } else {
            result = "\n-------\n";
        }
        editText.getText().replace(selectionStart, selectionStart, result);
        editText.setSelection(result.length() + selectionStart);

    }

    public static void addPhoto(@NonNull EditText editText) {
        int selectionStart = editText.getSelectionStart();
        String result = "![]()\n";
        int length = selectionStart + result.length();
        editText.getText().insert(selectionStart, result);
        editText.setSelection(length - 2);
    }

    public static void addLink(@NonNull EditText editText) {
        int selectionStart = editText.getSelectionStart();
        String result = "[]()\n";
        int length = selectionStart + result.length();
        editText.getText().insert(selectionStart, result);
        editText.setSelection(length - 2);
    }

    private static boolean hasNewLine(@NonNull String source, int selectionStart) {
        try {
            if (source.isEmpty()) return true;
            source = source.substring(0, selectionStart);
            return source.charAt(source.length() - 1) == 10;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean isImage(@Nullable final String name) {
        if (InputHelper.isEmpty(name)) return false;
        for (String extension : IMAGE_EXTENSIONS)
            if (name.toLowerCase().endsWith(extension)) return true;

        return false;
    }

    public static boolean isMarkdown(@Nullable String name) {
        if (InputHelper.isEmpty(name)) return false;
        name = name.toLowerCase();
        for (String extension : MARKDOWN_EXTENSIONS)
            if (name.endsWith(extension) || name.equals("README")) return true;

        return false;
    }

    public static boolean isArchive(@Nullable String name) {
        if (InputHelper.isEmpty(name)) return false;
        name = name.toLowerCase();
        for (String extension : ARCHIVE_EXTENSIONS)
            if (name.endsWith(extension)) return true;

        return false;
    }
}
