package com.example.drcreeper.mydrive;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel {
    private final ObservableField<String> buttonText = new ObservableField<>("");
    private Fragment parent;
    GoogleSignInAccount account;
    Drive drive;

    public ObservableField<String> getButtonText() {
        return buttonText;
    }

    public LoginViewModel(Fragment fragment){
        parent = fragment;
    }

    public void login(View v){


        account = GoogleSignIn.getLastSignedInAccount(parent.getContext());
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(parent.getContext(),
                Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount());
        drive = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(), credential)
                .setApplicationName("MyExample")
                .build();
        Disposable d =  Observable.create((ObservableOnSubscribe<List<File>>) e-> {
            try {
                File doc = new File().setName("test.txt").setMimeType("text/plain");
                File folder = new File().setName("Tipo folder").setMimeType(DriveFolder.MIME_TYPE);
                drive.files().create(folder).execute();
                ByteArrayContent content = ByteArrayContent.fromString("text/plain","lolkek cheburek \n"+ drive.toString());
                drive.files().create(doc,content).execute();
                FileList files = drive
                        .files()
                        .list()
                        .setPageSize(5)
                        .setFields("nextPageToken, files(id, name)")
                        .execute();//296752
                e.onNext(files.getFiles());
            } catch (IOException err) {
                e.onError(err);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                n-> {for(File f:n){buttonText.set(buttonText.get()+f.getName()+"\n"+f.getOriginalFilename()+'\n'+f.getParents());}},
                e-> {buttonText.set(e.toString());}
                );

        /*
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(Drive.SCOPE_FILE).build();
        GoogleSignInClient account = GoogleSignIn.getClient(parent.getActivity(),options);
        Intent signIn = account.getSignInIntent();
        parent.startActivityForResult(signIn, 9000);*/

    }

    private List<File> getList(){
        try {
            FileList files = drive
                    .files()
                    .list()
                    .setPageSize(5)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();//296752
            return files.getFiles();
            //buttonText.set(files.getFiles().get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                Fragment listFragment = new ListFragment();

                FragmentManager fragmentManager = parent.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.root,new ListFragment()).commit();


            } catch (ApiException e) {
                Toast.makeText(parent.getActivity(),R.string.err_login,Toast.LENGTH_LONG).show();
                parent.getActivity().finish();
            }
        }
    }
}
