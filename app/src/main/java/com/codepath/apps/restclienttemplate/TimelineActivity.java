package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {


    private final int REQUEST_CODE = 20;
    MenuItem miActionProgressItem;
    TwitterClient client;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        final TextView menuTitle = (TextView) findViewById(R.id.menuTitle);
        menuTitle.setText("Home");

        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //deserialize the user object
                try {
                    user = User.fromJSON(response);
                    ImageButton ivProfileImage = (ImageButton) findViewById(R.id.logo);
                    // load profile image
                    Glide.with(getApplicationContext()).load(user.profileImageUrl).into(ivProfileImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        // get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),this));

        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_vector_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_vector_notifications_stroke);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0){
                    tab.setIcon(R.drawable.ic_vector_home);
                    menuTitle.setText("Home");
                } else {
                    tab.setIcon(R.drawable.ic_vector_notifications);
                    menuTitle.setText("Notifications");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0){
                    tab.setIcon(R.drawable.ic_vector_home_stroke);
                } else {
                    tab.setIcon(R.drawable.ic_vector_notifications_stroke);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.fab_compose);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onComposeTweet();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("q", query);
                startActivity(i);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    public void onProfileView(View v) {
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract body
            Tweet received_tweet = (Tweet) Parcels.unwrap(intent.getParcelableExtra("new_tweet"));

            tweets.add(0, received_tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }*/


    public void onComposeTweet() {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }



    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra("tweet", Parcels.wrap(tweet));
        startActivity(i);
    }
    @Override
    public void onImageSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tweet.user.screenName);
        startActivity(i);
    }

    @Override
    public void onRefresh(boolean show) {
        miActionProgressItem.setVisible(show);
    }
}
