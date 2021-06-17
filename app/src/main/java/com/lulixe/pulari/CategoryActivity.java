package com.lulixe.pulari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lulixe.pulari.adapter.AdapterCategoryList;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.model.QuantityConn;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.FlipAnimation;
import com.lulixe.pulari.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements QuantityConn {

    private AdapterCategoryList adptrCatList;
    private RecyclerView rvCatList;
    private ImageView imgCart,backIcon;
    int c=0;

    @Override
    protected void onRestart() {
        super.onRestart();

        adptrCatList.notifyDataSetChanged();
        new FlipAnimation(this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        int id = getIntent().getIntExtra("id",0);
        init();
        setAdapter(new ArrayList<Product>());
        getCategoryData(id);

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CategoryActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*c+=1;
                FlipAnimation flip=new FlipAnimation(CategoryActivity.this,c+"");
                        flip.start();*/
            }
        });

        new FlipAnimation(this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    private void init() {
        rvCatList=findViewById(R.id.rv_cat_list);
        imgCart = findViewById(R.id.img_cart);
        backIcon = findViewById(R.id.imgBack);
    }

    private void setAdapter(List<Product> products) {
        RecyclerView.LayoutManager lman2=new GridLayoutManager(CategoryActivity.this,2);
        rvCatList.setLayoutManager(lman2);
        rvCatList.setNestedScrollingEnabled(false);
        adptrCatList=new AdapterCategoryList(this,products);
        rvCatList.setItemAnimator(new DefaultItemAnimator());
        rvCatList.setAdapter(adptrCatList);
    }

    private void getCategoryData(int id){
        String url = Utils.BASE_URL+"/getProductByCat?marketID="+ Utils.MARKET_ID+"&catID="+id;

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setResponce(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);
    }


    private void setResponce(String responce) {
        List<Product>productList = new Gson().fromJson(responce,new TypeToken<List<Product>>() {}.getType());
        setAdapter(productList);
    }

    @Override
    public void refreshAdapter() {
        adptrCatList.notifyDataSetChanged();
    }

    @Override
    public void refreshAdapter(int index, Product e) {

    }
}
