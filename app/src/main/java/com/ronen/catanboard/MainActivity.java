package com.ronen.catanboard;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MainActivity extends AppCompatActivity {

    public static MainActivity app;
    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);


        refreshLayout = findViewById(R.id.refresh);
        RelativeLayout mainLayout = findViewById(R.id.main);

       refreshLayout.setOnRefreshListener(() -> drawBoard(mainLayout));

        drawBoard(mainLayout);
    }

    private void drawBoard(RelativeLayout layout) {
        layout.removeAllViews();
        final int[] mapping = {7, 12, 16, 3, 8, 13, 17, 0, 4, 9, 14, 18, 1, 5, 10, 15, 2, 6, 11};


        String[] board = GenerateBoard.generateColors();
        int[] values = GenerateBoard.generateNumbers(board);
        int i = 0;

        double v_x = Math.cos(Math.toRadians(60));
        double v_y = Math.sin(Math.toRadians(60));


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float centerX = displayMetrics.widthPixels / 2f;
        float centerY = displayMetrics.heightPixels / 2f;

        float r = Math.min(centerX, centerY);

        Hexagon.drawHexagon(layout, centerX, centerY, r*2f, "background", 0);

        float hexagonSize = r/3f;
        double hexagon_dist = hexagonSize * Math.cos(Math.toRadians(30)) / 1.85f;


        int rings = 3;

        for (int u = -(rings - 1); u < rings; u++) {
            for (int v = -(rings - 1); v < rings; v++) {
                if (Math.abs(u + v) > rings - 1)
                    continue;
                double x = centerX + 2 * u * hexagon_dist + 2 * v * v_x * hexagon_dist;
                double y = centerY + 2 * v * v_y * hexagon_dist;
                Hexagon.drawHexagon(layout, (float) x, (float) y, hexagonSize, board[mapping[i]], values[mapping[i++]]);

            }
        }
        refreshLayout.setRefreshing(false);
    }
}