package com.example.drcreeper.mydrive.core;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface Switcher {
    void switchFragment(Fragment fragment);
    void startActivity(Intent intent);
    void startActivityForResult(Intent intent, int code);
    void requestPermissions(String[] permissions, int code);
}
