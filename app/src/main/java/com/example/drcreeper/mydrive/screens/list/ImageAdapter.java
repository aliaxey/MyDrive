package com.example.drcreeper.mydrive.screens.list;

import android.view.View;
import android.widget.ImageView;

import com.example.drcreeper.mydrive.R;
import com.squareup.picasso.Picasso;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter {
    @BindingAdapter({"app:url"})
    public static void loadImage(ImageView imageView, String v) {
        Picasso.with(imageView.getContext()).load(v).error(R.drawable.ic_launcher_background).into(imageView);
    }
    @BindingAdapter({"app:res_id"})
    public static void setFileIcon(ImageView imageView,int resId){
        imageView.setImageResource(resId);
    }
    @BindingAdapter({"app:manager"})
    public static void setLayoutManager(RecyclerView recyclerView, int managerId){
        switch (managerId){
            case 1:
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                break;
            default:
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),managerId));
        }
    }
    @BindingAdapter({"app:visible"})
    public static void setVisiblity(View v, boolean visible){
        if(visible) {
            v.setVisibility(View.VISIBLE);
        }else {
            v.setVisibility(View.INVISIBLE);
        }
    }
}
