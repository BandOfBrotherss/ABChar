package com.example.abchar.ScreenActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.abchar.ChildAdapter;
import com.example.abchar.FireModel;
import com.example.abchar.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.opencv.ml.ParamGrid;

import java.util.HashMap;
import java.util.Map;

public class ParentActivity extends AppCompatActivity{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childRef = db.collection("Children");

    ChildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        setUpRecyclerView();

        FloatingActionButton fab = findViewById(R.id.button_add_child);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ParentActivity.this, AddChildActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        Query query = childRef.whereEqualTo("parentid", currentUser.getUid());

        FirestoreRecyclerOptions<FireModel> options = new FirestoreRecyclerOptions.Builder<FireModel>()
                .setQuery(query, FireModel.class)
                .build();

        adapter = new ChildAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                int failTrials, successTrials, trainCount;
                HashMap trueFalse;
                String name;
                trueFalse =  (HashMap) documentSnapshot.get("true_false");
                failTrials = documentSnapshot.getLong("failTrials").intValue();
                successTrials = documentSnapshot.getLong("succesTrials").intValue();
                trainCount = documentSnapshot.getLong("trainingCount").intValue();
                name = documentSnapshot.getString("name");
                Intent i = new Intent(ParentActivity.this, ChildInfo.class);
                i.putExtra("successTrials", successTrials);
                i.putExtra("failTrials", failTrials);
                i.putExtra("trainingCount", trainCount);
                i.putExtra("trueFalse", trueFalse);
                i.putExtra("name", name);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
     }
}