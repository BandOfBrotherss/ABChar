package com.example.abchar;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abchar.ImageClassifier;
import com.example.abchar.MobNetClassifier;
import com.google.zxing.common.StringUtils;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import io.opencensus.internal.StringUtil;

public class TrainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_child);
        try {
            classify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageClassifier classifier;

    public void classify() throws IOException {

        classifier = new MobNetClassifier(this);
        Mat img = getImg();
        Bitmap bitmapImg = convertMatToBitMap(img);
        String result = classifier.classifyFrame(bitmapImg);
        setContents(result);
        Log.d("CLASSIFIED:",result);



    }

    private Mat getImg(){
        Intent intent = getIntent();
        long addr = intent.getLongExtra("MyImg", 0);
        Mat tempImg = new Mat(addr);
        Mat img = tempImg.clone();
        return img;


    }

    private Bitmap convertMatToBitMap(Mat img){
        Bitmap bmp = null;
        Mat tmp = new Mat (img.height(), img.width(), CvType.CV_8U, new Scalar(4));
        try{
            Imgproc.cvtColor(img, tmp, Imgproc.COLOR_RGB2RGBA, 4);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);

        }catch (CvException e){Log.d("Exception",e.getMessage());}

        return bmp;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("train_data", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void setContents(String result){
        TextView info = (TextView)findViewById(R.id.informationView);
        TextView definition = (TextView)findViewById(R.id.characterDefinition);
        ImageView image =(ImageView)findViewById(R.id.imageView);

        HashMap<String, String> characterMap = setCharacterMap();
        HashMap<String,Drawable> drawableMap = setDrawableMap();
        if (isNumeric(result)){

           definition.setText("This is -> " + result);
           info.setText("There are " + result + " finger(s) above!");

       }
       else{
           definition.setText("This is -> " + result);
           info.setText(characterMap.get(result));

       }

       image.setImageDrawable(drawableMap.get(result));


    }

    private HashMap <String, String> setCharacterMap(){
        HashMap<String, String> infos = new HashMap<String, String>();
        ArrayList <String> outputs= new ArrayList<String>();
        infos.put("a", "There is an apple above, apple starts with an 'A'!");
        infos.put("b", "There is a bird above, bird starts with a 'B'!");
        infos.put("c", "There is a cat above, cat starts with 'C'!");
        infos.put("d", "There is a dog above, dog starts with 'D'!");
        infos.put("e", "There is an elephant above, elephant starts with 'E'!");
        return infos;

    }



    private HashMap<String, Drawable> setDrawableMap(){
        HashMap<String, Drawable> drawableMap = new HashMap<String, Drawable>();
        Drawable a =getResources().getDrawable(R.drawable.a);
        Drawable b =getResources().getDrawable(R.drawable.b);
        Drawable c =getResources().getDrawable(R.drawable.c);
        Drawable d =getResources().getDrawable(R.drawable.d);
        Drawable e =getResources().getDrawable(R.drawable.e);
        Drawable zero =getResources().getDrawable(R.drawable.zero);
        Drawable one =getResources().getDrawable(R.drawable.one);
        Drawable two =getResources().getDrawable(R.drawable.two);
        Drawable three =getResources().getDrawable(R.drawable.three);
        Drawable four =getResources().getDrawable(R.drawable.four);
        drawableMap.put("a",a);
        drawableMap.put("b",b);
        drawableMap.put("c",c);
        drawableMap.put("d",d);
        drawableMap.put("e",e);
        drawableMap.put("0",zero);
        drawableMap.put("1",one);
        drawableMap.put("2",two);
        drawableMap.put("3",three);
        drawableMap.put("4",four);
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
}
