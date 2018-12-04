package com.genius.memecreator.appActivities;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.Keys;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class PermissionsHelperActivity extends SuperCompatActivity {

    @BindView(R.id.next_btn)
    AppCompatButton nextBtn;

    @BindView(R.id.permsm)
    TextView permsnTv;

    private String[] appNeededPermissions = {Keys.CAMERA, Keys.READ_STORAGE, Keys.WRITE_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_helper);
    }

    @OnClick({R.id.next_btn})
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                checkAndAskPermission();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (appPermissionsHelper.isAllPermissionsGranted(grantResults)) {
            goNext();
        } else {
            appPermissionsHelper.requestPermissionsIfDenied();
        }
    }

    private void checkAndAskPermission() {
        if (AppHelper.isAboveM()) {
            askPermissions();
        } else {
            goNext();
        }
    }

    private void askPermissions() {
        appPermissionsHelper.initPermissions(appNeededPermissions);
        if (!appPermissionsHelper.isAllPermissionsGranted()) {
            appPermissionsHelper.askAllPermission();
        } else {
            goNext();
        }
    }

    private void goNext() {
        createFolders();
        dataStorage.saveData(Keys.ALL_PERMS_GRANTED, true);
        goTo(this, HomeActivity.class, true);
    }

    private void createFolders() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            AppHelper.toast(this, "Error! No SDCARD Found!");
        } else {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator
                    + getString(R.string.app_name)
                    + File.separator
                    + getString(R.string.edited));
            if(!file.exists()) {
                file.mkdirs();
                AppHelper.print("Edited folder created");
            }

            File file1 = new File(Environment.getExternalStorageDirectory()
                    + File.separator
                    + getString(R.string.app_name)
                    + File.separator
                    + getString(R.string.saved));
            if(!file1.exists()) {
                file1.mkdirs();
                AppHelper.print("Saved folder created!");
            }
        }
    }
}
