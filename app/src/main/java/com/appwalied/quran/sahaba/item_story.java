package com.appwalied.quran.sahaba;

public class item_story {

    private String header;
    private String title;

    public item_story(String header, String title) {
        this.header = header;
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
