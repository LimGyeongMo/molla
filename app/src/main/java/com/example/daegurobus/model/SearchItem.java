package com.example.daegurobus.model;

public class SearchItem {

    private String rowMain;
    private String rowSub;

    public SearchItem(String rowMain, String rowSub){
        this.rowMain = rowMain ;
        this.rowSub = rowSub ;
    }

    public String getRowMain() {
        return rowMain;
    }

    public String getRowSub() {
        return rowSub;
    }

}
