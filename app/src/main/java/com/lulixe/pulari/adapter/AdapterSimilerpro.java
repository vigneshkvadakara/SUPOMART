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
import com.lulixe.pulari.SingleViewActivity;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSimilerpro extends RecyclerView.Adapter<AdapterSimilerpro.viewHolder> {

    private Context context;
    private List<Product> list;

    public AdapterSimilerpro(Context context, List<Product> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_similer_item,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder h, int i) {

        final Product product = list.get(i);

        Picasso.get()
                .load(Utils.productImageLink+product.getImage())
                .into(h.img_main);

        h.textName.setText(product.getName());
        h.textPrice.setText(String.valueOf(product.getPrice()));

        h.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, SingleViewActivity.class);
                i.putExtra("id",product.getId());
                context.startActivity(i);
            }
        });

        h.textUnit.setText(String.valueOf(" /"+product.getProVarList().get(0).getName()));
        h.textPrice.setText(String.valueOf("₹"+product.getProVarList().get(0).getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView img_main,img_veg_non;
        TextView textName,textPrice,textUnit;
        ConstraintLayout parent;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            img_main=itemView.findViewById(R.id.img_pro);
            img_veg_non=itemView.findViewById(R.id.img_pro_veg);

            textName =itemView.findViewById(R.id.text_pro_name);
            textPrice =itemView.findViewById(R.id.text_pro_price);
            textUnit = itemView.findViewById(R.id.textView14);
        }
    }
}
