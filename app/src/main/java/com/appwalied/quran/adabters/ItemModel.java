package com.appwalied.quran.adabters;

public class ItemModel {
    private String name;
    private String theader;

    public ItemModel(String name, String theader) {
        this.name = name;
        this.theader = theader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheader() {
        return theader;
    }

    public void setTheader(String theader) {
        this.theader = theader;
    }
}
