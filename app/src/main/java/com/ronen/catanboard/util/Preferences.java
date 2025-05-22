package com.ronen.catanboard.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {

    public static void init(Context context){
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static SharedPreferences getPrefs(){
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor(){
        if (editor != null)
            save();
        editor = sharedPreferences.edit();
        return editor;
    }

    public static void save(){
        editor.apply();
    }

}
