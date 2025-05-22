package com.ronen.catanboard;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.ronen.catanboard.classes.Board;
import com.ronen.catanboard.classes.SaveData;
import com.ronen.catanboard.util.Data;
import com.ronen.catanboard.util.GenerateBoard;
import com.ronen.catanboard.util.Preferences;
import com.ronen.catanboard.util.Util;


public class BoardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v-> Util.switchActivities(BoardHistory.class, this));

        RelativeLayout mainLayout = findViewById(R.id.main);

        Preferences.init(this);
        String g = Preferences.getPrefs().getString("board", null);
        if (g == null){
            Util.showToast(this, "An unexpected error occured!");
            Util.switchActivities(BoardHistory.class, this);
            return;
        }
        toolbar.setTitle(g);
        findViewById(R.id.dividerToolbar).setBackgroundColor(Util.resolveAttrColor(this));

        String[] colors = GenerateBoard.generateColors();
        int[] values = GenerateBoard.generateNumbers(colors);
        String[] ports = GenerateBoard.generatePorts();
        Board b = new Board(colors, values, ports);

        SaveData sd = Data.getBoard(this, g);
        if (sd != null)
            b = sd.getBoard();
        else
            Data.saveBoard(this, b, g);

        Board finalB = b;
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finalB.drawBoard(mainLayout, BoardActivity.this, true);
            }
        });

    }

}