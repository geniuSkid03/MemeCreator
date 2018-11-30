package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.genius.memecreator.R;

public class EditorExitDialog extends Dialog {

    private Context context;
    private AppCompatButton okBtn, cancelBtn;

    private ExitListener exitListener;

    public EditorExitDialog(Context context, final ExitListener exitListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.context = context;
        this.exitListener = exitListener;

        View exitView = LayoutInflater.from(context).inflate(R.layout.editor_exit_view, null);

        okBtn = exitView.findViewById(R.id.ok_btn);
        cancelBtn = exitView.findViewById(R.id.cancel_btn);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ok_btn:
                        exitListener.onOkClicked();
                        break;
                    case R.id.cancel_btn:
                        exitListener.onCancelClicked();
                        break;
                }
            }
        };
        okBtn.setOnClickListener(clickListener);
        cancelBtn.setOnClickListener(clickListener);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(exitView, layoutParams);

        setCancelable(true);
    }

    public interface ExitListener {
        void onOkClicked();

        void onCancelClicked();
    }
}
