package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.abchar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrainTestChooseActivity extends AppCompatActivity {

    private String childrenID, childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_test_choose);

        Intent i = getIntent();
        childrenID = i.getStringExtra("childId");
        childName = i.getStringExtra("name");
        Log.d("MESSAGE", childrenID);
        Button trainButton = (Button) findViewById(R.id.trainButton);
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainMode();
            }
        });

        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMode();
            }
        });
    }


    public void trainMode(){

        Intent i = new Intent(TrainTestChooseActivity.this, TrainCameraActivity.class);
        i.putExtra("childId", childrenID);
        i.putExtra("name", childName);
        startActivity(i);

    }

    public void testMode(){
        Intent i = new Intent(TrainTestChooseActivity.this, TestQuestion.class);
        i.putExtra("childId", childrenID);
        i.putExtra("name", childName);
        startActivity(i);

    }

}
