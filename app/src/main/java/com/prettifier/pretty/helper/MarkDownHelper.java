package com.prettifier.pretty.helper;

import android.support.annotation.NonNull;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public class MarkDownHelper {

    @NonNull public static String generateContent(@NonNull String source) {
        return mergeContent(source);
    }

    private static String mergeContent(@NonNull String source) {
        return "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link rel=\"stylesheet\" href=\"./bootstrap.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./md.css\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "        <div id=\"renderer\" class=\"hidden\">" + source + "</div> \n" +
                "        <div class=\"container\">\n" +
                "            <div id=\"md\" class=\"col-md-12\"></div>\n" +
                "        </div>\n" +
                "    <script src=\"./markdown-it.js\"></script>\n" +
                "    <script src=\"./intercept-touch.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "    var md = window.markdownit({\n" +
                "        html: true,\n" +
                "        linkify: true,\n" +
                "        typographer: true\n" +
                "    });\n" +
                "    var result = md.render(document.getElementById('renderer').innerHTML);\n" +
                "    document.getElementById('md').innerHTML = result;\n" +
                "    document.getElementById('renderer').innerHTML = \"\" \n" +
                "    </script>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
    }

}
