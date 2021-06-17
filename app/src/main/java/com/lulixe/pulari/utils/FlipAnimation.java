package com.lulixe.pulari.utils;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lulixe.pulari.R;

public class FlipAnimation {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;

    private Context context;
    private TextView frontView,backView;
    private String text;

    public FlipAnimation(Context context, /*TextView frontView, TextView backView, */String text) {
        this.context = context;
        /*this.frontView = frontView;
        this.backView = backView;*/
        frontView = ((Activity) context).findViewById(R.id.textF);
        backView = ((Activity) context).findViewById(R.id.textB);
        this.text = text;


    }

    private static int flag;
    public void start(/*int flag*/){
        loadAnimations();
        changeCameraDistance();
        flipCard(flag);

        if (flag==0){
            flag=1;
        }else
            flag=0;
    }

    private void loadAnimations() {

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = context.getResources().getDisplayMetrics().density * distance;
        frontView.setCameraDistance(scale);
        backView.setCameraDistance(scale);
    }

    //this Flip right
    private void flipRight(View li,View ro){
        mSetRightOut.setTarget(ro);
        mSetLeftIn.setTarget(li);
        mSetRightOut.start();
        mSetLeftIn.start();
    }

    //this flip left
    private void flipLeft(View ro,View li){
        mSetRightOut.setTarget(ro);
        mSetLeftIn.setTarget(li);
        mSetRightOut.start();
        mSetLeftIn.start();
    }

    private void flipCard(int flag) {

        switch (flag){
            case 0:
                mSetRightOut.setTarget(frontView);
                mSetLeftIn.setTarget(backView);
                mSetRightOut.start();
                mSetLeftIn.start();
                changeNumber(2,text);
                break;
            case 1:
                mSetRightOut.setTarget(backView);
                mSetLeftIn.setTarget(frontView);
                mSetRightOut.start();
                mSetLeftIn.start();
                changeNumber(1,text);
                break;
        }
    }

    private void changeNumber(int id,String count){
        switch (id){
            case 1:
                frontView.setText(count+"");
                break;
            case 2:
                backView.setText(count+"");
                break;
        }
    }
}
