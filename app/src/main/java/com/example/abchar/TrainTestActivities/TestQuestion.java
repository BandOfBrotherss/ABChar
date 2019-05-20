package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.abchar.R;

import java.util.ArrayList;
import java.util.Random;

public class TestQuestion extends AppCompatActivity {

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        childId = getIntent().getStringExtra("childId");

        final String questionLabel = setQuestion();

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
        TextView question = (TextView) findViewById(R.id.questionLetterInfo);
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
        String content = "Show" + questionLabel;
        question.setText(content);
        return  questionLabel;
    }
}
