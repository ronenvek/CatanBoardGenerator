package com.ronen.catanboard.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.Toast;

public class Util {

    public static void switchActivities(Object page, Context context) {
        Intent switchActivityIntent = new Intent(context, (Class<?>) page);
        context.startActivity(switchActivityIntent);
    }

    public static void showToast(Activity activity, String message){
        activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
    }

    public static int resolveAttrColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
