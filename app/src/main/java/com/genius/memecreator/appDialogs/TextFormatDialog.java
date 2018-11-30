package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.Fonts;

public class TextFormatDialog extends Dialog {

    private TextFormatListener textFormatListener;
    private Fonts font;

    private Context context;

    private TextView normalView, boldView, italicView, headingTv, boldItalicView, upperCaseView, lowerCaseView;
    private ImageView alignLeftView, alignCenterView, alignRightView;

    public TextFormatDialog(Context context, TextFormatListener textFormatListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.textFormatListener = textFormatListener;
        this.context = context;

        View textFormatView = LayoutInflater.from(context).inflate(R.layout.text_format_view, null);

        headingTv = textFormatView.findViewById(R.id.heading);

        normalView = textFormatView.findViewById(R.id.normal_view);
        boldView = textFormatView.findViewById(R.id.bold_view);
        italicView = textFormatView.findViewById(R.id.italic_view);
        boldItalicView = textFormatView.findViewById(R.id.bold_italic_view);

        upperCaseView = textFormatView.findViewById(R.id.upper_case_view);
        lowerCaseView = textFormatView.findViewById(R.id.lower_case_view);

        alignLeftView = textFormatView.findViewById(R.id.align_left);
        alignCenterView = textFormatView.findViewById(R.id.align_center);
        alignRightView = textFormatView.findViewById(R.id.align_end);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.normal_view:
                        showDrawableFor(0);
                        break;
                    case R.id.bold_view:
                        showDrawableFor(1);
                        break;
                    case R.id.italic_view:
                        showDrawableFor(2);
                        break;
                    case R.id.bold_italic_view:
                        showDrawableFor(3);
                        break;
                    case R.id.lower_case_view:
                        setAsSelected(0);
                        break;
                    case R.id.upper_case_view:
                        setAsSelected(1);
                        break;
                    case R.id.align_left:
                        makeTextAlign(0);
                        break;
                    case R.id.align_center:
                        makeTextAlign(1);
                        break;
                    case R.id.align_end:
                        makeTextAlign(2);
                        break;
                }
            }
        };

        normalView.setOnClickListener(clickListener);
        boldView.setOnClickListener(clickListener);
        italicView.setOnClickListener(clickListener);
        boldItalicView.setOnClickListener(clickListener);
        upperCaseView.setOnClickListener(clickListener);
        lowerCaseView.setOnClickListener(clickListener);
        alignRightView.setOnClickListener(clickListener);
        alignCenterView.setOnClickListener(clickListener);
        alignLeftView.setOnClickListener(clickListener);

        setContentView(textFormatView);
        setCancelable(true);
    }

    private void makeTextAlign(int position) {
        switch (position) {
            case 0: //left align
                alignLeftView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                alignCenterView.setBackgroundResource(android.R.color.transparent);
                alignRightView.setBackgroundResource(android.R.color.transparent);
                break;
            case 1: //center align
                alignCenterView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                alignLeftView.setBackgroundResource(android.R.color.transparent);
                alignRightView.setBackgroundResource(android.R.color.transparent);
                break;
            case 2: //right align
                alignRightView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                alignCenterView.setBackgroundResource(android.R.color.transparent);
                alignLeftView.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }

    private void setAsSelected(int position) {
        switch (position) {
            case 0: //lowercase
                lowerCaseView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                upperCaseView.setBackgroundResource(android.R.color.transparent);

                headingTv.setAllCaps(false);
                break;
            case 1: //upper case
                upperCaseView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                lowerCaseView.setBackgroundResource(android.R.color.transparent);

                headingTv.setAllCaps(true);
                break;
        }
        textFormatListener.isAllCaps(position != 0);
    }

    public void setChosenFormat(int textViewFormat, boolean isAllCaps, int textAlignment) {
        showDrawableFor(textViewFormat);
        setAsSelected(isAllCaps ? 1 : 0);
        makeTextAlign(textAlignment);
    }

    public void setChosenFont(Fonts font) {
        if (font != null) {
            this.font = font;
        } else {
            this.font = new Fonts(context.getString(R.string.def), Typeface.DEFAULT);
        }

        headingTv.setTypeface(this.font.getTypeface());
    }

    private Fonts getChosenFont() {
        return font;
    }

    private void showDrawableFor(int position) {
        textFormatListener.onTextFormat(position);
        switch (position) {
            case 0:
                headingTv.setTypeface(getChosenFont().getTypeface(), Typeface.NORMAL);

                normalView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                boldItalicView.setBackgroundResource(android.R.color.transparent);
                boldView.setBackgroundResource(android.R.color.transparent);
                italicView.setBackgroundResource(android.R.color.transparent);
                break;
            case 1:
                headingTv.setTypeface(getChosenFont().getTypeface(), Typeface.BOLD);

                boldView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                boldItalicView.setBackgroundResource(android.R.color.transparent);
                normalView.setBackgroundResource(android.R.color.transparent);
                italicView.setBackgroundResource(android.R.color.transparent);
                break;
            case 2:
                headingTv.setTypeface(getChosenFont().getTypeface(), Typeface.ITALIC);

                italicView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                boldItalicView.setBackgroundResource(android.R.color.transparent);
                normalView.setBackgroundResource(android.R.color.transparent);
                boldView.setBackgroundResource(android.R.color.transparent);
                break;
            case 3:
                headingTv.setTypeface(getChosenFont().getTypeface(), Typeface.BOLD_ITALIC);

                boldItalicView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_border));
                italicView.setBackgroundResource(android.R.color.transparent);
                normalView.setBackgroundResource(android.R.color.transparent);
                boldView.setBackgroundResource(android.R.color.transparent);
                break;
        }
    }

    public interface TextFormatListener {
        void onTextFormat(int textFormat);

        void isAllCaps(boolean isAllCaps);

        void onAlignmentChosen(int alignment);
    }
}
