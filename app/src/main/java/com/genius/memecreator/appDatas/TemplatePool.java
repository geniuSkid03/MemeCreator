package com.genius.memecreator.appDatas;

public class TemplatePool {

    private String tName;
    private String tUrl;
    private int tCount;

    public TemplatePool(String tName, String tUrl, int tCount) {
        this.tName = tName;
        this.tUrl = tUrl;
        this.tCount = tCount;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettUrl() {
        return tUrl;
    }

    public void settUrl(String tUrl) {
        this.tUrl = tUrl;
    }

    public int gettCount() {
        return tCount;
    }

    public void settCount(int tCount) {
        this.tCount = tCount;
    }
}
