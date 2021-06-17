package com.lulixe.pulari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lulixe.pulari.adapter.AdapterSearch;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.model.QuantityConn;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.FlipAnimation;
import com.lulixe.pulari.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements QuantityConn {

    private EditText search;
    private ImageView imgBack,imgCart;
    private RecyclerView rvSearch;

    private AdapterSearch adptrSearch;

    int c=1;


    @Override
    protected void onRestart() {
        super.onRestart();
        adptrSearch.notifyDataSetChanged();
        new FlipAnimation(SearchActivity.this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();

        setAdapter(new ArrayList<Product>());

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new Utils().hideKeyboard(SearchActivity.this);
                getSearchData(search.getText().toString());
                return true;
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SearchActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new FlipAnimation(SearchActivity.this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    private void init(){
        search=findViewById(R.id.edt_search);
        imgBack=findViewById(R.id.imgBack);
        imgCart=findViewById(R.id.img_cart);
        rvSearch=findViewById(R.id.rv_search);
        search.requestFocus();
    }

    private void setAdapter(List<Product> products) {
        RecyclerView.LayoutManager lman2=new GridLayoutManager(SearchActivity.this,2);
        rvSearch.setLayoutManager(lman2);
        rvSearch.setNestedScrollingEnabled(false);
        adptrSearch=new AdapterSearch(this,products);
        rvSearch.setItemAnimator(new DefaultItemAnimator());
        rvSearch.setAdapter(adptrSearch);
    }

    private void getSearchData(String text){
        String url = Utils.BASE_URL+"/searchProduct?marketID="+ Utils.MARKET_ID+"&searchKey="+text;

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
        /*try{
            JSONArray jar=new JSONArray(responce);
            *//* product *//*
            List<Product> productList = new ArrayList<>();
            int catListSize = jar.length();
            for(int i=0;i<catListSize;i++){

                JSONObject jObj = jar.getJSONObject(i);

                Product product=new Product();
                product.setId(jObj.getInt("id"));
                product.setImage(jObj.getString("image"));
                product.setName(jObj.getString("name"));
                product.setPrice(jObj.getDouble("price"));
                product.setVarID(jObj.getInt("qty"));
                product.setStock(jObj.getInt("stock"));
                product.setType(jObj.getInt("type"));
                product.setUnit(jObj.getString("unit"));
                product.setDateTime(jObj.getString("date"));
                productList.add(product);
            }
            setAdapter(productList);

        }catch (Exception e){
        }*/
    }

    @Override
    public void refreshAdapter() {
        adptrSearch.notifyDataSetChanged();
    }

    @Override
    public void refreshAdapter(int index, Product e) {

    }
}
