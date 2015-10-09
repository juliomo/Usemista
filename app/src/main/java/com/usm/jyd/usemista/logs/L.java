package com.usm.jyd.usemista.logs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by der_w on 10/8/2015.
 */
public class L {
    public static void m(String message) {
        Log.d("Der ", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}