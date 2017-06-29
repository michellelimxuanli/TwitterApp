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
        return tweet;
    }

    public String getBody() {
        return body;
    }
}
