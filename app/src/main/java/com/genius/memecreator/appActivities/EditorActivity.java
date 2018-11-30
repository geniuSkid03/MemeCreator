package com.genius.memecreator.appActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appAdapters.EditingOptionsAdapter;
import com.genius.memecreator.appDatas.EditingMenus;
import com.genius.memecreator.appDatas.Fonts;
import com.genius.memecreator.appDatas.TrendingMemes;
import com.genius.memecreator.appDialogs.EditorExitDialog;
import com.genius.memecreator.appDialogs.FontChooserDialog;
import com.genius.memecreator.appDialogs.SaveFileDialog;
import com.genius.memecreator.appDialogs.TextFormatDialog;
import com.genius.memecreator.appDialogs.TextInputDialog;
import com.genius.memecreator.appHelpers.RecyclerViewItemClickListener;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.Keys;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditorActivity extends SuperCompatActivity {

    @BindView(R.id.editing_image)
    ImageView editImageIv;

    @BindView(R.id.edited_bottom_text)
    TextView bottomTv;

    @BindView(R.id.edited_top_text)
    TextView topTv;

    @BindView(R.id.editing_options_rv)
    RecyclerView editingMenusRv;

    @BindView(R.id.seek_bar_layout)
    LinearLayout seekbarLayout;

    @BindView(R.id.text_size_seek_bar)
    SeekBar textSizeSeekbar;

    @BindView(R.id.editing_image_container)
    RelativeLayout editingImageContainer;

    private static final int PERMISSION_CODE = 150;
    private static final int OPEN_IMAGE = 152;
    private static final int OPEN_CAMERA = 153;

    private EditingOptionsAdapter editingOptionsAdapter;
    private ArrayList<EditingMenus> editingMenusArrayList;

    private TrendingMemes trendingMemes;
    private AmbilWarnaDialog textColorChooserDialog, bgColorChooserDialog;

    private TextInputDialog textInputDialog;
    private FontChooserDialog fontChooserDialog;
    private TextFormatDialog textFormatDialog;
    private SaveFileDialog saveFileDialog;
    private EditorExitDialog editorExitDialog;

    private MenuItem downloadMenu, shareMenu, okMenu;
    private Uri shareImageUri, editingImageUri;

    private boolean isColorChanged = false;
    private int textSize;
    private int textColor = Color.WHITE;
    private int textBgColor = Color.BLACK;
    private int textViewFormat = 0;
    private boolean isAllCapital = false;
    private int textAlignment = 1;

    private Fonts chosenFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initToolbar();

        if (getIntent().getExtras() != null) {
            trendingMemes = gson.fromJson(getIntent().getStringExtra(Keys.TO_EDIT), TrendingMemes.class);
        }

        if (trendingMemes != null) {
            loadEditor(trendingMemes);
        }

        initEditingMenus();

        loadEditingMenus();

        loadDialogs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (okMenu.isVisible()) {
                    doFunctionsForOkMenuClick();
                } else {
                    showExitConfirmation();
                }
                break;
            case R.id.save:
                askToSave();
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.ok:
                doFunctionsForOkMenuClick();
                break;
            case R.id.editor_help:
                AppHelper.toast(this, getString(R.string.function_not_set));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_activity_menu, menu);

        downloadMenu = menu.findItem(R.id.save);
        shareMenu = menu.findItem(R.id.share);
        okMenu = menu.findItem(R.id.ok);

        showToolbarMenus();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionIfNeeded();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        showToast(EditorActivity.this, " No Permission granted!");
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case OPEN_IMAGE:
                        if (data != null) {
                            openImage(data);
                        }
                        break;
                    case OPEN_CAMERA:
                        if (data != null) {
                            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                            editImageIv.setImageBitmap(bitmap);
                        }
                        break;
                    case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                        editImageIv.setImageURI(activityResult.getUri());
                        editingImageUri = activityResult.getUri();
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!okMenu.isVisible()) {
            showExitConfirmation();
        } else {
            doFunctionsForOkMenuClick();
        }
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void loadEditor(TrendingMemes trendingMemes) {
        Picasso.get().load(trendingMemes.getImgUrl()).into(editImageIv, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) editImageIv.getDrawable()).getBitmap();
                editingImageUri = AppHelper.getImageUri(EditorActivity.this, bitmap);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        AppHelper.print("Image Uri: " + trendingMemes.getImgUrl());

        chosenFont = new Fonts(getString(R.string.def), Typeface.DEFAULT);
    }

    private void initEditingMenus() {
        editingMenusArrayList = new ArrayList<>();

        editingOptionsAdapter = new EditingOptionsAdapter(this, editingMenusArrayList, dataStorage);

//        editingMenusRv.addItemDecoration(new BottomPaddingDecoration(4));

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
//        Drawable dividerDrawable = ContextCompat.getDrawable(editingMenusRv.getContext(), R.drawable.recycler_horizontal_divider);
//        assert dividerDrawable != null;
//        dividerItemDecoration.setDrawable(dividerDrawable);
//        editingMenusRv.addItemDecoration(dividerItemDecoration);

        editingMenusRv.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        editingMenusRv.setLayoutManager(linearLayoutManager);

        editingMenusRv.addOnItemTouchListener(new RecyclerViewItemClickListener(EditorActivity.this,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        doFunctionsForClick(editingOptionsAdapter, position);
                        editingOptionsAdapter.setSelected(position);
                    }
                }));
        editingMenusRv.setAdapter(editingOptionsAdapter);
    }

    private void loadEditingMenus() {
        editingMenusArrayList.add(0, new EditingMenus(R.drawable.ic_edit_text, "Text"));
        editingMenusArrayList.add(1, new EditingMenus(R.drawable.ic_text_size, "Size"));
        editingMenusArrayList.add(2, new EditingMenus(R.drawable.ic_text_color_icon, "Color"));
        editingMenusArrayList.add(3, new EditingMenus(R.drawable.ic_bg_color, "Bg color"));
        editingMenusArrayList.add(4, new EditingMenus(R.drawable.ic_style_icon, "Styles"));
        editingMenusArrayList.add(5, new EditingMenus(R.drawable.ic_text_format, "Format"));
        editingMenusArrayList.add(6, new EditingMenus(R.drawable.ic_stickers_icon, "sticker"));
        editingMenusArrayList.add(7, new EditingMenus(R.drawable.ic_black_and_white_icon, "Invert"));
        editingMenusArrayList.add(8, new EditingMenus(R.drawable.ic_crop_icon, "Crop"));
//        editingMenusArrayList.add(8, new EditingMenus(R.drawable.ic_reset_icon, "Reset"));

        editingOptionsAdapter.notifyDataSetChanged();
    }

    private void loadDialogs() {
        textInputDialog = new TextInputDialog(this, new TextInputDialog.TextInputDialogCallback() {
            @Override
            public void onDialogDismissed(String topText, String bottomText) {
                onTextReceived(topText, bottomText);
            }
        });

        textSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onSizeReceived(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        textColorChooserDialog = new AmbilWarnaDialog(this, textColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                onTextColorChosen(textColor);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                textColor = color;
                onTextColorChosen(color);
            }
        });

        bgColorChooserDialog = new AmbilWarnaDialog(this, textBgColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                onBgColorChosen(textBgColor);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                textBgColor = color;
                onBgColorChosen(color);
            }
        });

        fontChooserDialog = new FontChooserDialog(this, fontNames, fontTypeface, new FontChooserDialog.FontStylesInterface() {
            @Override
            public void onFontChoosed(Fonts fonts) {
                if (fonts != null) {
                    chosenFont = fonts;
                }
                onTextFontSelected(fonts);
            }
        });

        textFormatDialog = new TextFormatDialog(this, new TextFormatDialog.TextFormatListener() {
            @Override
            public void onTextFormat(int textFormat) {
                textViewFormat = textFormat;
                onTextFormatReceived(textFormat);
            }

            @Override
            public void isAllCaps(boolean isAllCaps) {
                isAllCapital = isAllCaps;
                onTextAllCaps(isAllCaps);
                AppHelper.print("Text all caps: " + isAllCaps);
            }

            @Override
            public void onAlignmentChosen(int alignment) {
                textAlignment = alignment;
                onTextAlignmentChanged(textAlignment);
            }
        });

        saveFileDialog = new SaveFileDialog(this, new SaveFileDialog.SaveNameInteface() {
            @Override
            public void onNameGiven(String fileName) {
                if (!TextUtils.isEmpty(fileName)) {
                    saveWithName(fileName);
                } else {
                    showToast(EditorActivity.this, getString(R.string.name_cannot_be_empty));
                }
            }
        });

        editorExitDialog = new EditorExitDialog(this, new EditorExitDialog.ExitListener() {
            @Override
            public void onOkClicked() {
                goToHome();
            }

            @Override
            public void onCancelClicked() {
                editorExitDialog.dismiss();
            }
        });
    }

    private void askToSave() {
        if (saveFileDialog != null && !saveFileDialog.isShowing()) {
            saveFileDialog.show();
        }
    }

    private void saveWithName(String fileName) {
        editingImageContainer.setDrawingCacheEnabled(true);
        Bitmap bitmap = editingImageContainer.getDrawingCache();

        File mainFolder = new File(Environment.getExternalStorageDirectory(), File.separator + Keys.FOLDER_MAIN + File.separator);
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }

        File editedMemes = new File(mainFolder, Keys.FOLDER_EDITED);
        if (!editedMemes.exists()) {
            editedMemes.mkdirs();
        }

        String file = fileName + ".jpg";
        File toWriteFile = new File(editedMemes, file);

        if (toWriteFile.exists()) {
            toWriteFile.delete();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(toWriteFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();

            showToast(EditorActivity.this, "Meme saved to: " + toWriteFile.getAbsolutePath());
            refreshGallery(toWriteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGallery(File toWriteFile) {
        MediaScannerConnection.scanFile(this,
                new String[]{toWriteFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        AppHelper.print("ExternalStorage Scanned " + path);
                        AppHelper.print("ExternalStorage -> uri=" + uri);
                    }
                });
    }

    private void shareImage() {
        if (AppHelper.isAboveM()) {
            if (appPermissionsHelper.isStoragePermissionsAvailable()) {
                shareImageUri = AppHelper.getUriFromImage(this, editImageIv);
            } else {
                appPermissionsHelper.initPermissions(new String[]{Keys.READ_STORAGE, Keys.WRITE_STORAGE});
                appPermissionsHelper.askAllPermission();
            }
        } else {
            shareImageUri = AppHelper.getUriFromImage(this, editImageIv);
        }

        if (shareImageUri != null) {
            startActivity(Intent.createChooser(
                    new Intent().setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, shareImageUri)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setType("image/*"), "Share MEME via"
            ));
        } else {
            showToast(this, "Sharing failed!");
        }

    }

    private void getPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
        }
    }

    private void doFunctionsForClick(EditingOptionsAdapter editingOptionsAdapter, int position) {
        switch (position) {
            case 0: //input text
                showTextInputDialog();
                break;
            case 1: //text size
                showTextSizeView();
                break;
            case 2: //text color
                showColorChooserDialog();
                break;
            case 3: //bg color
                showBgColorChooserDialog();
                break;
            case 4: //text font
                showFontChooserDialog();
                break;
            case 5: //text format - styles (bold, italic, all caps, all small, text alignment)
                showTextFormatDialog();
                break;
            case 6:
                showStickers();
                break;
            case 7: //black - white color
                changeColor();
                break;
            case 8: //Crop image
                openCropActivity();
                break;
            case 9: //reset
                askBeforeReset();
                break;
        }
    }

    private void askBeforeReset() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(getString(R.string.unsaved_will_be_lost));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proceedToReset();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (!isFinishing() && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void proceedToReset() {
        textSize = 16;
        textColor = Color.WHITE;
        textBgColor = Color.BLACK;
        textViewFormat = 0;
        isAllCapital = false;
        textAlignment = 1;

        chosenFont = new Fonts(getString(R.string.def), Typeface.DEFAULT);
        onTextReceived("", "");
        if (textInputDialog != null) {
            textInputDialog.setEditingView(topTv.getText().toString().trim(), bottomTv.getText().toString().trim());
        }
    }

    private void showTextInputDialog() {
        if (textInputDialog != null && !textInputDialog.isShowing()) {
            textInputDialog.setEditingView(topTv.getText().toString().trim(), bottomTv.getText().toString().trim());
            textInputDialog.show();
        }
    }

    private void showTextSizeView() {
        hideToolbarMenus();
        editingMenusRv.setVisibility(View.GONE);
        seekbarLayout.setVisibility(View.VISIBLE);
    }

    private void showColorChooserDialog() {
        if (textColorChooserDialog != null && !textColorChooserDialog.getDialog().isShowing()) {
            textColorChooserDialog.show();
        }
    }

    private void showBgColorChooserDialog() {
        if (bgColorChooserDialog != null && !bgColorChooserDialog.getDialog().isShowing()) {
            bgColorChooserDialog.show();
        }
    }

    private void showFontChooserDialog() {
        if (fontChooserDialog != null && !fontChooserDialog.isShowing()) {
            fontChooserDialog.show();
        }
    }

    private void showTextFormatDialog() {
        if (textFormatDialog != null && !textFormatDialog.isShowing()) {
            textFormatDialog.setChosenFont(chosenFont);
            textFormatDialog.setChosenFormat(textViewFormat, isAllCapital, textAlignment);
            textFormatDialog.show();
        }
    }

    private void showStickers() {

    }

    private void showExitConfirmation() {
        if(editorExitDialog != null && !editorExitDialog.isShowing()) {
            editorExitDialog.show();
        }
    }

    private void changeColor() {
        if (!isColorChanged) {
            convertToGrayscale();
        } else {
            convertToNormal();
        }
    }

    private void convertToGrayscale() {
        isColorChanged = true;

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        editImageIv.setColorFilter(colorMatrixColorFilter);
    }

    private void convertToNormal() {
        isColorChanged = false;

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(1);

        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        editImageIv.setColorFilter(colorMatrixColorFilter);
    }

    private void openCropActivity() {
        if (editingImageUri == null) {
            AppHelper.print("Image uri null, cannot crop!");
            showToast(this, "This image cannot be cropped!");
            return;
        }

        Intent intent = CropImage.activity(editingImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void hideToolbarMenus() {
        downloadMenu.setVisible(false);
        shareMenu.setVisible(false);

        okMenu.setVisible(true);
    }

    private void showToolbarMenus() {
        downloadMenu.setVisible(true);
        shareMenu.setVisible(true);

        okMenu.setVisible(false);
    }

    private void doFunctionsForOkMenuClick() {
        showToolbarMenus();

        editingMenusRv.setVisibility(View.VISIBLE);
        seekbarLayout.setVisibility(View.GONE);
    }

    private void goToHome() {
        discardChanges();
        finish();
    }

    private void discardChanges() {
        showToast(this, "Changes discarded!");
    }

    private void onTextReceived(String topText, String bottomText) {
        if (!TextUtils.isEmpty(topText)) {
            topTv.setVisibility(View.VISIBLE);
            topTv.setText(topText);
        } else {
            topTv.setText("");
            topTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(bottomText)) {
            bottomTv.setVisibility(View.VISIBLE);
            bottomTv.setText(bottomText);
        } else {
            bottomTv.setText("");
            bottomTv.setVisibility(View.GONE);
        }

    }

    private void onSizeReceived(int textSize) {
        topTv.setTextSize(textSize);
        bottomTv.setTextSize(textSize);
    }

    private void onTextColorChosen(int textColor) {
        topTv.setTextColor(textColor);
        bottomTv.setTextColor(textColor);
    }

    private void onBgColorChosen(int textBgColor) {
        topTv.setBackgroundColor(textBgColor);
        bottomTv.setBackgroundColor(textBgColor);
    }

    private void onTextFontSelected(Fonts fonts) {
        if (fonts != null) {
            topTv.setTypeface(fonts.getTypeface());
            bottomTv.setTypeface(fonts.getTypeface());
            AppHelper.print("Font set as: " + fonts.getFontName());
        } else {
            AppHelper.print("Font empty");
        }
    }

    private void onTextFormatReceived(int format) {
        switch (format) {
            case 0:
                topTv.setTypeface(chosenFont.getTypeface(), Typeface.NORMAL);
                bottomTv.setTypeface(chosenFont.getTypeface(), Typeface.NORMAL);
                break;
            case 1:
                topTv.setTypeface(chosenFont.getTypeface(), Typeface.BOLD);
                bottomTv.setTypeface(chosenFont.getTypeface(), Typeface.BOLD);
                break;
            case 2:
                topTv.setTypeface(chosenFont.getTypeface(), Typeface.ITALIC);
                bottomTv.setTypeface(chosenFont.getTypeface(), Typeface.ITALIC);
                break;
            case 3:
                topTv.setTypeface(chosenFont.getTypeface(), Typeface.BOLD_ITALIC);
                bottomTv.setTypeface(chosenFont.getTypeface(), Typeface.BOLD_ITALIC);
                break;
        }
    }

    private void onTextAllCaps(boolean isAllCapital) {
        AppHelper.print("OnTextAllCaps: " + isAllCapital);
        topTv.setAllCaps(isAllCapital);
        bottomTv.setAllCaps(isAllCapital);
    }

    private void onTextAlignmentChanged(int position) {
        switch (position) {
            case 0:
                topTv.setGravity(Gravity.START);
                bottomTv.setGravity(Gravity.START);
                break;
            case 1:
                topTv.setGravity(Gravity.CENTER);
                bottomTv.setGravity(Gravity.CENTER);
                break;
            case 2:
                topTv.setGravity(Gravity.END);
                bottomTv.setGravity(Gravity.END);
                break;

        }
    }

    private void showSharingOptions() {
        Bitmap toShareBitmap = ((BitmapDrawable) editImageIv.getDrawable()).getBitmap();

        if (toShareBitmap == null) {
            AppHelper.print("Image was not found to share!");
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(Keys.MIME_TYPE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "image_title");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, Keys.MIME_TYPE);

        Uri sharingUri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            if (sharingUri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(sharingUri);
                toShareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } else {
                AppHelper.print("Sharing Uri was null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppHelper.print(e.toString());
        }

        shareIntent.putExtra(Intent.EXTRA_STREAM, sharingUri);
        startActivity(Intent.createChooser(shareIntent, "Share Your meme via..."));
    }

    private void askAndShowGallery() {
        final CharSequence items[] = {getString(R.string.open_camera), getString(R.string.pick_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.open_file_title));
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean isPermissionAvailable = dataStorage.getBoolean(Keys.ALL_PERMS_GRANTED, false);
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (isPermissionAvailable) {
                        openCamera();
                    } else {
                        showPermissionDenied();
                    }
                } else if (items[item].equals(getString(R.string.pick_from_gallery))) {
                    if (isPermissionAvailable) {
                        openGallery();
                    } else {
                        showPermissionDenied();
                    }
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        alertDialog.setCancelable(false);
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    private void openCamera() {
        Intent cameraIntetn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntetn.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntetn, OPEN_CAMERA);
        }
    }

    private void openGallery() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), OPEN_IMAGE);
    }

    private void showPermissionDenied() {
        showToast(this, "Cannot use this feature, permission denied!");
    }

    private void openImage(Intent data) {
        try {
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            if (selectedImageUri != null) {
                cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            }
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                editImageIv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
