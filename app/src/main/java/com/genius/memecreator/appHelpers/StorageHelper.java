package com.genius.memecreator.appHelpers;

import android.content.Context;
import android.os.Environment;

import com.genius.memecreator.appUtils.Keys;

import java.io.File;

public class StorageHelper {

    private Context context;
    File mainFolfer;

    public StorageHelper(Context context) {
        this.context = context;
        createMainFolder(Keys.FOLDER_MAIN);
    }

    private boolean isFileExists(File fileName) {
        return fileName.exists();
    }

    private void createMainFolder(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), File.separator + fileName + File.separator);
        if (!isFileExists(file)) {
            file.mkdirs();
        }
    }

    public boolean isSdCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
