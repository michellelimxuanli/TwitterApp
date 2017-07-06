package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setTitle(null);

        String screenName = getIntent().getStringExtra("screen_name");
        // create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        // display the user timeline inside the container (dynamically generated)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, userTimelineFragment);

        // commit the transaction
        ft.commit();

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //deserialize the user object
                try {
                    User user = User.fromJSON(response);
                    // set the title of the toolbar based on the user information
                    getSupportActionBar().setTitle("@" + user.screenName);
                    // populate the user headline
                    popularUserHeadline(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        client = TwitterApplication.getRestClient();
        if (screenName == null) {
            client.getUserInfo(handler);
        }
        else {
            client.getAnotherUserInfo(screenName,handler);
        }


    }

    public void popularUserHeadline(User user) {
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageButton ivProfileImage = (ImageButton) findViewById(R.id.ivProfileImage);
        tvUsername.setText(user.name);

        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");

        // load profile image
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);

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
