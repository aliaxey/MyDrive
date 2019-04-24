package com.example.drcreeper.mydrive.screens.list;


import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.drcreeper.mydrive.R;
import com.example.drcreeper.mydrive.core.Constants;
import com.example.drcreeper.mydrive.core.DriveApplication;
import com.example.drcreeper.mydrive.core.MimeType;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.IOException;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FileView {
    private String fileId;
    private String mimeType;
    private final ObservableInt image = new ObservableInt();
    private final ObservableField<String> name = new ObservableField<>();
    private boolean isFolder = false;
    DriveApplication application;

    public FileView(File file){
        application = DriveApplication.getInstance();
        fileId = file.getId();
        name.set(file.getName());
        mimeType = file.getMimeType();
        String type = mimeType.substring(0,mimeType.indexOf("/"));
        switch (type){
            case MimeType.TEXT:
                image.set(R.drawable.text);
                break;
            case MimeType.IMAGE:
                image.set(R.drawable.image);
                break;
            case MimeType.MUSIC:
                image.set(R.drawable.music);
                break;
            case MimeType.VIDEO:
                image.set(R.drawable.video);
                break;
                default:
                    if(file.getMimeType().equals(MimeType.FOLDER)){
                        isFolder = true;
                        image.set(R.drawable.folder);
                    }else {
                        image.set(R.drawable.unknown);
                    }
        }
    }
    public ObservableInt getImage() {
        return image;
    }

    public ObservableField<String> getName() {
        return name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void onOpenClick(View v){
        checkPermissionsAndDownload(v.getContext(),true);
    }
    public void onDownloadClick(View v){
        checkPermissionsAndDownload(v.getContext(),false);
    }

    private void checkPermissionsAndDownload(Context context, boolean needOpen){
        if(application.checkPermissions(context)) {
            downloadFile(context,needOpen);
        }
    }
    private void downloadFile(Context context, boolean needOpen){
        Observable.create((subscriber)->{
            FileOutputStream fStream = null;
            try {
                java.io.File file = new java.io.File(Environment.getExternalStorageDirectory(),
                        Constants.APP_FOLDER + Constants.DIR_DIVIDER + name.get()); // .executeMediaAndDownloadTo(fStream) context.getExternalFilesDir(Constants.APP_FOLDER).;
                fStream = new FileOutputStream(file);
                Drive drive = application.getDrive();
                drive.files().get(fileId).executeAndDownloadTo(fStream);
                subscriber.onNext(file);
            }catch (IOException e){
                subscriber.onError(e);
            }finally {

                if(fStream != null){
                    fStream.close();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                (next)->{
                    if(needOpen){
                        application.openFile((java.io.File)next,mimeType);
                    }else {
                        Toast.makeText(context, context.getString(R.string.msg_download_done), Toast.LENGTH_LONG).show();
                    }
                    },
                (error)->{Toast.makeText(context,context.getString(R.string.err_download) + error ,Toast.LENGTH_LONG).show();}
        );
    }


}
