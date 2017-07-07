package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by michellelim on 7/6/17.
 */

public class DetailsFragment extends Fragment {

    TwitterClient client;
    Tweet tweet;
    ImageButton details_ivProfileImage;
    TextView details_tvUsername;
    TextView details_tvBody;
    TextView details_tvCreatedAt;
    TextView details_tvUserhandle;
    TextView details_tvRetweetCount;
    ImageButton details_ibRetweet;
    TextView details_tvLikeCount;
    ImageButton details_ibLike;
    ImageButton details_ibReply;
    ImageView details_ivPhoto;


    public static DetailsFragment newInstance(Tweet tweet) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", Parcels.wrap(tweet));
        detailsFragment.setArguments(args);
        return detailsFragment;
    };

    //inflation happens inside on CreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragment_details_view, container, false);
        client = TwitterApplication.getRestClient();
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        details_ivProfileImage = (ImageButton) v.findViewById(R.id.details_ivProfileImage);
        details_tvUsername = (TextView) v.findViewById(R.id.details_tvUsername);
        details_tvBody = (TextView) v.findViewById(R.id.details_tvBody);
        details_tvCreatedAt = (TextView) v.findViewById(R.id.details_tvCreatedAt);
        details_tvUserhandle = (TextView) v.findViewById(R.id.details_tvUserhandle);
        details_tvRetweetCount = (TextView) v.findViewById(R.id.details_tvRetweetCount);
        details_ibRetweet = (ImageButton) v.findViewById(R.id.details_ibRetweet);
        details_tvLikeCount = (TextView) v.findViewById(R.id.details_tvLikeCount);
        details_ibLike = (ImageButton) v.findViewById(R.id.details_ibLike);
        details_ibReply = (ImageButton) v.findViewById(R.id.details_ibReply);
        details_ivPhoto = (ImageView) v.findViewById(R.id.details_ivPhoto);
        tweet = Parcels.unwrap(getArguments().getParcelable("tweet"));

        // populate the views according to this data
        details_tvUsername.setText(tweet.user.name);
        details_tvBody.setText(tweet.body);
        details_tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));
        details_tvUserhandle.setText("@"+tweet.user.screenName);

        if (tweet.retweetMode==true)
        {
            details_ibRetweet.setColorFilter(0xff17bf63);
            details_tvRetweetCount.setTextColor(0xff17bf63);
        }
        if (tweet.favoriteMode==true)
        {
            details_ibLike.setColorFilter(0xffe0245e);
            details_tvLikeCount.setTextColor(0xffe0245e);
        }
        if (tweet.retweetCount > 0){
            details_tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        } else
        {
            details_tvRetweetCount.setText("");
        }
        if (tweet.favoriteCount > 0){
            details_tvLikeCount.setText(String.valueOf(tweet.favoriteCount));
        } else
        {
            details_tvLikeCount.setText("");
        }
        Context context = getContext();

        Glide.with(this).load(tweet.user.profileImageUrl).bitmapTransform(new RoundedCornersTransformation(context, 5, 2)).into(details_ivProfileImage);
        Glide.with(this).load(tweet.imageUrl).bitmapTransform(new RoundedCornersTransformation(context, 5, 2)).into(details_ivPhoto);

        details_ibRetweet.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                    client.retweet(tweet.uid, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            //super.onSuccess(statusCode, headers, response);
                            Log.d("Retweet", "Retweet works!");
                            try {
                                details_tvRetweetCount.setText(String.valueOf(Tweet.fromJSON(response).retweetCount));
                                details_ibRetweet.setColorFilter(0xff17bf63);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

            }
        });
        details_ibLike.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                // make sure the position is valid, i.e. actually exists in the view
            client.favorite(tweet.uid, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    Log.d("Favorite", "Favorite works!");
                    try {
                        details_tvLikeCount.setText(String.valueOf(Tweet.fromJSON(response).favoriteCount));
                        details_ibLike.setColorFilter(0xffe0245e);
                        details_tvLikeCount.setTextColor(0xffe0245e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

            }
        });
        details_ibReply.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getContext(), ComposeActivity.class);
                    i.putExtra("username", tweet.user.screenName);
                    ((AppCompatActivity) getContext()).startActivityForResult(i, 20);
            }
        });
    }



    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            relativeDate = abbrev(relativeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String abbrev(String agoDate) {
        if (agoDate.contains("ago")){
            agoDate = agoDate.replace(" ago", "");
        }
        if (agoDate.contains("minutes")){
            agoDate = agoDate.replace(" minutes", "m");
        }
        else if (agoDate.contains(" seconds")){
            agoDate = agoDate.replace(" seconds", "s");
        }
        else if (agoDate.contains(" hours")){
            agoDate = agoDate.replace(" hours", "h");
        }
        else if (agoDate.contains(" hour")){
            agoDate = agoDate.replace(" hour", "h");
        }
        else if (agoDate.contains(" second")){
            agoDate = agoDate.replace(" second", "s");
        }
        else if (agoDate.contains(" minute")){
            agoDate = agoDate.replace(" minute", "m");
        }
        return agoDate;

    }
}
