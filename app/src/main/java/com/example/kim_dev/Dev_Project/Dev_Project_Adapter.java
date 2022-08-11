package com.example.kim_dev.Dev_Project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kim_dev.R;

import java.util.ArrayList;

public class Dev_Project_Adapter extends RecyclerView.Adapter<Dev_Project_Adapter.DPA_ViewHolder> {

    Context context;
    ArrayList<Dev_Project_data> list = new ArrayList<>();

    public Dev_Project_Adapter (Context context, ArrayList<Dev_Project_data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DPA_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_dev_project_item, parent, false);
        return new DPA_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DPA_ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.p_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DPA_ViewHolder extends RecyclerView.ViewHolder {
        ImageView p_image;

        public DPA_ViewHolder(@NonNull View itemView) {
            super(itemView);

            p_image = itemView.findViewById(R.id.p_image);
        }
    }
}


























