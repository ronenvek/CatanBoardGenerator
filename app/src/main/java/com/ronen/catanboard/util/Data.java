package com.ronen.catanboard.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ronen.catanboard.classes.Board;
import com.ronen.catanboard.classes.SaveData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private final static String file = "boards.json";

    public static void saveBoard(Context context, Board board, String time) {
        List<SaveData> boards = read(context); // Read existing list
        boards.add(new SaveData(board, time)); // Add new board

        save(context, boards);
    }

    public static void save(Context context, List<SaveData> boards){
        Gson gson = new Gson();
        String json = gson.toJson(boards);

        try (FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SaveData getBoard(Context context, String date) {
        for (SaveData sd : Data.read(context)) {
            if (sd.getDate().equals(date))
                return sd;
        }
        return null;
    }

    public static List<SaveData> read(Context context) {
        try (FileInputStream fis = context.openFileInput(file);
             InputStreamReader reader = new InputStreamReader(fis)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<SaveData>>(){}.getType();

            return gson.fromJson(reader, listType);

        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void clear(Context context) {
        try (FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {

            writer.write("[]"); // Empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
