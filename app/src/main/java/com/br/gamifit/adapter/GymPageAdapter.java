package com.br.gamifit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class GymPageAdapter extends FragmentPagerAdapter {
    private int numTabs;

    public GymPageAdapter(FragmentManager fm,int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }
    @Override
    public Fragment getItem(int position) {
//        switch(position){
//            case 0:
//                return new UsersSocialFragment();
//        }
        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
