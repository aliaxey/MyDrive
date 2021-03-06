package com.example.drcreeper.mydrive.screens.login;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drcreeper.mydrive.R;
import com.example.drcreeper.mydrive.databinding.FragmentLoginBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
private LoginViewModel viewModel;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new LoginViewModel(this);
        FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false);
        binding.setVm(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onResult(requestCode,resultCode,data);
    }
}