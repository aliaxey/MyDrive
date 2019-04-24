package com.example.drcreeper.mydrive.screens.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.drcreeper.mydrive.R;
import com.example.drcreeper.mydrive.core.Constants;
import com.example.drcreeper.mydrive.core.DriveApplication;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel {
    private static final int LINEAR_LAYOUT_MANAGER = 1;
    Context context;
    Drive drive;
    List<File> list;
    public ObservableInt layoutManagerId;
    public ObservableField<FilesRecyclerViewAdapter> adapter;
    public ObservableBoolean isLoading;

    public ListViewModel(Context context){
        this.context = context;
        layoutManagerId = new ObservableInt(LINEAR_LAYOUT_MANAGER);
        adapter = new ObservableField<>();
        isLoading = new ObservableBoolean();
        drive = DriveApplication.getInstance().getDrive();
        updateList();
    }

    @SuppressLint("CheckResult")
    private void updateList(){
        isLoading.set(true);
        Observable.create((ObservableOnSubscribe<List<File>>) e-> {
            try {
                FileList files = drive
                        .files()
                        .list()
                        .execute();
                e.onNext(files.getFiles());
            } catch (IOException err) {
                e.onError(err);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                (n)->{
                    adapter.set(new FilesRecyclerViewAdapter(n));
                    isLoading.set(false);
                },
                (e)->{
                    Toast.makeText(context,R.string.err_login,Toast.LENGTH_LONG).show();
                    isLoading.set(false);
                }
        );

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1001:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, R.string.err_no_scopes, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public boolean onOptionItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_upload:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                DriveApplication.getInstance().startActivityForResult(intent, Constants.SELECT_FILE_REQUEST);
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.SELECT_FILE_REQUEST:
                if (resultCode == Activity.RESULT_OK && DriveApplication.getInstance().checkPermissions(context)) {
                    Uri uri = data.getData();
                    loadFile(uri);
                    break;
                }
        }
    }

    private void loadFile(Uri uri){
        Observable.create((emitter)-> {
            try {
                String type = getType(uri);
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                InputStreamContent data = new InputStreamContent(type, inputStream);
                File content = new File();
                content.setMimeType(type);
                content.setName(getFileName(uri));
                DriveApplication.getInstance().getDrive().files().create(content, data).execute();
            } catch (IOException e) {
                emitter.onError(e);
            }
            emitter.onNext(context);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                (n)->{
                    updateList();
                    Toast.makeText(context,R.string.msg_upload_done,Toast.LENGTH_LONG).show();
                    },
                (e)->{
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                });
    }
    private String getType(Uri uri){
        ContentResolver resolver = context.getContentResolver();
        return resolver.getType(uri);
    }
    private String getFileName(Uri uri){
        String[] parts = uri.getPath().split("/");
        String fileName = parts[parts.length - 1];
        return fileName;

    }
}
