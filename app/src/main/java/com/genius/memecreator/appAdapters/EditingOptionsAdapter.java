package com.genius.memecreator.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.DataStorage;
import com.genius.memecreator.appDatas.EditingMenus;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.Keys;
import com.genius.memecreator.appViews.EditingOptionsView;

import java.util.ArrayList;

public class EditingOptionsAdapter extends RecyclerView.Adapter<EditingOptionsAdapter.EditingOptions> {

    private ArrayList<EditingMenus> editingMenusArrayList;
    private Context context;
    private DataStorage dataStorage;

    public EditingOptionsAdapter(Context context, ArrayList<EditingMenus> editingMenusArrayList, DataStorage dataStorage) {
        this.context = context;
        this.editingMenusArrayList = editingMenusArrayList;
        this.dataStorage = dataStorage;
        AppHelper.print("Editing options adapter : constructor");
    }

    @NonNull
    @Override
    public EditingOptions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EditingOptions(new EditingOptionsView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull EditingOptions holder, final int position) {
        holder.setEditingMenus(editingMenusArrayList.get(position));
        holder.setPosition(position);

//        if (editingMenusArrayList.get(position).isMenuSelected()) {
//            holder.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
//            holder.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.white));
//        } else {
//            holder.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//            holder.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        }
//        setMenuAsSelected(holder, position);
    }

    public void changeImage(int index) {
        editingMenusArrayList.get(index).setMenuSelected(true);
        notifyItemChanged(index);
    }

    @Override
    public int getItemCount() {
        return editingMenusArrayList.size();
    }

//    private void setMenuAsSelected(EditingOptions editingOptions, int positoin) {
//        if (editingMenusArrayList.get(positoin).isMenuSelected()) {
//            editingOptions.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
//            editingOptions.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//        } else {
//            editingOptions.drawableIv.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//            editingOptions.menuNameTv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        }
//    }

    public void setSelected(int position) {
        try {

            if (editingMenusArrayList.size() > 1) {
                editingMenusArrayList.get(dataStorage.getInt(Keys.EDITING_MENU)).setMenuSelected(false);
                dataStorage.saveInt(Keys.EDITING_MENU, position);
            }
            editingMenusArrayList.get(position).setMenuSelected(true);

            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class EditingOptions extends RecyclerView.ViewHolder {

        private EditingMenus editingMenus;
        private View view;
        private int position;
        private ImageView drawableIv;
        private TextView menuNameTv;

        EditingOptions(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setEditingMenus(EditingMenus editingMenus) {
            this.editingMenus = editingMenus;
            setUpView();
        }

        void setPosition(int position) {
            this.position = position;
        }

        private void setUpView() {
            drawableIv = view.findViewById(R.id.edit_icon);
            drawableIv.setImageDrawable(ContextCompat.getDrawable(context, editingMenus.getMenuIcon()));

            menuNameTv = view.findViewById(R.id.edit_name);
            menuNameTv.setText(editingMenus.getMenuName());
        }
    }

}
