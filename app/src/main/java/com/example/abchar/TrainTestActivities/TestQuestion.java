package com.example.abchar.TrainTestActivities;

import android.content.Intent;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.abchar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class TestQuestion extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private String childId;
    private TextToSpeech tts;
    private String questionLabel;
    private TextView question;

    private String  childName, TAG;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        tts = new TextToSpeech(this, this);
        question = (TextView) findViewById(R.id.questionLetterInfo);

        questionLabel = setQuestion();
        db = FirebaseFirestore.getInstance();

        TAG = "TEST_QUESTION";
        childId = getIntent().getStringExtra("childId");
        childName = getIntent().getStringExtra("name");



        ImageButton startTestButton =  findViewById(R.id.StartTest);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestQuestion.this, TestCameraActivity.class);
                i.putExtra("question", questionLabel);
                i.putExtra("childId", childId);
                i.putExtra("name", childName);
                startActivity(i);
            }
        });
        ImageButton listenAgain = findViewById(R.id.ListenAgain);
        listenAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, TrainTestChooseActivity.class);
        intent.putExtra("childId",childId);
        intent.putExtra("name", childName);
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
        tts.speak(letterString, TextToSpeech.QUEUE_ADD, null);


    }
}
