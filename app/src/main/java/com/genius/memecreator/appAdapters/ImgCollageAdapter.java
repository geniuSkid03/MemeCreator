package com.genius.memecreator.appAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.genius.memecreator.R;

public class ImgCollageAdapter extends BaseAdapter {

    private Context context;
    private Integer[] gridIds;
    private OnGridItemClicked onGridItemClicked;
    private ImageView gridItemView;

    public ImgCollageAdapter(Context context, Integer[] gridIds, OnGridItemClicked onGridItemClicked) {
        this.context = context;
        this.gridIds = gridIds;
        this.onGridItemClicked = onGridItemClicked;
    }

    @Override
    public int getCount() {
        return gridIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
        }

        gridItemView = convertView.findViewById(R.id.grid_image);

        gridItemView.setImageResource(gridIds[position]);

        gridItemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onGridItemClicked.itemClicked(position);
            }
        });

        return convertView;
    }

    public interface OnGridItemClicked {
        void itemClicked(int position);
    }
}
