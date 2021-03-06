package com.example.amine.learn2sign;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.stetho.Stetho;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import cz.msebera.android.httpclient.Header;

import static android.provider.MediaStore.EXTRA_DURATION_LIMIT;
import static android.provider.MediaStore.EXTRA_MEDIA_TITLE;
import static com.example.amine.learn2sign.LoginActivity.INTENT_EMAIL;
import static com.example.amine.learn2sign.LoginActivity.INTENT_ID;
import static com.example.amine.learn2sign.LoginActivity.INTENT_SERVER_ADDRESS;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;


public class RateActivity extends AppCompatActivity {

    String practice_word;
    String videoFilePath;
    RateActivity activity;
    Intent returnIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        returnIntent = new Intent();
        activity = this;
        final VideoView video_view = (VideoView) findViewById(R.id.r_video_learn);
        VideoView video_record_view = (VideoView) findViewById(R.id.r_record);
        if(getIntent().hasExtra(INTENT_WORD)) {
            practice_word = getIntent().getStringExtra(INTENT_WORD);
        }
        if(getIntent().hasExtra(INTENT_URI)) {
            videoFilePath = getIntent().getStringExtra(INTENT_URI);
        }


        play_video(practice_word);
        play_record_video(videoFilePath);
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.start();

                }

            }
        };

        video_view.setOnCompletionListener(onCompletionListener);
        video_record_view.setOnCompletionListener(onCompletionListener);

        Button accept = (Button)findViewById(R.id.Accept_word);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Rate Activity", "The rate preview accepted");
//                Intent t = new Intent(RateActivity.this,PracticeActivity.class);
                upload_file(videoFilePath);
                returnIntent.putExtra(INTENT_WORD, "Accepted");
                activity.setResult(2000, returnIntent);
                activity.finish();
            }
        });

        Button decline = (Button)findViewById(R.id.Decline_word);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                This needs to be implemented. It should upload the file and then go back to practice
                with a new word.
                */
                Log.d("Rate Activity", "The rate preview declined");
//                Intent t = new Intent(RateActivity.this,PracticeActivity.class);
                returnIntent.putExtra(INTENT_WORD, "Declined");
                activity.setResult(2000, returnIntent);
                activity.finish();
            }
        });
    }

    public void play_record_video(String path) {
        if(!path.isEmpty()) {

            Uri uri = Uri.parse(path);
            VideoView video_view = (VideoView) findViewById(R.id.r_record);
            video_view.setVideoURI(uri);
            video_view.start();

        }
    }

    public void upload_file(String fileName) {
        String id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENT_ID,"00000000");
        String server_ip = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE).getString(INTENT_SERVER_ADDRESS,"10.211.17.171");
        RequestParams params = new RequestParams();
        final File m = new File(fileName);
        if(m.exists()) {
            Log.d("File", "File found");
        }
        try {
            params.put("uploaded_file", m);
            params.put("name", id);
            params.put("tmp_name", id);
        } catch (FileNotFoundException e) {}
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://"+server_ip +"/upload_video_performance.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("msg success", statusCode+"");
                if (statusCode == 200) {
                   Toast.makeText(RateActivity.this, "Success uploading the video", Toast.LENGTH_SHORT).show();
                   m.delete();
                } else  {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("msg fail", "Something went wrong");
                Toast.makeText(RateActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                Log.d("msg progress",bytesWritten + " out of " + totalSize);

                super.onProgress(bytesWritten, totalSize);
            }
        });
    }


    public void play_video(String text) {
        String old_text = text;
        String path = "";
        if(text.equals("About")) {

            path = "android.resource://" + getPackageName() + "/" + R.raw._about;
        } else if(text.equals("And")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._and;
        } else if (text.equals("Can")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._can;
        }else if (text.equals("Cat")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._cat;
        }else if (text.equals("Cop")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._cop;
        }else if (text.equals("Cost")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._cost;
        }else if (text.equals("Day")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._day;
        }else if (text.equals("Deaf")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._deaf;
        }else if (text.equals("Decide")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._decide;
        }else if (text.equals("Father")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._father;
        }else if (text.equals("Find")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._find;
        }else if (text.equals("Go Out")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._go_out;
        }else if (text.equals("Gold")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._gold;
        }else if (text.equals("Goodnight")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._good_night;
        }else if (text.equals("Hearing")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hearing;
        }else if (text.equals("Here")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._here;
        }else if (text.equals("Hospital")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hospital;
        }else if (text.equals("Hurt")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hurt;
        }else if (text.equals("If")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._if;
        }else if (text.equals("Large")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._large;
        }else if (text.equals("Hello")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hello;
        }else if (text.equals("Help")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._help;
        }else if (text.equals("Sorry")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._sorry;
        }else if (text.equals("After")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._after;
        }else if (text.equals("Tiger")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._tiger;
        }


        if(!path.isEmpty()) {

            Uri uri = Uri.parse(path);
            VideoView video_view = (VideoView) findViewById(R.id.r_video_learn);
            video_view.setVideoURI(uri);
            video_view.start();

        }

    }
}
