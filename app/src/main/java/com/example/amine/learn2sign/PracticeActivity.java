package com.example.amine.learn2sign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Button;
import java.util.Random;
import com.example.amine.learn2sign.MainActivity;

import butterknife.BindView;

import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;

public class PracticeActivity extends AppCompatActivity {

    @BindView(R.id.practice_words)
    TextView practice_words;

    @BindView(R.id.practice_ip_address)
    Spinner practice_ip_address;

    //@BindView(R.id.practice_video_learn)
    VideoView practice_video_learn;

    @BindView(R.id.practice_record)
    Button practice_record;

    //@BindView(R.id.practice_new_word)
    Button practice_new_word;

    String[] array;
    String videoFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        //
        array = getResources().getStringArray(R.array.spinner_words);

        //practice_video_learn = (VideoView)findViewById(R.id.practice_video_learn);

        Button mButtonRecord = (Button)findViewById(R.id.practice_record);
        mButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick - Record Button", "Starting the Record Video Activity");
                Intent intent = new Intent(PracticeActivity.this, VideoActivity.class);
                intent.putExtra(INTENT_WORD, "Hello from Practice");
                intent.putExtra(INTENT_TIME_WATCHED, System.currentTimeMillis());
                startActivityForResult(intent, 5555);

            }
        });

        Button newword = (Button)findViewById(R.id.practice_new_word);
        newword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_video();
                //practice_video_learn.setOnCompletionListener(onCompletionListener);

            }
        });

        practice_video_learn = (VideoView)findViewById(R.id.practice_video_learn);

        play_video();

        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.start();

                }

            }
        };

        practice_video_learn.setOnCompletionListener(onCompletionListener);





    }

    public void play_video() {


        String text = array[new Random().nextInt(array.length)];

        TextView textview = (TextView) findViewById(R.id.practice_words);
        textview.setText(text);

        String path = "";
        String old_text = "";

        //old_text = text;
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
        else{
            Log.d("not","found");
        }
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);

            VideoView practice_video_learn = (VideoView)findViewById(R.id.practice_video_learn);
            //practice_video_learn.setOnCompletionListener(onCompletionListener);
            practice_video_learn.setVideoURI(uri);
            practice_video_learn.start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.e("OnActivityresult",requestCode+" "+resultCode);
        if(requestCode == 5555 && resultCode == 8888){
            if(intent.hasExtra(INTENT_URI)) {
                TextView textview = (TextView) findViewById(R.id.practice_words);
                videoFileName = intent.getStringExtra(INTENT_URI);
                Intent t = new Intent(this, RateActivity.class);
                t.putExtra(INTENT_WORD, textview.getText());
                t.putExtra(INTENT_URI, videoFileName);
                startActivityForResult(t, 5555);
            }
        }
    }
}
