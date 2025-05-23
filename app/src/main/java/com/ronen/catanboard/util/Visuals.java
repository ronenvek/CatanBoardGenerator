package com.ronen.catanboard.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ronen.catanboard.R;

public class Visuals {


    public static void drawHexagon(RelativeLayout layout, Context context, float centerX, float centerY, float size, String color, int val) {
        ImageView hexagonView = new ImageView(context);
        hexagonView.setImageDrawable(getCorrectColor(color, context));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) size,
                (int) size
        );

        params.leftMargin = (int) (centerX - size / 2);
        params.topMargin = (int) (centerY - size / 2);

        hexagonView.setLayoutParams(params);

        if (color.equals("background")){
            layout.addView(hexagonView);
            return;
        }
        hexagonView.setRotation(30.0f);
        layout.addView(hexagonView);

        if (val == 0)
            return;

        TextView valueText = new TextView(context);
        valueText.setText(String.valueOf(val));

        if (val == 6 || val == 8)
            valueText.setTextColor(0xFFAC050F);
        else
            valueText.setTextColor(Color.BLACK);

        valueText.setTextSize(size / 6);
        valueText.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                (int) size,
                (int) size
        );
        textParams.leftMargin = (int) (centerX - size / 2);
        textParams.topMargin = (int) (centerY - size / 2);
        valueText.setLayoutParams(textParams);
        layout.addView(valueText);
    }

    private static Drawable getCorrectColor(String color, Context context) {
        switch (color){
            case "background": return ContextCompat.getDrawable(context, R.drawable.hexagon_background);
            case "wood": return ContextCompat.getDrawable(context, R.drawable.hexagon_wood);
            case "robber": return ContextCompat.getDrawable(context, R.drawable.hexagon_robber);
            case "brick": return ContextCompat.getDrawable(context, R.drawable.hexagon_brick);
            case "wheat": return ContextCompat.getDrawable(context, R.drawable.hexagon_wheat);
            case "sheep": return ContextCompat.getDrawable(context, R.drawable.hexagon_sheep);
            case "ore": return ContextCompat.getDrawable(context, R.drawable.hexagon_ore);
            default: return ContextCompat.getDrawable(context, R.drawable.hexagon);
        }
    }
}
