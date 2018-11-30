package com.genius.memecreator.appAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.Fonts;

import java.util.ArrayList;

public class FontAdapter extends BaseAdapter {

    private ArrayList<String> fontNames;
    private ArrayList<Typeface> fontFace;

    private Context context;
    private OnFontClicked onFontClicked;

    public FontAdapter(Context context, ArrayList<String> fontNames, ArrayList<Typeface> fontFace, OnFontClicked onFontClicked) {
        super();

        this.context = context;
        this.fontFace = fontFace;
        this.fontNames = fontNames;
        this.onFontClicked = onFontClicked;
    }

    @Override
    public int getCount() {
        return fontNames.size();
    }

    @Override
    public Typeface getItem(int position) {
        return fontFace.get(position);
    }

    @Override
    public long getItemId(int position) {
        return fontNames.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_font_view, null);
        }

        TextView textView = convertView.findViewById(R.id.custom_font);

        textView.setText(fontNames.get(position));
        textView.setTypeface(fontFace.get(position));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFontClicked.chosenFont(new Fonts(fontNames.get(position), fontFace.get(position)));
            }
        });
       // textView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));

        return convertView;
    }

    public interface OnFontClicked {
        void chosenFont(Fonts fonts);
    }
}
