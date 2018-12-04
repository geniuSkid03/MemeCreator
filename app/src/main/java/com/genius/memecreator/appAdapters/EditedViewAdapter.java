package com.genius.memecreator.appAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.genius.memecreator.R;

public class EditedViewAdapter extends BaseAdapter {

    private Activity activity;
    private String[] filepath;
    private String[] filename;

    private ClickListener clickListener;

    private static LayoutInflater inflater = null;

    public EditedViewAdapter(Activity a, String[] fpath, String[] fname, ClickListener clickListener) {
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clickListener = clickListener;
    }

    public int getCount() {
        return filepath.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.edited_view_item, null);
        }
        ImageView image = vi.findViewById(R.id.image);
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        image.setImageBitmap(bmp);

        (vi.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onShareClicked(position);
            }
        });
        (vi.findViewById(R.id.edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEditClicked(position);
            }
        });

        return vi;
    }

    public interface ClickListener {
        void onEditClicked(int position);

        void onShareClicked(int position);
    }

}
