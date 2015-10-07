package com.usm.jyd.usemista.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.usm.jyd.usemista.fragments.FragmentBase;
import com.usm.jyd.usemista.fragments.TestFragment;

/**
 * Created by der_w on 10/6/2015.
 */
public class AdapterViewPagerSeccionUno extends FragmentStatePagerAdapter{


    public AdapterViewPagerSeccionUno(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
       /* switch (position){
            case 0:
        }*/
        return fragment = TestFragment.newInstance("","");
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}
