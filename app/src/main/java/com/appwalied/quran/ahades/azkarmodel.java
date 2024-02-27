package com.appwalied.quran.ahades;

public class azkarmodel {

    private int id;
    private String name;
    private String title;
    private String fadl;
    private String count;

    public azkarmodel(int id, String name, String title, String fadl, String count) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.fadl = fadl;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFadl() {
        return fadl;
    }

    public void setFadl(String fadl) {
        this.fadl = fadl;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public azkarmodel() {
    }
}