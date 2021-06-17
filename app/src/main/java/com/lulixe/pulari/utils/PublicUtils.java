package com.lulixe.pulari.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PublicUtils {

    public Bitmap getBitmap(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        return drawable.getBitmap();
    }

    public void createRoundedRectBitmap(ImageView mbitmap, float tl, float tr, float bl, float br) {
        Bitmap bitmap = getBitmap(mbitmap);
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

    public float convertDpToPx(Context context,float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private Bitmap setBitmap( Context context,Bitmap image,float BITMAP_SCALE,float BLUR_RADIUS){
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = null;
        rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }
        return outputBitmap;
    }

    public byte[] convertByteToBitmap(Context context,String src){

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            Bitmap bytBitmap=roundedRectBitmap(myBitmap,convertDpToPx(context,10),convertDpToPx(context,10),
                    convertDpToPx(context,10),convertDpToPx(context,10));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bytBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] b = baos.toByteArray();
            //String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            return b;

        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    private  Bitmap roundedRectBitmap(Bitmap bitmap, float tl, float tr, float bl, float br) {
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
        //mbitmap.setImageBitmap(outputImage);
        return outputImage;
    }
}
