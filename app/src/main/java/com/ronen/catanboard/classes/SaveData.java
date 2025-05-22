package com.ronen.catanboard.classes;

import com.ronen.catanboard.util.Data;
import com.ronen.catanboard.util.GenerateBoard;

public class SaveData extends Board{

    private String date;
    public SaveData(Board b, String data){
        super(b.getColors(), b.getValues(), b.getPorts());
        this.date = data;
    }

    public String getDate(){
        return date;
    }

    public Board getBoard(){
        return new Board(getColors(), getValues(), getPorts());
    }

}
