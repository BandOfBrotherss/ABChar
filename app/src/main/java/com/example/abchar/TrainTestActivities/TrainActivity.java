package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abchar.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.HashMap;
import java.util.Locale;

public class TrainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private ImageClassifier classifier;
    private TextView info, definition;
    private ImageView image;
    private Button continueButton;
    private FirebaseFirestore db;
    private String TAG, childId;
    private int traincounts;
    private String name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_child);
        TAG = "TRAIN_ACTIVITY";

        Intent i = getIntent();
        childId = i.getStringExtra("childId");
        Log.d("MESSAGE", childId);
        //getTrainingCounts();

        db = FirebaseFirestore.getInstance();
        tts = new TextToSpeech(this, this);
        info = (TextView) findViewById(R.id.informationView);
        definition = (TextView) findViewById(R.id.characterDefinition);
        image = (ImageView) findViewById(R.id.imageView);
        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setEnabled(false);
        try {
            getTrainingCounts();
            classify();
        } catch (IOException e) {
            e.printStackTrace();
        }
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrainActivity.this, TrainCameraActivity.class);
                i.putExtra("childId", childId);
                startActivity(i);
            }
        });

    }


    private void getTrainingCounts() {
        DocumentReference docRef = db.collection("Children").document(childId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    traincounts = document.getLong("trainingCount").intValue();
                    name = document.getString("name");

                    DocumentReference child = db.collection("Children").document(childId);
                    child.update("trainingCount", traincounts + 1);
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


    public void classify() throws IOException {


        classifier = new MobNetClassifier(this);
        Mat img = getImg();
        Bitmap bitmapImg = convertMatToBitMap(img);
        String result = classifier.classifyFrame(bitmapImg);
        setContents(result);
        Log.d("CLASSIFIED:", result);


    }

    private Mat getImg() {
        Intent intent = getIntent();
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


    private void setContents(String result) {
        String definitionText;
        String infoText;


        HashMap<String, String> characterMap = setCharacterMap();
        HashMap<String, Drawable> drawableMap = setDrawableMap();
        if (isNumeric(result)) {
            definitionText = "This is :" + result;
            infoText = "There are " + result + " finger(s) above!";

        } else {
            definitionText = "This is : " + result;
            infoText = characterMap.get(result);

        }
        definition.setText(definitionText);
        info.setText(infoText);
        image.setImageDrawable(drawableMap.get(result));


    }

    private HashMap<String, String> setCharacterMap() {
        HashMap<String, String> infos = new HashMap<String, String>();

        infos.put("A", "There is an apple above, apple starts with an 'A'!");
        infos.put("B", "There is a bird above, bird starts with a 'B'!");
        infos.put("C", "There is a cat above, cat starts with 'C'!");
        infos.put("D", "There is a dog above, dog starts with 'D'!");
        infos.put("E", "There is an elephant above, elephant starts with 'E'!");
        return infos;

    }


    private HashMap<String, Drawable> setDrawableMap() {
        HashMap<String, Drawable> drawableMap = new HashMap<String, Drawable>();
        Drawable a = getResources().getDrawable(R.drawable.a);
        Drawable b = getResources().getDrawable(R.drawable.b);
        Drawable c = getResources().getDrawable(R.drawable.c);
        Drawable d = getResources().getDrawable(R.drawable.d);
        Drawable e = getResources().getDrawable(R.drawable.e);
        Drawable zero = getResources().getDrawable(R.drawable.zero);
        Drawable one = getResources().getDrawable(R.drawable.one);
        Drawable two = getResources().getDrawable(R.drawable.two);
        Drawable three = getResources().getDrawable(R.drawable.three);
        Drawable four = getResources().getDrawable(R.drawable.four);
        drawableMap.put("A", a);
        drawableMap.put("B", b);
        drawableMap.put("C", c);
        drawableMap.put("D", d);
        drawableMap.put("E", e);
        drawableMap.put("0", zero);
        drawableMap.put("1", one);
        drawableMap.put("2", two);
        drawableMap.put("3", three);
        drawableMap.put("4", four);
        return drawableMap;

    }

    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
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
        continueButton.setEnabled(true);

    }

    public void speakOut() {
        String information = info.getText().toString();
        String definitionString = definition.getText().toString();
        tts.speak(definitionString, TextToSpeech.QUEUE_FLUSH, null);
        tts.speak(information, TextToSpeech.QUEUE_ADD, null);


    }
}
