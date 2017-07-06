package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.restclienttemplate.fragments.SearchTweetsFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class SearchActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.searchToolbar);
        setSupportActionBar(topToolBar);


        String searchQuery = getIntent().getStringExtra("q");
        getSupportActionBar().setTitle(searchQuery);
        // create the user fragment
        SearchTweetsFragment searchTweetsFragment = SearchTweetsFragment.newInstance(searchQuery);

        // display the user timeline inside the container (dynamically generated)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flSearchContainer, searchTweetsFragment);

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
