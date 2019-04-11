package com.example.drcreeper.mydrive;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;

import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;

public class LoginViewModel {
    private final ObservableField<String> buttonText = new ObservableField<>();
    private Fragment parent;
    GoogleSignInAccount account;

    public ObservableField<String> getButtonText() {
        return buttonText;
    }

    public LoginViewModel(Fragment fragment){
        parent = fragment;
    }
    public void login(View v){
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(Drive.SCOPE_FILE).build();
        GoogleSignInClient account = GoogleSignIn.getClient(parent.getActivity(),options);
        Intent signIn = account.getSignInIntent();
        parent.startActivityForResult(signIn, 9000);

    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                TextView t = (TextView)parent.getActivity().findViewById(R.id.textView);
                /// TODO: 05.04.2019  

            } catch (ApiException e) {
                Toast.makeText(parent.getActivity(),R.string.err_login,Toast.LENGTH_LONG).show();
                parent.getActivity().finish();
            }
        }
    }
}
