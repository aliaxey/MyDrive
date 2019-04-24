package com.example.drcreeper.mydrive.screens.list;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.drcreeper.mydrive.R;
import com.example.drcreeper.mydrive.databinding.FragmentListBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class ListFragment extends Fragment {
    private ListViewModel viewModel;

    public ListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        FragmentListBinding binding = FragmentListBinding.inflate(inflater,container,false);//DataBindingUtil.setContentView(getActivity(),R.layout.fragment_list);//
        viewModel = new ListViewModel(getContext());
        binding.setVm(viewModel);
        return binding.getRoot();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.files_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return viewModel.onOptionItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
