package com.example.drcreeper.mydrive.screens.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drcreeper.mydrive.R;
import com.example.drcreeper.mydrive.databinding.LayoutFileBinding;
import com.google.api.services.drive.model.File;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FileViewHolder> {
    private List<File> list;
    public FilesRecyclerViewAdapter(List<File> l){
        super();
        list = l;
    }
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutFileBinding binding = LayoutFileBinding.inflate(inflater,parent,false);
        return new FileViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        File file = list.get(position);
        holder.getBinding().setFile(new FileView(file));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
