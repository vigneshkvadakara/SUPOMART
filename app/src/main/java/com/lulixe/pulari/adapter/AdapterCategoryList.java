package com.lulixe.pulari.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lulixe.pulari.R;
import com.lulixe.pulari.SingleViewActivity;
import com.lulixe.pulari.dialog.QuantityDialog;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.viewHolder> {

    private Context context;
    private List<Product> list;
    private AppDb db;
    public AdapterCategoryList(Context context, List<Product> list) {
        this.context = context;
        this.list=list;
        db = new AppDb(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_product_list_item,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder h, int i) {

        final Product product = list.get(i);

        Picasso.get()
                .load(Utils.productImageLink+product.getImage())
                .into(h.img_main);
        h.textName.setText(product.getName());
        h.textPrice.setText(String.valueOf("₹"+product.getProVarList().get(0).getPrice()));

        h.img_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, SingleViewActivity.class);
                i.putExtra("id",product.getId());
                context.startActivity(i);
            }
        });

        String [] ava = db.getProQtyAddedinDB(product.getId());
        if(ava != null){
            h.textBtBuy.setText("ADDED");
            h.textBtBuy.setTextColor(context.getResources().getColor(R.color.colorWhite));
            h.textBtBuy.setBackground(context.getResources().getDrawable(R.drawable.simple_btn_bknd));
        }
        else{
            h.textBtBuy.setText("BUY");
            h.textBtBuy.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            h.textBtBuy.setBackground(context.getResources().getDrawable(R.drawable.simple_btn_bknd2));
        }
        h.textBtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getStock() > 0){
                    QuantityDialog dialog= new QuantityDialog(context);
                    dialog.setModel(product);
                    dialog.show();
                }
            }
        });

        //veg non-veg
        int type = product.getType();
        if(type == 1)
            h.img_veg_non.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));
        else if(type == 2)
            h.img_veg_non.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_non_veg));

        h.textUnit.setText(String.valueOf("/"+product.getProVarList().get(0).getName()));

        //if items are out of stock
        if(product.getStock() <= 0){
            h.textBtBuy.setText("OUT OF STOCK");
            h.textBtBuy.setTextColor(context.getResources().getColor(R.color.colorRed));
            h.textBtBuy.setBackground(context.getResources().getDrawable(R.drawable.simple_btn_bknd3));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView img_main,img_veg_non;
        TextView textName,textPrice,textBtBuy,textUnit;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            img_main=itemView.findViewById(R.id.img_pro);
            img_veg_non=itemView.findViewById(R.id.img_pro_veg);

            textName =itemView.findViewById(R.id.text_pro_name);
            textPrice =itemView.findViewById(R.id.text_pro_price);
            textBtBuy =itemView.findViewById(R.id.text_bt_buy);
            textUnit = itemView.findViewById(R.id.textView13);
        }
    }
}
