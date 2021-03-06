package com.example.abchar.TrainTestActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.abchar.R;
import com.example.abchar.Warper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import java.util.ArrayList;
import java.util.List;

public class TrainCameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean  mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    Mat frame;
    Mat frameCopy;
    Mat markerIds;
    List<Mat> markerCorners;
    DetectorParameters parameters;
    Dictionary dictionary;
    Warper warper;


    private String childId, childName;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        childId = getIntent().getStringExtra("childId");
        childName = getIntent().getStringExtra("name");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_train_camera);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);


        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, TrainTestChooseActivity.class);
        intent.putExtra("childId",childId);
        intent.putExtra("name", childName);
        startActivity(intent);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

        frame = new Mat(height, width, CvType.CV_8UC4);
        dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
        warper = new Warper();

    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        frame = inputFrame.rgba();
        frameCopy = new Mat();
        frame.copyTo(frameCopy);
        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_RGBA2GRAY);
        Imgproc.cvtColor(frameCopy,frameCopy,Imgproc.COLOR_RGBA2RGB);
        markerIds = new Mat();
        markerCorners = new ArrayList<>();
        parameters = DetectorParameters.create();
        Aruco.detectMarkers(frame, dictionary, markerCorners, markerIds, parameters);
        if(markerCorners.size() == 4){
            warper.setCorners(markerCorners);
                Mat warped = warper.warp(frameCopy,128,128);
            //Imgproc.resize(warped, warped ,frame.size());
            long img_addr = warped.getNativeObjAddr();
            Intent camera = new Intent(TrainCameraActivity.this, TrainActivity.class);
            camera.putExtra("MyImg", img_addr);
            camera.putExtra("childId", childId);
            camera.putExtra("name", childName);
            startActivity(camera);
            //return warped;

        }
        return frameCopy;
    }
}
