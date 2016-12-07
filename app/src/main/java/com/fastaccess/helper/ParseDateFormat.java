package com.fastaccess.helper;

import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class ParseDateFormat {
    private static final String TAG = "ParseDateFormat";

    private static final ParseDateFormat INSTANCE = new ParseDateFormat();

    public static ParseDateFormat getInstance() {
        return INSTANCE;
    }

    private final Object lock = new Object();

    private final DateFormat dateFormat;

    private ParseDateFormat() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(new SimpleTimeZone(0, "GMT"));
        dateFormat = format;
    }

    public Date parse(String dateString) {
        synchronized (lock) {
            try {
                return dateFormat.parse(dateString);
            } catch (java.text.ParseException e) {
                Logger.e(TAG, "could not parse date: " + dateString, e);
                return null;
            }
        }
    }

    public String format(Date date) {
        synchronized (lock) {
            return dateFormat.format(date);
        }
    }

    public static CharSequence getTimeAgo(@Nullable Date parsedDate) {
        if (parsedDate != null) {
            return DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS);
        }
        return "N/A";
    }

    public static CharSequence getTimeAgo(@Nullable String date) {
        Date parsedDate = getInstance().parse(date);
        if (parsedDate != null) {
            return DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS);
        }
        return "N/A";
    }
}