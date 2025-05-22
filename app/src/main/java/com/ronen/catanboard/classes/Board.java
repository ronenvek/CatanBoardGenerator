package com.ronen.catanboard.classes;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ronen.catanboard.util.Visuals;

public class Board {

    private String[] colors;
    private int[] values;
    private String[] ports;

    public Board(String[] colors, int[] values, String[] ports){
        this.colors = colors;
        this.values = values;
        this.ports = ports;
    }

    public void drawBoard(RelativeLayout layout, Context context, boolean drawPorts) {
        layout.removeAllViews();
        final int[] mapping = {7, 12, 16, 3, 8, 13, 17, 0, 4, 9, 14, 18, 1, 5, 10, 15, 2, 6, 11};

        int i = 0;

        double v_x = Math.cos(Math.toRadians(60));
        double v_y = Math.sin(Math.toRadians(60));


        DisplayMetrics displayMetrics = layout.getResources().getDisplayMetrics();
        float width = layout.getWidth();
        float height = layout.getHeight();

        float centerX = width / 2f;
        float centerY = height / 2f;

        float r = Math.min(width, height) / 2f;

        Visuals.drawHexagon(layout, context, centerX, centerY, r*2f, "background", 0);

        float hexagonSize = r/3f;
        double hexagon_dist = hexagonSize * Math.cos(Math.toRadians(30)) / 1.85f;


        int rings = 3;

        for (int u = -(rings - 1); u < rings; u++) {
            for (int v = -(rings - 1); v < rings; v++) {
                if (Math.abs(u + v) > rings - 1)
                    continue;
                double x = centerX + 2 * u * hexagon_dist + 2 * v * v_x * hexagon_dist;
                double y = centerY + 2 * v * v_y * hexagon_dist;
                Visuals.drawHexagon(layout, context, (float) x, (float) y, hexagonSize, colors[mapping[i]], values[mapping[i++]]);

            }
        }

        if (!drawPorts)
            return;

        float[] anglesDeg = { -90, -30, 30, 90, 150, 210 };
        double portRadius = r * 0.85;
        for (int j = 0; j < 6; j++) {
            double angleRad = Math.toRadians(anglesDeg[j]);
            float px = centerX + (float)(portRadius * Math.cos(angleRad));
            float py = centerY + (float)(portRadius * Math.sin(angleRad));

            TextView portLabel = new TextView(context);

            float angle = anglesDeg[j]+90;

            String port = ports[j];
            if (angle == 180 || angle == 240 || angle == 120){
                angle -= 180;
                String[] portTypes = port.split("\\|");
                port = portTypes[2] + "|" + portTypes[1] + "|" + portTypes[0];
            }

            String cleanPort = port.replace("|", "   ").replace("3", "3:1");

            portLabel.setText(cleanPort);
            portLabel.setTextSize(hexagonSize / 10);
            portLabel.setTextColor(Color.DKGRAY);
            portLabel.setGravity(Gravity.CENTER);

            portLabel.setRotation(angle);
            portLabel.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            int w = portLabel.getMeasuredWidth();
            int h = portLabel.getMeasuredHeight();

            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(w, h);

            lp.leftMargin = (int)(px - (w / 2f));
            lp.topMargin  = (int)(py - (h / 2f));
            lp.leftMargin = Math.max(0, Math.min(lp.leftMargin, displayMetrics.widthPixels - lp.width));
            lp.topMargin  = Math.max(0, Math.min(lp.topMargin, displayMetrics.heightPixels - lp.height));

            portLabel.setLayoutParams(lp);

            layout.addView(portLabel);
        }

    }

    public String[] getColors() {
        return colors;
    }
    public int[] getValues(){
        return values;
    }
    public String[] getPorts(){
        return ports;
    }

}
