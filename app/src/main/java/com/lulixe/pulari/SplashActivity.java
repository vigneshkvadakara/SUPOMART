package com.lulixe.pulari;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.Utils;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }
        /*w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //status bar height
        int actionBarHeight = getActionBarHeight();
        int statusBarHeight = getStatusBarHeight();
        //action bar height
        statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;*/


        getHomeData();
        setDailyUser();
    }

    private void setDailyUser() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String url = Utils.BASE_URL+"/daily?UID="+androidId+"&marketID="+ Utils.MARKET_ID;

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responce",response);
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

    private void getHomeData(){
        String url = Utils.BASE_URL+"/homeData?marketID="+ Utils.MARKET_ID;

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String todayDate = jsonObject.getString("date");
                            Log.e("responce",response);

                            AppDb db = new AppDb(SplashActivity.this);
                            db.delAll(todayDate);

                            Intent in = new Intent(getApplicationContext(),MainActivity.class);
                            in.putExtra("data",response);
                            startActivity(in);
                            finish();

                            //getTodayDate(response);
                        }catch (Exception e){

                        }
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

    /*private void getTodayDate(final String homeResponce){
        String url = Utils.BASE_URL+"/getTodayDate";
        final Dialog d = new Dialog(SplashActivity.this);
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

        Volley.newRequestQueue(SplashActivity.this).add(postRequest);
    }*/
}
