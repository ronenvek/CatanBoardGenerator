package com.ronen.catanboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.ronen.catanboard.classes.Board;
import com.ronen.catanboard.classes.SaveData;
import com.ronen.catanboard.util.Data;
import com.ronen.catanboard.util.Preferences;
import com.ronen.catanboard.util.Util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;
    List<String> data = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardhistory);

        EdgeToEdge.enable(this);

        Preferences.init(this);
        String g = Preferences.getPrefs().getString("board", null);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (g != null){
            toolbar.setNavigationOnClickListener(v -> Util.switchActivities(BoardActivity.class, this));;
        }
        else{
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
        findViewById(R.id.dividerToolbar).setBackgroundColor(Util.resolveAttrColor(this, com.google.android.material.R.attr.colorOnSurface));



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListAdapter(data, true, item -> {
            if (item.equals("No prior boards found"))
                return;
            if (item.equals("new")){
                Preferences.getEditor().putString("board", getCurrentDate());
                Preferences.save();
                Util.switchActivities(BoardActivity.class, this);
            }
            else{
                Preferences.getEditor().putString("board", item);
                Preferences.save();
                Util.switchActivities(BoardActivity.class, this);
            }
        }, item -> {
            if (item.equals(g)){
                Preferences.getEditor().putString("board", null);
                Preferences.save();
                toolbar.setNavigationIcon(null);
                toolbar.setNavigationOnClickListener(null);
            }
            List<SaveData> sdl = Data.read(this);
            int i = 0;
            for (SaveData sd : sdl){
                if (sd.getDate().equals(item)){
                    sdl.remove(i);
                    break;
                }
                i++;
            }
            Data.save(this, sdl);
            update();
        });
        recyclerView.setAdapter(adapter);

        data.add("Loading board history...");
        adapter.notifyDataSetChanged();

        Preferences.init(this);
        update();
      //  Data.clear(this);
    }


    public void update(){
        List<String> l = new ArrayList<>();
        List<SaveData> boards = Data.read(this);
        for (SaveData b : boards){
            l.add(b.getDate());
        }
        setList(l);
    }

    private void setList(List<String> l) {
        runOnUiThread(() -> {
            data.clear();

            List<String> sorted = new ArrayList<>(l);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            sorted.sort((a, b) -> {
                try {
                    String dateAString = a.length() >= 10 ? a.substring(0, 10) : "01.01.1970";
                    String dateBString = b.length() >= 10 ? b.substring(0, 10) : "01.01.1970";

                    LocalDate dateA = LocalDate.parse(dateAString, formatter);
                    LocalDate dateB = LocalDate.parse(dateBString, formatter);

                    int dateCompare = dateB.compareTo(dateA);
                    if (dateCompare != 0) return dateCompare;

                    int suffixA = a.length() > 10 ? a.charAt(11) -48 : 0;
                    int suffixB = b.length() > 10 ? b.charAt(11) -48 : 0;

                    return Integer.compare(suffixB, suffixA);

                } catch (Exception e) {
                    return 0;
                }
            });

            data.add("newNODELETE");
            data.addAll(sorted);

            if (sorted.isEmpty())
                data.add("No prior boards foundNODELETE");

            adapter.notifyDataSetChanged();
        });
    }

    public String getCurrentDate(){
        LocalDate now = LocalDate.now();

        String date = String.format("%02d", now.getDayOfMonth()) + "." + String.format("%02d", now.getMonthValue()) + "." + now.getYear();
        Set<String> names = new HashSet<>();
        for (SaveData b : Data.read(this)) {
            names.add(b.getDate());
        }
        return getCurrentDate(date, date, 0, names);
    }

    private String getCurrentDate(String base, String game, int id, Set<String> dates){
        if (!dates.contains(game))
            return game;
        return getCurrentDate(base, base + "(" + (id + 1) + ")", id + 1, dates);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //Util.switchActivities(Boar.class, this);
    }

}
