package com.example.drcreeper.mydrive.core;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.drcreeper.mydrive.R;
import com.google.api.services.drive.Drive;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class DriveApplication extends Application {
    private static DriveApplication instance;
    private Switcher switcher;
    private Drive drive;

    public void setSwitcher(Switcher switcher) {
        this.switcher = switcher;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new DriveApplication();
    }

    public static DriveApplication getInstance(){
        return instance;
    }
    public void fragmentSwitch(Fragment fragment){
        if(switcher != null){
            switcher.switchFragment(fragment);
        }else{
            throw new NullPointerException("Not set fragment switch");
        }
    }
    public void startActivityForResult(Intent intent, int code){
        switcher.startActivityForResult(intent,code);
    }
    public void startActivity(Intent intent){
        switcher.startActivity(intent);
    }
    public void openFile(java.io.File file,String mimeType){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri,mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
    public boolean checkPermissions(Context context){

        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(context, R.string.err_no_scopes, Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            switcher.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            return false;
        }
        return true;
    }
    private boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    private boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}
