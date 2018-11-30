package com.genius.memecreator.appUtils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genius.memecreator.R;

public class DialogsViewManager {

    private Context context;
    private String viewName, viewId;
    private int textSize;

    private View view;
    private LayoutInflater layoutInflater;

    private DialogManagerListener dialogManagerListener;

    public DialogsViewManager(Context context, DialogManagerListener dialogManagerListener) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dialogManagerListener = dialogManagerListener;
    }

    public View getViewForInput(int viewId) {
        switch (viewId) {
            case 0:
                view = viewForTextInput();
                break;
            case 1:
                view = viewForFontSize();
                break;
        }
        return view;
    }

    private View viewForFontSize() {
        View fontView = layoutInflater.inflate(R.layout.font_size_view, null);

        final TextView textView = fontView.findViewById(R.id.font_size_sample_text);
        SeekBar seekBar = fontView.findViewById(R.id.font_size_seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSize = progress;
                textView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setTextSize(textSize);
                dialogManagerListener.onTextSizeChanged(textSize);
            }
        });

        return fontView;
    }

    private View viewForTextInput() {
        View textInputView = layoutInflater.inflate(R.layout.text_dialog_input_layout, null);

        final EditText topEd = textInputView.findViewById(R.id.top_text_ed);
        final EditText bottomEd = textInputView.findViewById(R.id.bottom_text_ed);
        AppCompatButton okBtn = textInputView.findViewById(R.id.ok_btn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogManagerListener.onTextReceived(topEd.getText().toString().trim(), bottomEd.getText().toString().trim());
            }
        });

        return textInputView;
    }

    public interface DialogManagerListener {
        void onTextReceived(String topText, String bottomText);
        void onTextSizeChanged(int textSize);
        void onFontChanged(Typeface chosenFont);
        void onAlignmentChanged(String alignment);
        void onStylesChanged(String style);
        void onTypoChanged(String typo);
        void onTextColorChanged(int textColor, int textBorderColor);
    }
}
