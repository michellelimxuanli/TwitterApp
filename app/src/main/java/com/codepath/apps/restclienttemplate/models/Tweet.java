package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by michellelim on 6/26/17.
 */

@Parcel
public class Tweet {

    //list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;
    public long retweetCount;
    public long favoriteCount;
    public boolean retweetMode;
    public boolean favoriteMode;
    public String imageUrl;

    //empty constructor needed by Parcelable
    public Tweet() {
    }

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.retweetCount = jsonObject.getLong("retweet_count");
        tweet.favoriteCount = jsonObject.getLong("favorite_count");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweetMode = jsonObject.getBoolean("retweeted");
        tweet.favoriteMode = jsonObject.getBoolean("favorited");
        //todo: choose media according to photo type
        try {
        tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
        } catch(JSONException e)
        {
            tweet.imageUrl = "";
        };
        return tweet;
    }

    public String getBody() {
        return body;
    }
}
