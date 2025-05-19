package com.ronen.catanboard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class GenerateBoard {

    public static String[] generateColors(){
        String[] tilesArr = {
                "robber",
                "brick", "brick", "brick",
                "ore", "ore", "ore",
                "wheat", "wheat", "wheat", "wheat",
                "sheep", "sheep","sheep","sheep",
                "wood","wood","wood","wood"};
        ArrayList<String> tiles = new ArrayList<>(Arrays.asList(tilesArr));


        String[] board = new String[19];
        for (int i = 0; i < 19; i++) {
            int random = (int)(Math.random() * tiles.size());
            board[i] = tiles.get(random);
            tiles.remove(random);
        }
        return board;
    }

    public static int[] generateNumbers(String[] board){
        Integer[] numbersArr = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(numbersArr));

        HashMap<String, HashSet<Integer>> blocked = new HashMap<>();


        int[] values = new int[19];
        for (int i = 0; i < 19; i++) {
            if (board[i].equals("robber"))
                continue;

            int[] coords = intToCoords(i);
            HashSet<Integer> unavailable = blocked.get(Arrays.toString(coords));
            ArrayList<Integer> poss = new ArrayList<>(numbers);

            if (unavailable != null)
                poss.removeAll(unavailable);

            if (poss.isEmpty())
                return generateNumbers(board);

            int random = (int)(Math.random() * poss.size());
            int val = poss.get(random);
            values[i] = val;
            numbers.remove(Integer.valueOf(val));

            addBlock(coords[0], coords[1] + 1, blocked, val);
            addBlock(coords[0] + 1, coords[1], blocked, val);
            addBlock(coords[0] + 1, coords[1] + 1, blocked, val);
        }

        return values;
    }

    public static String[] generatePorts(){
        String[] options = {" |ore| ", "3| |brick", " |wood| ", "3| |wheat", " |3| ", "3| |sheep"};
        ArrayList<String> poss = new ArrayList<>(Arrays.asList(options));
        String[] ports = new String[6];
        for (int i = 0; i < 6; i++){
            int random = (int)(Math.random() * poss.size());
            ports[i] = poss.get(random);
            poss.remove(random);
        }
        return ports;
    }


    private static void addBlock(int y, int x, HashMap<String, HashSet<Integer>> blocked, int val){
        String coords = "["+y + ", " + x + "]";
        HashSet<Integer> block = blocked.get(coords);
        if (block == null)
            block = new HashSet<>();
        block.add(val);

        if (val == 6)
            block.add(8);
        else if (val == 8)
            block.add(6);

        blocked.put(coords, block);
    }


    private static int[] intToCoords(int i){
        if (i < 3)
            return new int[]{0, i};
        if (i < 7)
            return new int[]{1, i - 3};
        if (i < 12)
            return new int[]{2, i - 7};
        if (i < 16)
            return new int[]{3, i - 11};
        return new int[]{4, i - 14};
    }

}
