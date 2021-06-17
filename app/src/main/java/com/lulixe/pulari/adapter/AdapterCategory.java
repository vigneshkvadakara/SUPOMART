package com.lulixe.pulari.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lulixe.pulari.R;
import com.lulixe.pulari.CategoryActivity;
import com.lulixe.pulari.model.Category;
import com.lulixe.pulari.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.viewHolder> {

    private Context context;
    private List<Category> list;
    public AdapterCategory(Context context, List<Category> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_category_item,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder h, int i) {
        final Category cat=list.get(i);

        Picasso.get()
                .load(Utils.categoryLink+cat.getImage())
                .into(h.img);



       h.parent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent in = new Intent(context, CategoryActivity.class);
               in.putExtra("id",cat.getId());
               context.startActivity(in);
           }
       });

       h.textHeading.setText(cat.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parent;
        ImageView img;
        TextView textHeading;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img_cat);
            textHeading=itemView.findViewById(R.id.text_cat);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
