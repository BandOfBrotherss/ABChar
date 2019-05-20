package com.example.abchar.ScreenActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.abchar.ChildAdapter;
import com.example.abchar.R;
import com.example.abchar.TrainTestActivities.TrainTestChooseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childRef = db.collection("Children");
    ChildAdapter adapter;
    private List<String> childrenNames;
    private List<String> childrenIDs;
    private ArrayAdapter<String> childAdapter;
    ListView dialogList;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ImageButton parent = findViewById(R.id.parent);
        final ImageButton child = findViewById(R.id.child);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ParentActivity.class);
                startActivity(i);
            }
        });

        child.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                childrenNames = new ArrayList<>();
                childrenIDs = new ArrayList<>();
                getChildren();
            }
        });
    }

    private void getChildren() {
        childRef.whereEqualTo("parentid", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                childrenNames.add(document.getString("name"));
                                childrenIDs.add(document.getId());
                                Log.d("sss", childrenIDs.get(0) + " => " + document.getData());
                            }
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                            dialogBuilder.setTitle("Select User");
                            final CharSequence[] Children = childrenNames.toArray(new String[childrenNames.size()]);
                            dialogBuilder.setItems(Children, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("WHICH", String.valueOf(which));
                                    Intent i = new Intent(HomeActivity.this, TrainTestChooseActivity.class);
                                    i.putExtra("childId", childrenIDs.get(which));
                                    startActivity(i);
                                }
                            });
                            AlertDialog dialogObject = dialogBuilder.create();
                            dialogObject.show();
                        } else {
                            Log.d("ss", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
