package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "COMPOSE ACTIVITY";
    public static final int MAX_LENGTH = 280;

    EditText etCompose;
    Button btnTweet;

    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client  = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent =  etCompose.getText().toString();
                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, Your tweet cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, Your tweet cannot be more than 140 characters.", Toast.LENGTH_LONG).show();
                    return;
                }

                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"ONSUCCESS to publish tweet");
                        try {
                            Tweet tweet =  Tweet.fromJson(json.jsonObject);
                            Log.i(TAG,"Publish tweet: "+ tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"ONFAILURE to publish tweet",throwable);
                    }
                });


            }
        });
    }
}