package com.upicon.app.UtilsMethod;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.upicon.app.R;

public class UtilsPermissions {
    Activity context;
    private final String[] REQUIRED_PERMISSIONS = new String[] {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.INTERNET"
    };
    private final int REQUEST_CODE_PERMISSIONS = 101;

    public UtilsPermissions(Activity context) {
        this.context = context;
    }

    // Check if all permissions have been allowed
    public boolean hasAllowedAllPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    // Request the un-allowed permissions
    public void requestPermissions() {
        ActivityCompat.requestPermissions(context, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }


}
