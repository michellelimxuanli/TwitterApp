package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {


    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        // get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),this));

        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                onComposeTweet();
                return true;
            case R.id.miProfile:
                onProfileView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onProfileView() {
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
        Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onImageSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tweet.user.screenName);
        startActivity(i);
    }
}
