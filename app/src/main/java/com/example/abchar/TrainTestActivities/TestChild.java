package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abchar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Locale;

public class TestChild extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private ImageView imgView;
    private TextView resultView, definitionView;
    private ConstraintLayout layoutt;
    private Button tryThisLetterAgain;
    private Button quizAgain;
    private String question,key;
    private String childId, childName;
    private int successTest, failTest, successNow, failNow;
    private String TAG;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Integer right, fail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        childId = i.getStringExtra("childId");

        TAG = "TEST_CHILD";
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("Children").document(childId);

        successTest = getIntent().getIntExtra("successTest", -1);
        failTest = getIntent().getIntExtra("failTest",-1);
        right = getIntent().getIntExtra("right", -1);
        fail = getIntent().getIntExtra("fail", -1);

        Log.d(TAG, String.valueOf(right));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_child);


        imgView = (ImageView) findViewById(R.id.imageView);
        resultView = (TextView) findViewById(R.id.resultOfClassification);
        definitionView = (TextView) findViewById(R.id.characterDefinition);
        layoutt = (ConstraintLayout) findViewById(R.id.layout);
        tts = new TextToSpeech(this, this);



        try {
            classifyAndSetContents();
        } catch (IOException e) {
            e.printStackTrace();
        }


        quizAgain = (Button) findViewById(R.id.quizAgain);
        quizAgain.setEnabled(false);
        //tryThisLetterAgain.setEnabled(false);

        quizAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestChild.this, TestQuestion.class);
                i.putExtra("childId", childId);
                i.putExtra("name", childName);
                startActivity(i);
            }
        });
        tryThisLetterAgain = (Button) findViewById(R.id.tryAgainButton);
        tryThisLetterAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestChild.this, TestCameraActivity.class);
                i.putExtra("question", question);
                i.putExtra("childId", childId);
                i.putExtra("name", childName);
                startActivity(i);
            }
        });

        //update();
    }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, TrainTestChooseActivity.class);
        intent.putExtra("childId",childId);
        intent.putExtra("name", childName);
        startActivity(intent);
    }

    private ImageClassifier classifier;


    private Mat getImg(Intent intent) {
        long addr = intent.getLongExtra("MyImg", 0);
        Mat tempImg = new Mat(addr);
        Mat img = tempImg.clone();
        return img;


    }

    private Bitmap convertMatToBitMap(Mat img) {
        Bitmap bmp = null;
        Mat tmp = new Mat(img.height(), img.width(), CvType.CV_8U, new Scalar(4));
        try {
            Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2RGBA, 4);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);

        } catch (CvException e) {
            Log.d("Exception", e.getMessage());
        }

        return bmp;
    }


    public void classifyAndSetContents() throws IOException {

        classifier = new MobNetClassifier(this);
        Intent intent = getIntent();
        Mat img = getImg(intent);
        Bitmap bitmapImg = convertMatToBitMap(img);
        String result = classifier.classifyFrame(bitmapImg);
        question = intent.getStringExtra("question");
        if (question.equals(result)) {
            key = question + "_T";
            docRef.update("succesTrials", successTest + 1, "true_false." + key, right + 1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

            setTrueContents(result);

        }
        if (!question.equals(result)) {
            key = question + "_F";
            docRef.update("failTrials", failTest + 1,"true_false." + key, fail + 1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

            setFalseContents(result, question, bitmapImg);
        }

        Log.d("CLASSIFIED:", result);


    }

    public void setTrueContents(String result) {


        resultView.setText("YOU FOUND IT RIGHT!");
        definitionView.setText("THIS IS : " + "  " + result);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.welldone));
        //tryThisLetterAgain.setVisibility(View.INVISIBLE);

    }

    public void setFalseContents(String result, String question, Bitmap img) {


        resultView.setText("TRY AGAIN!");
        layoutt.setBackgroundColor(getResources().getColor(R.color.tw__composer_red));
        definitionView.setText("THIS IS NOT: " + question + "\n" + "THIS IS: " + result);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.tryagain));
        //tryThisLetterAgain.setVisibility(View.VISIBLE);

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

        String classificationResult = resultView.getText().toString();
        String definitionText = definitionView.getText().toString();
        tts.speak(classificationResult, TextToSpeech.QUEUE_FLUSH, null);
        tts.speak(definitionText, TextToSpeech.QUEUE_ADD, null);
        tryThisLetterAgain.setEnabled(true);
        quizAgain.setEnabled(true);

    }
}
