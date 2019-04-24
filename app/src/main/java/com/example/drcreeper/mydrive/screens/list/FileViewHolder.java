package com.example.drcreeper.mydrive.screens.list;

import android.view.View;

import com.example.drcreeper.mydrive.databinding.LayoutFileBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FileViewHolder extends RecyclerView.ViewHolder {
    private LayoutFileBinding binding;
    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
    public LayoutFileBinding getBinding() {
        return binding;
    }

}
