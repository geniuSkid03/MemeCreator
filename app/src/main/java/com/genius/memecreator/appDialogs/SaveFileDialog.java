package com.genius.memecreator.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.genius.memecreator.R;
import com.genius.memecreator.appUtils.AppHelper;

import java.util.Objects;

public class SaveFileDialog extends Dialog {

    private Context context;

    public SaveFileDialog(final Context context, final SaveNameInteface saveNameInteface) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.context = context;

        final View nameAskingView = LayoutInflater.from(context).inflate(R.layout.name_asking_to_save, null);

        final EditText nameEd = nameAskingView.findViewById(R.id.file_name_ed);
        final AppCompatButton saveBtn = nameAskingView.findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNameInteface.onNameGiven(nameEd.getText().toString().trim());
                dismiss();
                AppHelper.hideKeyBoard(context, saveBtn.getWindowToken());
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(nameAskingView, layoutParams);

        setCancelable(true);
    }

    public interface SaveNameInteface {
        void onNameGiven(String fileName);
    }
}
