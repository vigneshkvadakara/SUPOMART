package com.lulixe.pulari.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static String BASE_URL = "https://www.lulixe.in/marketV1/";
    //public static String BASE_URL = "http://100.127.255.253:8080/market/";
    public static int MARKET_ID = 5;
    public static String productImageLink = "https://www.lulixe.in/market/getImage?path=/products/";
    public static String offerImageLink = "https://www.lulixe.in/market/getImage?path=/offers/";
    public static String categoryLink = "https://www.lulixe.in/market/getImage?path=/category/";

    public static int convertToInt(String value){
        try{
            return Integer.parseInt(value);
        }catch (Exception e){
            return 0;
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(Activity context){
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public boolean isWhatsAppInstalled(Context context){
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
