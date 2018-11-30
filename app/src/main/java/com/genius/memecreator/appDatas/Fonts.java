package com.genius.memecreator.appDatas;

import android.graphics.Typeface;

public class Fonts {

    private String fontName;
    private Typeface typeface;

    public Fonts(String fontName, Typeface typeface) {
        this.fontName = fontName;
        this.typeface = typeface;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
