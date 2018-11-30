package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.genius.memecreator.R;

public class stickersDialog extends Dialog {

    private StickerClickListener stickerClickListener;

    public stickersDialog(Context context, StickerClickListener stickerClickListener) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        View stickerView = LayoutInflater.from(context).inflate(R.layout.sticker_item_view, null);

        setContentView(stickerView);
        setCancelable(false);
    }


    public interface StickerClickListener {
        void onStickerClicked();
    }
}
