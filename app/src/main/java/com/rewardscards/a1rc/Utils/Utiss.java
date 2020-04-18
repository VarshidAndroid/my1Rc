package com.rewardscards.a1rc.Utils;

import android.content.Context;
import android.widget.Toast;

public class Utiss {
    public static void showToast(Context context, String msg, int color) {
        new CToast(context).simpleToast(msg, Toast.LENGTH_SHORT)
                .setBackgroundColor(color)
                .show();
    }
}
