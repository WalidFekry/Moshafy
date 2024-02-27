package com.appwalied.quran.sonan;


public class item_Aya {
    private String name;
    private String title;
    private int id;
    private int num;

    public item_Aya(String name, String title, int id, int num) {
        this.name = name;
        this.title = title;
        this.id = id;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
