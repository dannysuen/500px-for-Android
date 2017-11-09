package me.dannysuen.fivehundredpx.util;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {

    // Load selected categories from a local json file in assets
    public static String loadJsonStringFromAssets(Context context, String filenameInAssets) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filenameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
