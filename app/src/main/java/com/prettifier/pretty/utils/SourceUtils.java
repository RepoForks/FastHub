package com.prettifier.pretty.utils;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public class SourceUtils {

    private final static String HTML_CONTENT =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <link rel=\"stylesheet\" href=\"./styles/%s\">\n" +
                    "</head>\n" +
                    "<body onload=\"prettyPrint()\">\n" +
                    "<pre class=\"prettyprint linenums\">%s</pre>\n" +
                    "<script src=\"./js/prettify.js\"></script>\n" +
                    "</body>\n" +
                    "</html>";


    public static String generateContent(String source) {
        return String.format(HTML_CONTENT, getStyle(), source);
    }

    private static String getStyle() {
        return "prettify.css";
    }

}
