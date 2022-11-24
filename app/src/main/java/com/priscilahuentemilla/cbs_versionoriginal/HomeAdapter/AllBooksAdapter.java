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

public class AllBooksAdapter extends RecyclerView.Adapter<AllBooksAdapter.AllBooksViewHolder> {

    ArrayList<AllBooksHelpedClass> allBooksLocations;

    public AllBooksAdapter(ArrayList<AllBooksHelpedClass> allBooksLocations) {
        this.allBooksLocations = allBooksLocations;
    }

    @NonNull
    @Override
    public AllBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_books_card_design,parent,false);
        AllBooksViewHolder allBooksViewHolder = new AllBooksViewHolder(view);
        return allBooksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllBooksViewHolder holder, int position) {

        AllBooksHelpedClass allBooksHelpedClass = allBooksLocations.get(position);

        holder.image.setImageResource(allBooksHelpedClass.getImage());
        holder.title.setText(allBooksHelpedClass.getTitle());
        holder.desc.setText(allBooksHelpedClass.getDesc());
        holder.author.setText(allBooksHelpedClass.getAuthor());
        holder.genre.setText(allBooksHelpedClass.getGenre());
        holder.paginas.setText(allBooksHelpedClass.getPaginas());


    }

    @Override
    public int getItemCount() {
        return allBooksLocations.size();
    }

    public static class AllBooksViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, author, genre, desc, paginas;

        public AllBooksViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ab_image);
            title = itemView.findViewById(R.id.ab_title);
            author = itemView.findViewById(R.id.ab_author);
            genre = itemView.findViewById(R.id.ab_genre);
            desc = itemView.findViewById(R.id.ab_desc);
            paginas = itemView.findViewById(R.id.ab_numeroPaginas);


        }
    }


}
