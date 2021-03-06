package com.usm.jyd.usemista.anim;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by der_w on 10/9/2015.
 */
public class AnimUtils {
    public static void scaleXY(RecyclerView.ViewHolder holder){
        holder.itemView.setScaleX(0);
        holder.itemView.setScaleY(0);

        PropertyValuesHolder propx = PropertyValuesHolder.ofFloat("scaleX", 1);
        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat("scaleY", 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(holder.itemView, propx, propy);


        animator.setDuration(800);
        animator.start();
    }

    public static void scaleY(RecyclerView.ViewHolder holder){
        holder.itemView.setScaleY(0);

        PropertyValuesHolder propy = PropertyValuesHolder.ofFloat("scaleY", 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(holder.itemView, propy);

        animator.setDuration(800);
        animator.start();
    }

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown){

        YoYo.with(Techniques.ZoomIn).duration(400).playOn(holder.itemView);

//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX" ,0.5F, 0.8F, 1.0F);
//        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.5F, 0.8F, 1.0F);
//        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 300 : -300, 0);
//        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -50, 50, -30, 30, -20, 20, -5, 5, 0);
//        animatorSet.playTogether(animatorTranslateX, animatorTranslateY, animatorScaleX, animatorScaleY);
//        animatorSet.setInterpolator(new AnticipateInterpolator());
//        animatorSet.setDuration(1000);
//        animatorSet.start();
    }
}
