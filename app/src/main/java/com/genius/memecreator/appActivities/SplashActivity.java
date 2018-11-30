package com.genius.memecreator.appActivities;

import android.os.Bundle;
import android.os.Handler;

import com.genius.memecreator.R;
import com.genius.memecreator.appUtils.Keys;

public class SplashActivity extends SuperCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();

        waitAndLoad();
    }

    private void waitAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndGo();
            }
        }, 1500);
    }

    private void checkAndGo() {
        if (dataStorage.hasData(Keys.ALL_PERMS_GRANTED)) {
            if (dataStorage.getBoolean(Keys.ALL_PERMS_GRANTED, false)) {
                openHome();
            } else {
                openPermissionHelper();
            }
        } else {
            openPermissionHelper();
        }
    }

    private void openPermissionHelper() {
        goTo(this, PermissionsHelperActivity.class, true);
    }

    private void openHome() {
        goTo(this, HomeActivity.class, true);
    }
}
