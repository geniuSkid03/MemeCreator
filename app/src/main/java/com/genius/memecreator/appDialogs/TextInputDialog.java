package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.memecreator.R;
import com.genius.memecreator.appUtils.AppHelper;

import java.util.Objects;

public class TextInputDialog extends Dialog {

    private ImageView topClrIv, bottomClrIv;
    private EditText topEd, bottomEd;
    private AppCompatButton okBtn;

    private TextInputDialogCallback textInputDialogCallback;
    private Context context;

    public TextInputDialog(Context context, final TextInputDialogCallback textInputDialogCallback) {
        super(context);

        this.textInputDialogCallback = textInputDialogCallback;
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View textInputDialogView = LayoutInflater.from(context).inflate(R.layout.text_dialog_input_layout, null);

        topEd = textInputDialogView.findViewById(R.id.top_text_ed);
        bottomEd = textInputDialogView.findViewById(R.id.bottom_text_ed);
        topClrIv = textInputDialogView.findViewById(R.id.top_ed_clr);
        bottomClrIv = textInputDialogView.findViewById(R.id.bottom_ed_clr);
        okBtn = textInputDialogView.findViewById(R.id.ok_btn);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(textInputDialogView, layoutParams);

        setCancelable(true);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ok_btn:
                        onOkBtnClicked();
                        break;
                    case R.id.top_ed_clr:
                        clearText(0);
                        break;
                    case R.id.bottom_ed_clr:
                        clearText(1);
                        break;
                }
            }
        };

        okBtn.setOnClickListener(clickListener);
        topClrIv.setOnClickListener(clickListener);
        bottomClrIv.setOnClickListener(clickListener);
    }

    private void onOkBtnClicked() {
        textInputDialogCallback.onDialogDismissed(topEd.getText().toString().trim(), bottomEd.getText().toString().trim());
        AppHelper.hideKeyBoard(context, okBtn.getWindowToken());
        dismiss();
    }

    private void clearText(int view) {
        switch (view) {
            case 0:
                topEd.setText("");
                topClrIv.setVisibility(View.GONE);
                break;
            case 1:
                bottomEd.setText("");
                bottomClrIv.setVisibility(View.GONE);
                break;
        }
    }

    public void setEditingView(String prevTopText, String prevBottomText) {
        if (topEd != null && !TextUtils.isEmpty(prevTopText)) {
            topEd.setText(prevTopText);
            topClrIv.setVisibility(View.VISIBLE);
        }

        if (bottomEd != null && !TextUtils.isEmpty(prevBottomText)) {
            bottomEd.setText(prevBottomText);
            bottomClrIv.setVisibility(View.VISIBLE);
        }
    }
    public interface TextInputDialogCallback {
        void onDialogDismissed(String topText, String bottomText);
    }
}
