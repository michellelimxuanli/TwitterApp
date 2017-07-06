package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.restclienttemplate.fragments.DetailsFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setTitle("Tweet");

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        // create the user fragment
        DetailsFragment detailsFragment = DetailsFragment.newInstance(tweet);

        // display the user timeline inside the container (dynamically generated)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flDetailsContainer, detailsFragment);

        // commit the transaction
        ft.commit();


    }
    @Override
    public void onTweetSelected(Tweet tweet) {

    }

    @Override
    public void onImageSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tweet.user.screenName);
        startActivity(i);
    }

    @Override
    public void onRefresh(boolean show) {

    }
}
