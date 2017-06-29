package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by michellelim on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;
    private Typeface tf;
    private Typeface tf_bold;

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, Typeface typeface, Typeface typeface2) {
        mTweets = tweets;
        tf = typeface;
        tf_bold = typeface2;

    }
    //for each row, inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView, tf, tf_bold);
        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tvUserhandle.setText("@"+tweet.user.screenName);
        if (tweet.retweetCount > 0){
        holder.tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        } else
        {
            holder.tvRetweetCount.setText("");
        }

        Glide.with(context).load(tweet.user.profileImageUrl).bitmapTransform(new RoundedCornersTransformation(context, 5, 2)).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
    //create Viewholder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public TextView tvUserhandle;
        public TextView tvRetweetCount;



        public ViewHolder(View itemView, Typeface tf, Typeface tf_bold) {
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            tvUserhandle = (TextView) itemView.findViewById(R.id.tvUserhandle);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);

            tvUsername.setTypeface(tf_bold);
            tvBody.setTypeface(tf);
            tvCreatedAt.setTypeface(tf);
            tvUserhandle.setTypeface(tf);


        }
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

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}


