package com.appwalied.quran.quran.qouran_learning.models;

import androidx.annotation.Keep;

@Keep
public class Sora {

    private int startPageNum;
    private String name;

    public Sora() {
    }

    public Sora(int startPageNum, String name) {
        this.startPageNum = startPageNum;
        this.name = name;
    }

    public int getStartPageNum() {
        return startPageNum;
    }

    public void setStartPageNum(int startPageNum) {
        this.startPageNum = startPageNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
