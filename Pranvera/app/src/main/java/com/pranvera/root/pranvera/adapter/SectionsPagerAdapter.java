package com.pranvera.root.pranvera.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.ui.fragmentonvisit.DetailFragment;

import java.util.Locale;

/**
 * Created by root on 16/01/15.
 */
public class SectionsPagerAdapter  extends FragmentPagerAdapter {

    protected Context mContext;
    //protected Intent mIntent;
    protected String mTheValue;

    public SectionsPagerAdapter(Context context, FragmentManager fm,String theValue) {
        super(fm);
        mContext=context;
        //mIntent=intent;
        mTheValue=theValue;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch(position){
            case 0:
                Bundle detaigliBund=new Bundle();
                detaigliBund.putString("TheVisit",mTheValue);
                Fragment detaigliFragment=new DetailFragment();
                detaigliFragment.setArguments(detaigliBund);
                return detaigliFragment;

          //  case 1:
          //      Bundle galleryBund=new Bundle();
          //      galleryBund.putString("TheVisit",mTheValue);
          //      Fragment galleryFragment=new GalleryFragment();
          //      galleryFragment.setArguments(galleryBund);
          //      return galleryFragment;

        }
        return null;
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_detail).toUpperCase(l);
           // case 1:
           //     return mContext.getString(R.string.title_gallery).toUpperCase(l);
        }
        return null;
    }
}
