package com.genius.memecreator.appDialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.genius.memecreator.R;

public class EditNewBottomSheetDialog extends BottomSheetDialog {

    public EditNewBottomSheetDialog(@NonNull final Context context, final EditNewDialogCallback editNewDialogCallback) {
        super(context);

        View editNewView = LayoutInflater.from(context).inflate(R.layout.edit_new_dialog, null);

        setContentView(editNewView);
        setCancelable(true);

        LinearLayout openCameraView = editNewView.findViewById(R.id.open_camera);
        LinearLayout chooseFromGalleryView = editNewView.findViewById(R.id.open_gallery);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.open_camera:
                        editNewDialogCallback.onCameraClicked();
                        dismiss();
                        break;
                    case R.id.open_gallery:
                        editNewDialogCallback.onGalleryClicked();
                        dismiss();
                        break;
                }
            }
        };

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                editNewDialogCallback.onDismissed();

            }
        });

        openCameraView.setOnClickListener(onClickListener);
        chooseFromGalleryView.setOnClickListener(onClickListener);
    }

    public interface EditNewDialogCallback {
        void onCameraClicked();

        void onGalleryClicked();

        void onDismissed();
    }

}
