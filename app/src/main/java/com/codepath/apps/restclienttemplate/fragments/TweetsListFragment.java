package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by michellelim on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener{

    TweetAdapter tweetAdapter;
    TweetSelectedListener listener;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public SwipeRefreshLayout swipeContainer;
    public interface TweetSelectedListener {
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
        public void onImageSelected(Tweet tweet);
        public void onRefresh(boolean show);

    }

    //inflation happens inside on CreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        //find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);

        ((AppCompatActivity) getActivity()).getSupportActionBar();

        //add item decorator
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvTweets.addItemDecoration(itemDecoration);


        // init the arrayList (data source)
        tweets = new ArrayList<>();

        Typeface tf;
        Typeface tf_bold;
        tf = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeueLT-Roman.ttf");
        tf_bold = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeue-Bold.ttf");

        // construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets, this, tf, tf_bold);
        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    public void fetchTimelineAsync () {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TweetSelectedListener) {
            listener = (TweetSelectedListener) context;
        }
    }

    public void addItems(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            // convert each object to a Tweet model
            // add that Tweet model to our data source
            // notify the adapter that we've added an item
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size()-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onItemSelected(View view, int position, boolean isPic) {
        Tweet tweet = tweets.get(position);
        if (isPic) {
            ((TweetSelectedListener) getActivity()).onImageSelected(tweet);
        } else{
            ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
        }
    }
}
