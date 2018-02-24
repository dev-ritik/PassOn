package com.example.android.passon;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ashutoshchaubey on 28/01/18.
 */

/*
This class handles backend of swiping fragment in main screen
 */
public class PassOnFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Books","Requests" };
    private Context context;

    public PassOnFragmentPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context=context;

    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            return new PostFragment();
        }else{
            return new RequestFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
