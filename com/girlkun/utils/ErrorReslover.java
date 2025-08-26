package com.girlkun.utils;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;

public class ErrorReslover {

    public static void howToFix(String error) {
        try {
            String query = "https://chatgpt.com/?q=" + URLEncoder.encode(error, "UTF-8");
            URI uri = new URI(query);
            Desktop.getDesktop().browse(uri);

        } catch (Exception e) {

        }
    }
}
