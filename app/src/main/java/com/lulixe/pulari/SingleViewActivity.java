package com.lulixe.pulari;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.lulixe.pulari.adapter.AdapterSimilerpro;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.model.ProductVarient;
import com.lulixe.pulari.model.QuantityConn;
import com.lulixe.pulari.model.SingleProduct;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.FlipAnimation;
import com.lulixe.pulari.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SingleViewActivity extends AppCompatActivity implements QuantityConn {

    private AppDb db;
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 20;
    private int PRODUCT_ID = 0;

    private ImageView imgBackBlur,imgTop,imgVegOrNon,imgCart,imgBack;
    private TextView buyBt,unit,price,nameTx;
    private Spinner qtySpinner;
    private ImageView plusBt,minusBt;
    private TextView proQty;
    private LinearLayout qtyLayout;
    private RecyclerView rvSimiler;
    private AdapterSimilerpro adptrSimiler;
    private SingleProduct singleProduct;

    private ShimmerFrameLayout shimmer;
    private ConstraintLayout conContent;
    int c=0;

    int PRO_AUTO_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);
        init();
        shimmer.startShimmer();
        new FlipAnimation(SingleViewActivity.this,10+"").start();

        int id=getIntent().getIntExtra("id",0);
        setAdapterProduct(new ArrayList<Product>());
        getHomeData(id);

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SingleViewActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c+=1;
                FlipAnimation flip=new FlipAnimation(SingleViewActivity.this,10+"");
                flip.start();

                finish();
            }
        });

        plusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = singleProduct.getProduct();
                int qty = Integer.parseInt(proQty.getText().toString());
                product.setCustomerQty(qty+1);
                product.setVarID(product.getProVarList().
                        get(qtySpinner.getSelectedItemPosition()).getId());
                db.update(product);
                Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT).show();
                proQty.setText(String.valueOf(qty+1));

                new FlipAnimation(SingleViewActivity.this,
                        String.valueOf(new AppDb(getApplicationContext()).getCartCount())).start();
            }
        });

        minusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = singleProduct.getProduct();
                int qty = Integer.parseInt(proQty.getText().toString());
                if(qty == 1){
                    boolean flag = db.delSingle(product.getId(),
                            product.getProVarList().get(qtySpinner.getSelectedItemPosition()).getId());
                    if(flag){
                        buyBt.setVisibility(View.VISIBLE);
                        qtyLayout.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"else",Toast.LENGTH_LONG).show();
                    }
                }else{
                    product.setCustomerQty(qty-1);
                    product.setVarID(product.getProVarList().
                            get(qtySpinner.getSelectedItemPosition()).getId());
                    db.update(product);
                    Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT).show();
                    proQty.setText(String.valueOf(qty-1));
                }
                new FlipAnimation(SingleViewActivity.this,
                        String.valueOf(new AppDb(getApplicationContext()).getCartCount())).start();
            }
        });

        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = singleProduct.getProduct();
                String []data = db.getProQtyIfAvaliable(product.getId(),product.getProVarList().get(i).getId());
                if(data != null){
                    PRO_AUTO_ID = Utils.convertToInt(data[0]);
                    qtyLayout.setVisibility(View.VISIBLE);
                    buyBt.setVisibility(View.GONE);
                    proQty.setText(data[1]);
                }
                else{
                    PRO_AUTO_ID = 0;
                    qtyLayout.setVisibility(View.GONE);
                    buyBt.setVisibility(View.VISIBLE);
                    proQty.setText("1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new FlipAnimation(this,
                String.valueOf(new AppDb(this).getCartCount())).start();
    }

    private void init() {
        imgBackBlur=findViewById(R.id.imageView4);
        imgTop=findViewById(R.id.imageView2);
        imgVegOrNon=findViewById(R.id.imageView3);
        rvSimiler=findViewById(R.id.rv_similer);
        shimmer=findViewById(R.id.shimmer_s_v);
        conContent=findViewById(R.id.con_content);
        buyBt = findViewById(R.id.textView8);
        imgCart = findViewById(R.id.img_cart);
        imgBack = findViewById(R.id.imgBack);
        unit = findViewById(R.id.unit);
        price = findViewById(R.id.textView6);
        nameTx = findViewById(R.id.textView5);
        qtySpinner = findViewById(R.id.textView64);
        minusBt = findViewById(R.id.button);
        plusBt = findViewById(R.id.button2);
        proQty = findViewById(R.id.textview23);
        qtyLayout = findViewById(R.id.qtyLay);

        db = new AppDb(this);
    }

    private void setAdapterProduct(List<Product> p) {
        RecyclerView.LayoutManager lman2=new LinearLayoutManager(SingleViewActivity.this,LinearLayoutManager.HORIZONTAL,false);
        rvSimiler.setLayoutManager(lman2);
        ViewCompat.setNestedScrollingEnabled(rvSimiler, false);
        //rvSimiler.setNestedScrollingEnabled(false);
        adptrSimiler=new AdapterSimilerpro(this,p);
        rvSimiler.setItemAnimator(new DefaultItemAnimator());
        rvSimiler.setAdapter(adptrSimiler);
    }

    private void getHomeData(int id){
        String url = Utils.BASE_URL+"/getSingleProduct?marketID="+ Utils.MARKET_ID+"&id="+id;
        Log.e("url",url);

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
        //updated code
        singleProduct = new Gson().fromJson(responce,SingleProduct.class);
        Product product = singleProduct.getProduct();

        Picasso.get()
                .load(Utils.productImageLink+product.getImage())
                .into(imgTop);

        nameTx.setText(product.getName());
        ProductVarient varient = product.getProVarList().get(0);
        unit.setText(String.valueOf(" /"+varient.getName()));
        price.setText(String.valueOf("₹"+varient.getPrice()));

        Picasso.get()
                .load(Utils.productImageLink+product.getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ImageBlurer blurer=new ImageBlurer();
                        imgBackBlur.setImageBitmap(blurer.blur(bitmap,BITMAP_SCALE,BLUR_RADIUS));
                        createRoundedRectBitmap(imgTop,bitmap,convertDpToPx(10),convertDpToPx(10),convertDpToPx(10),convertDpToPx(10));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        setAdapterProduct(singleProduct.getSimilarProduct());

        //veg non-veg
        int type = product.getType();
        if(type == 1)
            imgVegOrNon.setImageDrawable(getResources().getDrawable(R.drawable.ic_veg));
        else if(type == 2)
            imgVegOrNon.setImageDrawable(getResources().getDrawable(R.drawable.ic_non_veg));

        //if items are out of stock
        if(product.getStock() <= 0){
            buyBt.setText("OUT OF STOCK");
            buyBt.setTextColor(this.getResources().getColor(R.color.colorWhite));
            buyBt.setBackground(this.getResources().getDrawable(R.drawable.simple_btn_bknd4));
        }

        //setting spinner
        List<String>variantList = new ArrayList<>();
        for(ProductVarient v:product.getProVarList()) variantList.add("₹"+v.getPrice()+"/"+v.getName());
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_text, variantList);
        qtySpinner.setAdapter(adp1);

        conContent.setVisibility(View.VISIBLE);
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);

        //looking for data in db
        String []data = db.getProQtyIfAvaliable(product.getId(),product.getProVarList().get(0).getId());
        if(data != null){
            PRO_AUTO_ID = Utils.convertToInt(data[0]);
            qtyLayout.setVisibility(View.VISIBLE);
            buyBt.setVisibility(View.GONE);
            proQty.setText(data[1]);
        }
        else{
            PRO_AUTO_ID = 0;
            qtyLayout.setVisibility(View.GONE);
            buyBt.setVisibility(View.VISIBLE);
            proQty.setText("1");
        }

        buyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = singleProduct.getProduct();
                if(product.getStock() > 0){
                    product.setCustomerQty(1);
                    product.setPrice(product.getProVarList().
                            get(qtySpinner.getSelectedItemPosition()).getPrice());
                    product.setUnit(product.getProVarList().
                            get(qtySpinner.getSelectedItemPosition()).getName());
                    product.setVarID(product.getProVarList().
                            get(qtySpinner.getSelectedItemPosition()).getId());
                    db.insert(product);
                    Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT).show();

                    new FlipAnimation(SingleViewActivity.this,
                            String.valueOf(new AppDb(getApplicationContext()).getCartCount())).start();

                    buyBt.setVisibility(View.GONE);
                    qtyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public float convertDpToPx( float dp) {
        return dp * this.getResources().getDisplayMetrics().density;
    }

    private Bitmap getBitmap(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        return drawable.getBitmap();

    }

    private  void createRoundedRectBitmap(ImageView mbitmap,Bitmap bitmap, float tl, float tr, float bl, float br) {
        //Bitmap bitmap = getBitmap(mbitmap);
        Bitmap outputImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputImage);
        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();

        float[] radii = new float[]{
                tl, tl,
                tr, tr,
                bl, bl,
                br, br
        };

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        mbitmap.setImageBitmap(outputImage);
    }

    @Override
    public void refreshAdapter() {
        /*String [] ava = db.getProQtyIfAvaliable(PRODUCT_ID,1);
        if(ava != null){
            buyBt.setText("ADDED");
            buyBt.setTextColor(this.getResources().getColor(R.color.colorWhite));
            buyBt.setBackground(this.getResources().getDrawable(R.drawable.simple_btn_bknd));
        }
        else{
            buyBt.setText("BUY");
            buyBt.setTextColor(this.getResources().getColor(R.color.colorWhite));
            buyBt.setBackground(this.getResources().getDrawable(R.drawable.single_view_bt_bkgnd));
        }*/
    }

    @Override
    public void refreshAdapter(int index, Product e) {

    }
}
