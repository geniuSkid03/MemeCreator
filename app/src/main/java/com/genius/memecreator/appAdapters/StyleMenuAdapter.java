package com.genius.memecreator.appAdapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.DataStorage;
import com.genius.memecreator.appDatas.EditingMenus;
import com.genius.memecreator.appUtils.Keys;
import com.genius.memecreator.appViews.EditingOptionsView;

import java.util.ArrayList;

public class StyleMenuAdapter extends RecyclerView.Adapter<StyleMenuAdapter.StyleMenuOptions> {

    private ArrayList<EditingMenus> styleMenusArrayList;
    private Context context;
    private DataStorage dataStorage;

    public StyleMenuAdapter(Context context, ArrayList<EditingMenus> styleMenusArrayList, DataStorage dataStorage) {
        this.context = context;
        this.styleMenusArrayList = styleMenusArrayList;
        this.dataStorage = dataStorage;
    }

    @NonNull
    @Override
    public StyleMenuOptions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StyleMenuOptions(new EditingOptionsView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull StyleMenuOptions holder, int position) {
        holder.setStyleMenus(styleMenusArrayList.get(position));
        holder.setPosition(position);

        setItemAsSelected(holder, position);
    }

    @Override
    public int getItemCount() {
        return styleMenusArrayList.size();
    }

    private void setItemAsSelected(StyleMenuOptions holder, int position) {
        if (styleMenusArrayList.get(position).isMenuSelected()) {
            holder.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
            holder.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.material_grey_500), PorterDuff.Mode.SRC_IN);
            holder.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.material_grey_500));
        }
    }

    public void setSelected(int pos) {
        try {
            if (styleMenusArrayList.size() > 1) {
                styleMenusArrayList.get(dataStorage.getInt(Keys.STYLES_MENU)).setMenuSelected(false);
                dataStorage.saveInt(Keys.STYLES_MENU, pos);
            }
            styleMenusArrayList.get(pos).setMenuSelected(true);

            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class StyleMenuOptions extends RecyclerView.ViewHolder {
        private EditingMenus editingMenus;
        private View view;
        private int position;
        private LinearLayout indicatorLayout;
        private ImageView drawableIv;
        private TextView menuNameTv;

        StyleMenuOptions(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setStyleMenus(EditingMenus editingMenus) {
            this.editingMenus = editingMenus;
            setUpView();
        }

        void setPosition(int position) {
            this.position = position;
        }

        private void setUpView() {
            drawableIv = view.findViewById(R.id.edit_icon);
            drawableIv.setImageDrawable(ContextCompat.getDrawable(context, this.editingMenus.getMenuIcon()));

            menuNameTv = view.findViewById(R.id.edit_name);
            menuNameTv.setText(editingMenus.getMenuName());

            indicatorLayout = view.findViewById(R.id.indicator_view);
        }
    }
}

