package com.example.abchar.ScreenActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abchar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChildActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "AddChildrenActivity";

    EditText name, age;
    Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        name = findViewById(R.id.add_name);
        age = findViewById(R.id.add_age);
        add_button = findViewById(R.id.add_button);
        add_button.setEnabled(false);


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enable_button();
            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enable_button();

            }
        });


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        Log.d(TAG, currentUser.getUid());


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map letter_map = getMap();
                Map<String, Object> user = new HashMap<>();
                user.put("name", name.getText().toString());
                user.put("age", Integer.valueOf(age.getText().toString()));
                user.put("parentid", currentUser.getUid());
                user.put("usageTime", 0);
                user.put("failTrials", 0);
                user.put("succesTrials", 0);
                user.put("trainingCount", 0);
                user.put("true_false",letter_map);



                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Children")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent i = new Intent(AddChildActivity.this,ParentActivity.class);
                                startActivity(i);
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });


    }


    private Map getMap() {
        ArrayList<String> letters = new ArrayList<>();
        letters.add("0_T");
        letters.add("0_F");
        letters.add("1_T");
        letters.add("1_F");
        letters.add("2_T");
        letters.add("2_F");
        letters.add("3_T");
        letters.add("3_F");
        letters.add("4_T");
        letters.add("4_F");
        letters.add("A_T");
        letters.add("A_F");
        letters.add("B_T");
        letters.add("B_F");
        letters.add("C_T");
        letters.add("C_F");
        letters.add("D_T");
        letters.add("D_F");
        letters.add("E_T");
        letters.add("E_F");
        Map<String, Integer> letter_map = new HashMap<String, Integer>();
        for( String key : letters) {
            letter_map.put(key,0);
        }

        return letter_map;
    }

    private void enable_button(){
        String name_input = name.getText().toString();
        String age_input = age.getText().toString();
        if (name_input.trim().length() != 0 && age_input.trim().length() !=0) {
            try {
                int num = Integer.valueOf(age_input);
                add_button.setEnabled(true);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Age must be number.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}