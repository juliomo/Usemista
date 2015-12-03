package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.fragments.FragmentBaseMMTask;
import com.usm.jyd.usemista.fragments.FragmentBaseMisMaterias;
import com.usm.jyd.usemista.fragments.TestFragment;

/**
 * Created by der_w on 11/22/2015.
 */
public class AdapterViewPagerMisMaterias extends FragmentStatePagerAdapter {

    private Context context;
    private int[] imageResId = {
            R.drawable.ic_book_white_24dp,
            R.drawable.ic_school_white_24dp
    };

    public AdapterViewPagerMisMaterias(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position) {
            case 0: fragment = FragmentBaseMisMaterias.newInstance(0); break;
            case 1: fragment = FragmentBaseMMTask.newInstance(); break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }



  /*  @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }*/
}
