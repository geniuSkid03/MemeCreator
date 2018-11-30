package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.genius.memecreator.R;


public class TextStylesDialog extends Dialog {

    private TextView sampleTv;
    private AppCompatButton okBtn;

    private Context context;

    private Typeface chosenTypeface;
    private int chosenTextSize, chosenTextColor, chosenTextBgColor;

    private TextStylesDialogCallback textStylesDialogCallback;

    public TextStylesDialog(Context context, final TextStylesDialogCallback textStylesDialogCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
////        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        this.context = context;

        View textStylesView = LayoutInflater.from(context).inflate(R.layout.text_styles_layout, null);

        TabHost tabsHost = textStylesView.findViewById(R.id.tabhost);
        tabsHost.setup();

        TabHost.TabSpec tabpage1 = tabsHost.newTabSpec(context.getString(R.string.text));
        tabpage1.setContent(R.id.text);
        tabpage1.setIndicator(context.getString(R.string.text));

        TabHost.TabSpec tabpage2 = tabsHost.newTabSpec(context.getString(R.string.font));
        tabpage2.setContent(R.id.font);
        tabpage2.setIndicator(context.getString(R.string.font));

        tabsHost.addTab(tabpage1);
        tabsHost.addTab(tabpage2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(textStylesView, layoutParams);

        setCancelable(true);

//        sampleTv = textStylesView.findViewById(R.id.sample_text);
//        okBtn = textStylesView.findViewById(R.id.ok_btn);
//
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOkbtnClicked(v);
//            }
//        });

        this.textStylesDialogCallback = textStylesDialogCallback;
    }

    private void onOkbtnClicked(View view) {
        textStylesDialogCallback.onFontClicked(chosenTypeface);
        textStylesDialogCallback.onTextBgColorClicked(chosenTextBgColor);
        textStylesDialogCallback.onTextColorClicked(chosenTextColor);
        textStylesDialogCallback.onTextSizeClicked(chosenTextSize);

        dismiss();
    }

    public void setNeededInfo(String topText, String bottomText) {
//        if (sampleTv != null) {
//            if (topText != null) {
//                sampleTv.setText(topText);
//            } else if (bottomText != null) {
//                sampleTv.setText(bottomText);
//            } else {
//                sampleTv.setText(context.getString(R.string.demo));
//            }
//        }
    }

    public interface TextStylesDialogCallback {
        void onFontClicked(Typeface typeface);

        void onTextColorClicked(int textColor);

        void onTextSizeClicked(int textSize);

        void onTextBgColorClicked(int bgColor);
    }
}
