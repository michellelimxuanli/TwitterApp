package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by michellelim on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public ArrayList<TweetsListFragment> mFragmentReferences = new ArrayList<>();

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
      super(fm);
        this.context = context;
    };
    //return total number of fragments
    @Override
    public int getCount() {
        return 3;
    }

    //return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            HomeTimelineFragment newFrag = new HomeTimelineFragment();
            mFragmentReferences.add(0, newFrag);
            return newFrag;
        } else if (position == 1) {
            MentionsTimelineFragment newFrag = new MentionsTimelineFragment();
            mFragmentReferences.add(1, newFrag);
            return newFrag;
        } else if (position == 2) {
            MentionsTimelineFragment newFrag = new MentionsTimelineFragment();
            mFragmentReferences.add(2, newFrag);
            return newFrag;
        } else {
            return null;
        }
    }

}
