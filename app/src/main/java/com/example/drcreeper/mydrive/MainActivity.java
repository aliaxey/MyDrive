package com.example.drcreeper.mydrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.drcreeper.mydrive.core.DriveApplication;
import com.example.drcreeper.mydrive.core.Switcher;
import com.example.drcreeper.mydrive.screens.login.LoginFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements Switcher {
    FragmentManager fragmentManager;
    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        DriveApplication.getInstance().setSwitcher(this);
        switchFragment(new LoginFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void switchFragment(Fragment fragment){
        currentFragment = fragment;
        fragmentManager.beginTransaction().replace(R.id.root,fragment).commitNow();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        currentFragment.startActivityForResult(intent, requestCode);
    }


}
