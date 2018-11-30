package com.genius.memecreator.appActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.DataStorage;
import com.genius.memecreator.appUtils.AppPermissionsHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class SuperCompatActivity extends AppCompatActivity {

    protected Animation fab_close, fab_open, rotate_backwards, rotate_forwards;

    protected DataStorage dataStorage;
    protected AppPermissionsHelper appPermissionsHelper;
    protected Gson gson = new Gson();


    protected ArrayList<String> fontNames;
    protected ArrayList<Typeface> fontTypeface;
    protected Typeface acmeTf, aldrichTf, architechTf, boogaloTf, breeSerifTf, cabinTf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //shared prefs initialization
        dataStorage = new DataStorage(this);

        //permissions helper initialization
        appPermissionsHelper = new AppPermissionsHelper(this, dataStorage);

        //loading animations globally for app
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open_animation);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close_animation);
        rotate_backwards = AnimationUtils.loadAnimation(this, R.anim.rotate_backwards_animation);
        rotate_forwards = AnimationUtils.loadAnimation(this, R.anim.rotate_forwards_animation);

        //fonts
        acmeTf = ResourcesCompat.getFont(this, R.font.acme);
        aldrichTf = ResourcesCompat.getFont(this, R.font.aldrich);
        architechTf = ResourcesCompat.getFont(this, R.font.archiitech);
        boogaloTf = ResourcesCompat.getFont(this, R.font.boogalo);
        breeSerifTf = ResourcesCompat.getFont(this, R.font.breeserif);
        cabinTf = ResourcesCompat.getFont(this, R.font.cabin);

        fontTypeface = new ArrayList<>();
        fontTypeface.add(acmeTf);
        fontTypeface.add(aldrichTf);
        fontTypeface.add(architechTf);
        fontTypeface.add(boogaloTf);
        fontTypeface.add(breeSerifTf);
        fontTypeface.add(cabinTf);

        fontNames = new ArrayList<>();
        fontNames.add(getString(R.string.font_acme));
        fontNames.add(getString(R.string.font_aldrich));
        fontNames.add(getString(R.string.font_architech));
        fontNames.add(getString(R.string.font_boogalo));
        fontNames.add(getString(R.string.font_breeserif));
        fontNames.add(getString(R.string.font_cabin));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        //butter knife injection
        ButterKnife.bind(this);
    }

    protected void goTo(Context context, Class destClass, boolean finish) {
        startActivity(new Intent(context, destClass));
        if (finish) {
            finish();
        }
    }

    protected void showToast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
