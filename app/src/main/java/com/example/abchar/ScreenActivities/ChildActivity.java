package com.example.abchar.ScreenActivities;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.abchar.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChildActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean  mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_child);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);


        mOpenCvCameraView.setCvCameraViewListener(this);
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
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        Mat frameCopy = new Mat();
        frame.copyTo(frameCopy);
        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_RGBA2GRAY);
        Imgproc.cvtColor(frameCopy,frameCopy,Imgproc.COLOR_RGBA2RGB);
        Mat markerIds = new Mat();
        List<Mat> markerCorners = new ArrayList<>();
        DetectorParameters parameters = DetectorParameters.create();
        Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(frame, dictionary, markerCorners, markerIds, parameters);
        if(markerCorners.size() == 4) {


            /*Log.i("CORNER PIXEL SIZE_Width", String.valueOf(markerCorners.get(0).size().width));
            Log.i("CORNER PIXEL SIZE_Heiht", String.valueOf(markerCorners.get(0).size().height));
            for (int i = 0; i < markerCorners.get(0).size().height; i++) {
                for (int j = 0; j < markerCorners.get(0).size().width; j++) {
                    double[] a = markerCorners.get(0).get(i, j);
                    for (double numb : a) {
                        Log.i("CORNERS -----", String.valueOf(numb) + String.valueOf(j));
                    }

                }
            }*/
        }

       // Aruco.drawDetectedMarkers(frameCopy, markerCorners);



        return frameCopy;
    }
}
