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
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appAdapters.FontAdapter;
import com.genius.memecreator.appDatas.Fonts;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appViews.CustomGrids;

import java.util.ArrayList;
import java.util.Objects;

public class FontChooserDialog extends Dialog {

    private FontStylesInterface fontStylesInterface;

    private Fonts chosenFont;

    public FontChooserDialog(Context context, ArrayList<String> fontNames, ArrayList<Typeface> fontTypeface, final FontStylesInterface fontStylesInterface) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.fontStylesInterface = fontStylesInterface;

        View fontStyleView = LayoutInflater.from(context).inflate(R.layout.font_styles_view, null);

        final TextView textView = fontStyleView.findViewById(R.id.sample_text);

        final CustomGrids customGridsLayout = fontStyleView.findViewById(R.id.font_list);
        customGridsLayout.setAdapter(new FontAdapter(context, fontNames, fontTypeface, new FontAdapter.OnFontClicked() {
            @Override
            public void chosenFont(Fonts fonts) {
                textView.setTypeface(fonts.getTypeface());
                AppHelper.print("Clicked Font: " + fonts.getFontName());
                setChosenFont(fonts);
            }
        }));

        AppCompatButton applyBtn = fontStyleView.findViewById(R.id.font_ok_btn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDismissed();
                cancel();
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(fontStyleView, layoutParams);

        setCancelable(true);
    }

    private void setChosenFont(Fonts chosenFont) {
        this.chosenFont = chosenFont;
    }

    private Fonts getChosenFont() {
        return chosenFont;
    }

    private void onDialogDismissed() {
        Fonts chosenFont = getChosenFont();
        if(chosenFont != null) {
            fontStylesInterface.onFontChoosed(chosenFont);
        }
    }

    public interface FontStylesInterface {
        void onFontChoosed(Fonts fonts);
    }
}
