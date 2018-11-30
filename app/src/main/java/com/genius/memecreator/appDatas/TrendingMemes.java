package com.genius.memecreator.appDatas;

import android.graphics.drawable.Drawable;

public class TrendingMemes {

    private int id, isFav;
    private String imgUrl, imgName;
    private Drawable imgDrawable;

    public TrendingMemes() {

    }

    public TrendingMemes(int id, String imgName, String imgUrl, int isFav) {
        this.id = id;
        this.imgName = imgName;
        this.imgUrl = imgUrl.replace("localhost", "192.168.1.7");
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int isFav() {
        return isFav;
    }

    public void setFav(int fav) {
        isFav = fav;
    }

    public Drawable getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(Drawable imgDrawable) {
        this.imgDrawable = imgDrawable;
    }
}
