package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.abchar.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class TestQuestion extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private String childId;
    private TextToSpeech tts;
    private String questionLabel;
    private TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        tts = new TextToSpeech(this, this);
        question = (TextView) findViewById(R.id.questionLetterInfo);

        childId = getIntent().getStringExtra("childId");

        questionLabel = setQuestion();

        Button startTestButton = (Button) findViewById(R.id.StartTest);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestQuestion.this, TestCameraActivity.class);
                i.putExtra("question", questionLabel);
                i.putExtra("childId", childId);
                startActivity(i);
            }
        });
        Button listenAgain = (Button) findViewById(R.id.ListenAgain);

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, TrainTestChooseActivity.class);
        intent.putExtra("childId",childId);
        startActivity(intent);
    }

    public String setQuestion(){

        Random random = new Random();
        ArrayList<String> labels = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
            add("D");
            add("E");
            add("0");
            add("1");
            add("2");
            add("3");
            add("4");

        }};

        int random_number = random.nextInt(labels.size());
        String questionLabel = labels.get(random_number);
        String content = "Listen and show the character you heared" ;
        question.setText(content);
        return  questionLabel;
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.7f);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    public void speakOut() {
        String commands = question.getText().toString();
        String letterString = "Show me: " + questionLabel;
        tts.speak(commands, TextToSpeech.QUEUE_FLUSH, null);


    }
}
