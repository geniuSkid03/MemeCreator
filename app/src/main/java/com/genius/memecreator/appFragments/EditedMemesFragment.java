package com.genius.memecreator.appFragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appActivities.EditorActivity;
import com.genius.memecreator.appAdapters.EditedViewAdapter;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.AppPermissionsHelper;
import com.genius.memecreator.appUtils.Keys;

import java.io.File;

public class EditedMemesFragment extends SuperFragment {

    private FloatingActionButton editNewFab, cameraFab, galleryFab;
    private CoordinatorLayout coordinatorLayout;
    private TextView noSavedTv;
    private GridView savedImgGv;

    private float offset1, offset2;
    private boolean isFabExpanded = false, isPermissionsAvailable = false;

    private static final String TRANSLATION_Y = "translationY";
    private static final String ROTATION = "rotation";
    private static final int OPEN_CAMERA = 125;
    private static final int OPEN_GALLERY = 565;

    private final int CHOOSE_IMAGE = 90;
    private Uri imgUri;

    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    private EditedViewAdapter editedViewAdapter;
    private File file;
    private Uri takenImageUri, pickedImgUri;

    private AppPermissionsHelper appPermissionsHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPermissionsHelper = new AppPermissionsHelper(getActivity(), dataStorage);
    }

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(layoutInflater.inflate(R.layout.fragment_edited_memes, container, false));
    }

    private View initView(View view) {

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        editNewFab = view.findViewById(R.id.edit_new);
        cameraFab = view.findViewById(R.id.fab_camera);
        galleryFab = view.findViewById(R.id.fab_gallery);

        savedImgGv = view.findViewById(R.id.gridview);
        noSavedTv = view.findViewById(R.id.no_saved);

        coordinatorLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                coordinatorLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = editNewFab.getHeight() * 1f;
                offset2 = editNewFab.getHeight() * 2f;
                return true;
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickOperations(v);
            }
        };

        editNewFab.setOnClickListener(clickListener);
        cameraFab.setOnClickListener(clickListener);
        galleryFab.setOnClickListener(clickListener);

        if (dataStorage.hasData(Keys.ALL_PERMS_GRANTED)) {
            isPermissionsAvailable = dataStorage.getBoolean(Keys.ALL_PERMS_GRANTED, false);
        }

        return view;
    }

    private void performClickOperations(View v) {
        switch (v.getId()) {
            case R.id.edit_new:
                toggleFab();
                break;
            case R.id.fab_camera:
                toggleFab();
                checkAndOpenCamera();
                break;
            case R.id.fab_gallery:
                toggleFab();
                checkAndOpenGallery();
                break;
        }
    }

    private void toggleFab() {
        if (!isFabExpanded) {
            expandFab();
        } else {
            collapseFab();
        }
    }

    private void expandFab() {
        editNewFab.setImageResource(R.drawable.ic_multiply);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(galleryFab, offset1), createExpandAnimator(cameraFab, offset2), rotateClockwiseAnimator(editNewFab, 90));
        animatorSet.start();
        animateFab(editNewFab);
        isFabExpanded = true;
    }

    private void collapseFab() {
        editNewFab.setImageResource(R.drawable.ic_add);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(galleryFab, offset1), createCollapseAnimator(cameraFab, offset2), rotateAnticlockwiseAnimator(editNewFab, 90));
        animatorSet.start();
        animateFab(editNewFab);
        isFabExpanded = false;
    }

    private void animateFab(FloatingActionButton fab) {
        Drawable drawable = fab.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, -offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, -offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator rotateClockwiseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, ROTATION, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator rotateAnticlockwiseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, ROTATION, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void checkAndOpenCamera() {
        if (isPermissionsAvailable) {
            openCamera();
        } else {
            askPermissions(OPEN_CAMERA);
        }
    }

    private void checkAndOpenGallery() {
        if (isPermissionsAvailable) {
            openGallery();
        } else {
            askPermissions(OPEN_GALLERY);
        }
    }

    private void askPermissions(int requestCode) {
        appPermissionsHelper.initPermissions(new String[]{Keys.READ_STORAGE, Keys.WRITE_STORAGE, Keys.CAMERA});
        if (!appPermissionsHelper.isAllPermissionsGranted()) {
            appPermissionsHelper.askAllPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (appPermissionsHelper.isAllPermissionsGranted(grantResults)) {
            switch (requestCode) {
                case OPEN_CAMERA:
                    openCamera();
                    break;
                case OPEN_GALLERY:
                    openGallery();
                    break;
            }
        } else {
            appPermissionsHelper.requestPermissionsIfDenied();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, "Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Editable image");
        takenImageUri = getContext().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takenImageUri);
        startActivityForResult(intent, OPEN_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_image_from)), OPEN_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_CAMERA:
                    if (takenImageUri != null) {
                        openWithEditor(takenImageUri);
                    } else {
                        showToast(getContext(), getString(R.string.info_img_cant_be_edited));
                    }
                    break;
                case OPEN_GALLERY:
                    pickedImgUri = data.getData();
                    if (pickedImgUri != null) {
                        openWithEditor(pickedImgUri);
                    } else {
                        showToast(getContext(), getString(R.string.info_img_cant_be_edited));
                    }
                    break;
            }
        }
    }

    private void openWithEditor(Uri imgUri) {
        goTo(getContext(), EditorActivity.class, false, Keys.TO_EDIT_IMAGE, imgUri.toString());
    }

    public void refreshEditedMemes() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            showToast(getContext(), getString(R.string.error_no_sd_card));
        } else {
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + getString(R.string.app_name) + File.separator + getString(R.string.edited));
            if(!file.exists()) {
                file.mkdirs();
            }
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();

            if (listFile != null) {
                FilePathStrings = new String[listFile.length];
                FileNameStrings = new String[listFile.length];

                for (int i = 0; i < listFile.length; i++) {
                    FilePathStrings[i] = listFile[i].getAbsolutePath();
                    FileNameStrings[i] = listFile[i].getName();
                }

                editedViewAdapter = new EditedViewAdapter(getActivity(), FilePathStrings, FileNameStrings, new EditedViewAdapter.ClickListener() {
                    @Override
                    public void onEditClicked(int position) {
                        prepareToEdit(FilePathStrings[position]);
                    }

                    @Override
                    public void onShareClicked(int position) {
                        prepareToShare(FilePathStrings[position]);
                    }
                });

                savedImgGv.setAdapter(editedViewAdapter);

                updateUi();
            } else {
                return;
            }
            AppHelper.print("No of images found: " + listFile.length);
        }
    }

    private void updateUi() {
        savedImgGv.setVisibility(editedViewAdapter.getCount() == 0 ? View.INVISIBLE : View.VISIBLE);
        noSavedTv.setVisibility(editedViewAdapter.getCount() != 0 ? View.INVISIBLE : View.VISIBLE);
    }

    private void prepareToShare(String filePathString) {
        Uri shareUri = AppHelper.getImageContentUri(getContext(), filePathString);
        if (shareUri != null) {
            startActivity(Intent.createChooser(
                    new Intent().setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, shareUri)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setType("image/*"), getString(R.string.share_meme_via)));
        } else {
            showToast(getContext(), getString(R.string.error_share_failed));
        }
    }

    private void prepareToEdit(String filePathString) {
        Uri shareUri = AppHelper.getImageContentUri(getContext(), filePathString);
        if (shareUri != null) {
            openWithEditor(shareUri);
        } else {
            showToast(getContext(), getString(R.string.info_edit_failed));
        }
    }



}
