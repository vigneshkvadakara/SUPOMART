package com.lulixe.pulari.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lulixe.pulari.CartTotal;
import com.lulixe.pulari.R;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.viewHolder> {

    private Context context;
    private List<Product>list;
    private AppDb db;

    public AdapterCart(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
        db = new AppDb(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_list_item,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder h,final int i) {
        final Product product = list.get(i);

        Picasso.get()
                .load(Utils.productImageLink+product.getImage())
                .into(h.img_main);

        h.textName.setText(product.getName());
        h.textQuantity.setText(String.valueOf("Quantity:"+product.getCustomerQty()));
        h.textUnitPrice.setText("₹"+product.getPrice()+" "+"/"+product.getUnit());
        h.img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = db.delSingle(product.getId(),product.getVarID());
                if(flag){
                    list.remove(i);
                    notifyDataSetChanged();
                    CartTotal cartTotal = (CartTotal) context;
                    cartTotal.setCartTotal();
                }
            }
        });

        h.price.setText("₹"+product.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public double getTotal() {
        double total = 0;
        for(Product p : list)
            total += p.getCustomerQty()*p.getPrice();

        return total;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView img_main,img_remove;
        private TextView textName,textUnitPrice,textQuantity,price;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            img_main=itemView.findViewById(R.id.imageView5);
            img_remove=itemView.findViewById(R.id.imageView7);

            textName =itemView.findViewById(R.id.textView9);
            textUnitPrice =itemView.findViewById(R.id.textView11);
            textQuantity =itemView.findViewById(R.id.textView10);
            price = itemView.findViewById(R.id.textView16);
        }
    }
}
