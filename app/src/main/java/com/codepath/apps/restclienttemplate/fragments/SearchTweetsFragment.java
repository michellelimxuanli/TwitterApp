package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by michellelim on 7/6/17.
 */

public class SearchTweetsFragment extends TweetsListFragment {

    TwitterClient client;
    String searchQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    public static SearchTweetsFragment newInstance(String q) {
        SearchTweetsFragment searchTweetsFragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("q", q);
        searchTweetsFragment.setArguments(args);
        return searchTweetsFragment;
    }

    private void populateTimeline() {
        // comes from the activity
        searchQuery = getArguments().getString("q");

        client.searchQuery(searchQuery, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    addItems(response.getJSONArray("statuses"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("TwitterClient", response.toString());
                // iterate through the JSON array
                // for each entry, deserialize the JSON object
                Log.d("Search", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Search", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Search", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Search", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }


}
