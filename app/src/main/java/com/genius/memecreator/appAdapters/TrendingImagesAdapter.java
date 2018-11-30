package com.genius.memecreator.appAdapters;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.TrendingMemes;
import com.genius.memecreator.appInterfaces.TrendingImageClickListener;
import com.genius.memecreator.appViews.ImageHolderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingImagesAdapter extends RecyclerView.Adapter<TrendingImagesAdapter.ImagesHolder>  {

    private ArrayList<TrendingMemes> trendingMemesArrayList, filteredTrendingMemesList;
    private Context context;
    private TrendingImageClickListener trendingImageClickListener;
    private int screenWidth;

    public TrendingImagesAdapter(Context context, ArrayList<TrendingMemes> trendingMemesArrayList) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        this.trendingMemesArrayList = trendingMemesArrayList;
        filteredTrendingMemesList = trendingMemesArrayList;
        this.context = context;
    }

    public void setTrendingImageClickListener(TrendingImageClickListener trendingImageClickListener) {
        this.trendingImageClickListener = trendingImageClickListener;
    }

    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesHolder(new ImageHolderView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder holder, int position) {
        holder.setImages(trendingMemesArrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return trendingMemesArrayList.size();
    }

    class ImagesHolder extends RecyclerView.ViewHolder {
        private TrendingMemes trendingMemes;
        private View view;
        private ImageView trendingIv, shareIv, editIv, favIv;
        private int position;


        ImagesHolder(View view) {
            super(view);
            this.view = view;
        }

        void setImages(TrendingMemes trendingMemes, int position) {
            this.trendingMemes = trendingMemes;
            this.position = position;
            setView();
        }

        private void setView() {
            trendingIv = view.findViewById(R.id.image);
            shareIv = view.findViewById(R.id.share);
            editIv = view.findViewById(R.id.edit);
            favIv = view.findViewById(R.id.favourite);

//            favIv.setImageDrawable(trendingMemes.isFav() == 1 ?
//                    ContextCompat.getDrawable(context, R.drawable.ic_favorite_icon_selected) :
//                    ContextCompat.getDrawable(context, R.drawable.ic_favorite_unselected));

            Picasso.get()
                    .load(trendingMemes.getImgUrl())
                    .error(R.drawable.ic_broken_image)
                    .resize(screenWidth / 2, 250)
                    .into(trendingIv);

            shareIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trendingImageClickListener.onShareClicked(trendingIv, position);
                }
            });
            editIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trendingImageClickListener.onEditClicked(position);
                }
            });
//            favIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    trendingImageClickListener.onFavClicked(position);
//                }
//            });
        }
    }
}
