package com.genius.memecreator.appUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.genius.memecreator.appDatas.DataStorage;

import java.util.ArrayList;
import java.util.Collections;

public class AppPermissionsHelper {

    private static final int PERMISSIONS_REQUEST_CODE = 3;
    private ArrayList<String> requiredPermissions, ungrantedPermissions;
    private Activity activity;
    private AlertDialog permissionAlertDialog;
    private DataStorage dataStorage;

    public AppPermissionsHelper(Activity activity, DataStorage dataStorage) {
        this.activity = activity;
        requiredPermissions = new ArrayList<>();
        ungrantedPermissions = new ArrayList<>();

        permissionAlertDialog = new AlertDialog.Builder(activity).create();
        permissionAlertDialog.setCancelable(false);

        this.dataStorage = dataStorage;
    }

    public void initPermissions(String[] permissions) {
        Collections.addAll(requiredPermissions, permissions);
    }

    public boolean isPermissionAvailable(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isAllPermissionsGranted() {
        boolean isAllPermissionsGranted = false;
        for (String permissions : requiredPermissions) {
            isAllPermissionsGranted = ContextCompat.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
        }
        return isAllPermissionsGranted;
    }

    public boolean isAllPermissionsGranted(int[] grantResults) {
        boolean isAllPermissionsGranted = false;
        for (int results : grantResults) {
            if (results == PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = true;
            }
        }
        return isAllPermissionsGranted;
    }

    public void askPermission(String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
    }

    public void askAllPermission() {
        ungrantedPermissions = getUnGrantedPermissionsList();
        if (ungrantedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]), PERMISSIONS_REQUEST_CODE);
            setPermissionAsAskedOnce(ungrantedPermissions);
        }

    }

    private void setPermissionAsAskedOnce(ArrayList<String> onceAskedPermissions) {
        for (String permission : onceAskedPermissions) {
            dataStorage.saveData(permission, true);
        }
    }

    private ArrayList<String> getUnGrantedPermissionsList() {
        ArrayList<String> permissionsList = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        return permissionsList;
    }

    private boolean canShowRationaleDialog() {
        boolean shouldShowRationale = false;
        for (String permission : ungrantedPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                shouldShowRationale = true;
            }
        }
        return shouldShowRationale;
    }

    public void requestPermissionsIfDenied() {
        ungrantedPermissions = getUnGrantedPermissionsList();
        if (canShowRationaleDialog()) {
            showRationalePermissionDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initPermissions(ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]));
                    askAllPermission();
                }
            });
        }
    }

    private void showRationalePermissionDialog(DialogInterface.OnClickListener onOkClickListener) {
        if (permissionAlertDialog != null) {
            permissionAlertDialog.setTitle("App cannot work without permission. Grant to continue or cancel to exit!");
            permissionAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Grant", onOkClickListener);
            permissionAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            if (!activity.isFinishing() && !permissionAlertDialog.isShowing()) {
                permissionAlertDialog.show();
            }
        }
    }

    public boolean isStoragePermissionsAvailable() {
        return isPermissionAvailable(Keys.READ_STORAGE) && isPermissionAvailable(Keys.WRITE_STORAGE);
    }

}
