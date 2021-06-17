package com.lulixe.pulari;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lulixe.pulari.R;
import com.lulixe.pulari.adapter.AdapterCart;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartTotal{

    private AdapterCart adptrCart;

    private RecyclerView rvCart;
    private ImageView imageBack;
    private LinearLayout send;
    private TextView totalAmt;
    private AppDb db;

    private List<Product>productList;

    @Override
    protected void onRestart() {
        super.onRestart();
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Message");
        builder.setMessage("Do you want to clear the cart!?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.delAll();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        //getTodayDate();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productList.size() <= 0){
                    Toast.makeText(getApplicationContext(),"No items in cart!",Toast.LENGTH_LONG).show();
                    return;
                }

                sendToWhatsApp();

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendToWhatsApp() {
        if(new Utils().isWhatsAppInstalled(this)){
            String httpString = "https://api.whatsapp.com/send?phone=+919633662557&text=ORDER\n";

            String Q = "";
            double total = 0;
            for(Product p : productList){
                total += p.getPrice()*p.getCustomerQty();
                Q += p.getCustomerQty()+" X "+" - "+p.getName()+"("+p.getUnit()+")"+
                        "- Total:₹"+p.getCustomerQty()*p.getPrice()+"\n";
            }
            httpString += Q+"\n\n*Total Amount : ₹"+total+
                    "*\nOur customer support executive will contact you soon.Thankyou.";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpString));

            startActivity(browserIntent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Whats App is not installed in your device",Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        rvCart=findViewById(R.id.rv_cart);
        send = findViewById(R.id.linearLayout);
        imageBack = findViewById(R.id.imgBack);
        totalAmt = findViewById(R.id.textView19);

        db =new AppDb(this);
        productList = new ArrayList<>();
        productList = db.getAll();
        RecyclerView.LayoutManager lman2=new LinearLayoutManager(this);
        rvCart.setLayoutManager(lman2);
        rvCart.setNestedScrollingEnabled(false);
        rvCart.setItemAnimator(new DefaultItemAnimator());
        setAdapter(productList);
    }

    private void setAdapter(List<Product>list) {
        adptrCart=new AdapterCart(this,list);
        rvCart.setAdapter(adptrCart);

        setTotal();
    }

    private void setTotal() {
        totalAmt.setText("₹"+String.valueOf(adptrCart.getTotal()));
    }

    @Override
    public void setCartTotal() {
        setTotal();
    }

    /*private void getTodayDate(){
        String url = Utils.BASE_URL+"/getTodayDate";
        final Dialog d = new Dialog(CartActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_load);
        d.setCancelable(false);
        d.show();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(d != null)
                            d.dismiss();

                        try {
                            String TODAY_DATE = response;
                            productList.clear();
                            productList.addAll(db.getAll());

                            db.delAll(TODAY_DATE);
                            adptrCart.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(d != null)
                            d.dismiss();

                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(CartActivity.this).add(postRequest);
    }*/
}
