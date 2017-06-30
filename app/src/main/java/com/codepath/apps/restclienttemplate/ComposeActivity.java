package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    String tweet_body;
    EditText etCompose;
    Button btSubmit;
    TextView tvTweetcount;
    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        etCompose = (EditText) findViewById(R.id.etCompose);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        tvTweetcount = (TextView) findViewById(R.id.tvTweetcount);

        String repliee = getIntent().getStringExtra("username");
        if (repliee!=null){
            etCompose.setText("@" + repliee);
            etCompose.setSelection(etCompose.getText().length());
        }



        client = TwitterApplication.getRestClient();
        // from https://stackoverflow.com/questions/3013791/live-character-count-for-edittext
        etCompose.addTextChangedListener(mTextEditorWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miSubmit:
                onTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTweet(){

        tweet_body = etCompose.getText().toString();
        client.sendTweet(tweet_body, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent();
                    intent.putExtra("new_tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
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

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvTweetcount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
