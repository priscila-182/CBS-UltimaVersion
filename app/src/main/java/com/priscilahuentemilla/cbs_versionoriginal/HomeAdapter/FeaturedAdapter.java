package com.priscilahuentemilla.cbs_versionoriginal.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.priscilahuentemilla.cbs_versionoriginal.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedHelpedClass> featuredLocations;

    public FeaturedAdapter(ArrayList<FeaturedHelpedClass> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        FeaturedHelpedClass featuredHelpedClass = featuredLocations.get(position);

        holder.image.setImageResource(featuredHelpedClass.getImage());
        holder.title.setText(featuredHelpedClass.getTitle());
        holder.desc.setText(featuredHelpedClass.getDescription());
        holder.author.setText(featuredHelpedClass.getAutor());
        holder.genre.setText(featuredHelpedClass.getGenero());

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, desc, author, genre, paginas;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //hooks
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            desc = itemView.findViewById(R.id.featured_desc);
            author = itemView.findViewById(R.id.featured_autor);
            genre = itemView.findViewById(R.id.featured_genero);
            paginas = itemView.findViewById(R.id.featured_paginas);
        }
    }
}
