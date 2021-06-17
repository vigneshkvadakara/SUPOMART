package com.lulixe.pulari;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sliderlibrary.library.Slide;
import com.example.sliderlibrary.library.Slider;
import com.google.gson.Gson;
import com.lulixe.pulari.R;
import com.lulixe.pulari.adapter.AdapterCategory;
import com.lulixe.pulari.adapter.AdapterProduct;
import com.lulixe.pulari.model.Category;
import com.lulixe.pulari.model.Home;
import com.lulixe.pulari.model.Offers;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.model.QuantityConn;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.FlipAnimation;
import com.lulixe.pulari.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements QuantityConn {

    private AdapterCategory adptrCategory;
    private AdapterProduct adptrProduc;

    private RecyclerView rvProduct,rvCategory;
    private ImageView imgCart,img;
    private TextView search,textF,textB;
    private Slider sliderLayout;
    private AppDb db;

    
    private int SPAN_COUNT_OF_CATEGORY=3;
    private int SPAN_COUNT_OF_PODUCT=2;

    int c=1;
    int flag=0;

    @Override
    protected void onRestart() {
        super.onRestart();
        adptrProduc.notifyDataSetChanged();
        new FlipAnimation(this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        setAdapterCategory(new ArrayList<Category>());
        setAdapterProduct(new ArrayList<Product>());

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });

        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c+=1;
                FlipAnimation flip= new FlipAnimation(MainActivity.this,*//*textF,textB,*//*c+"");
                flip.start(*//*flag*//*);
                *//*if (flag==0){
                    flag=1;
                }else
                    flag=0;*//*
            }
        });*/

        String responce = getIntent().getStringExtra("data");
        setUI(responce);

        new FlipAnimation(MainActivity.this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    private void setUI(String responce) {
        Home homeData = new Gson().fromJson(responce,Home.class);
        setSliderViews(homeData.getOffers());
        setAdapterCategory(homeData.getCategories());
        setAdapterProduct(homeData.getProducts());
    }

    private void setSliderViews(List<Offers> offerList) {

        List<Slide> slideList=new ArrayList<>();
        int size=offerList.size();

        for (int i =0 ;i<size;i++){
            slideList.add(new Slide(i,Utils.offerImageLink+offerList.get(i).getImage() , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        }
        sliderLayout.addSlides(slideList);
    }

    private void init() {
        rvCategory=findViewById(R.id.rv_category);
        rvProduct=findViewById(R.id.rv_product);
        imgCart =findViewById(R.id.img_cart);
        img=findViewById(R.id.imgBack);
        search=findViewById(R.id.text_search);
        sliderLayout = findViewById(R.id.imageSlider);
        textF=findViewById(R.id.textF);
        textB = findViewById(R.id.textB);

        db = new AppDb(this);

        //img = findViewById(R.id.imageSlider);
        /*imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(in);
            }
        });*/
    }

    private void setAdapterCategory(List<Category> categories) {
        RecyclerView.LayoutManager lman=new GridLayoutManager(MainActivity.this,SPAN_COUNT_OF_CATEGORY);
        rvCategory.setLayoutManager(lman);
        ViewCompat.setNestedScrollingEnabled(rvCategory, false);
        //rvCategory.setNestedScrollingEnabled(false);
        adptrCategory=new AdapterCategory(this,categories);
        rvCategory.setItemAnimator(new DefaultItemAnimator());
        rvCategory.setAdapter(adptrCategory);
    }

    private void setAdapterProduct(List<Product> products) {
        RecyclerView.LayoutManager lman2=new GridLayoutManager(MainActivity.this,SPAN_COUNT_OF_PODUCT);
        rvProduct.setLayoutManager(lman2);
        ViewCompat.setNestedScrollingEnabled(rvProduct, false);
        //rvProduct.setNestedScrollingEnabled(false);
        adptrProduc=new AdapterProduct(this,products);
        rvProduct.setItemAnimator(new DefaultItemAnimator());
        rvProduct.setAdapter(adptrProduc);
    }


    @Override
    public void refreshAdapter() {
        adptrProduc.notifyDataSetChanged();
        /*list = db.getAll(TODAY_DATE);
        ad = new SupermarketListAdapter(SupermarketListActivity.this,list);
        rv.setAdapter(ad);*/
    }

    @Override
    public void refreshAdapter(int index, Product e) {

    }
}
