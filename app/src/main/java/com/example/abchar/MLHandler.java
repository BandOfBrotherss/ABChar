package com.example.abchar;

import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;

public class MLHandler {
    public  MLHandler(){


    }

    private void configureLocalModelSource(){
        FirebaseLocalModel localSource =
                new FirebaseLocalModel.Builder("my_local_model")  // Assign a name to this model
                        .setAssetFilePath("my_model.tflite")
                        .build();
        FirebaseModelManager.getInstance().registerLocalModel(localSource);

    }

}
