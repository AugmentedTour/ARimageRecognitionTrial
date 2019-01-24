package com.example.robin.imagerecdemo;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {
    @Override
    protected Config getSessionConfiguration(Session session) {
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        //note to self: this line here allows the camera to actually focus properly.
        config.setFocusMode(Config.FocusMode.AUTO);
        session.configure(config);
        getArSceneView().setupSession(session);

        if ((((MainActivity) getActivity()).setupAugmentedImagesDb(config, session))) {
            Log.d("SetupAugImgDb", "Success");
        } else {
            Log.e("SetupAugImgDb","Failure setting up db");
        }

        return config;
    }



}