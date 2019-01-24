package com.example.robin.imagerecdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


public class MainActivity extends AppActivityBuilderMethods{
        ArFragment arFragment;
        boolean shouldAddModel = true;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
            arFragment.getPlaneDiscoveryController().hide();
            arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void placeObject(ArFragment arFragment, Anchor anchor, Uri uri) {
            ModelRenderable.builder()
                    .setSource(arFragment.getContext(), uri)
                    .build()
                    .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                    .exceptionally(throwable -> {
                                Toast.makeText(arFragment.getContext(), "Error:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                return null;
                            }
                    );
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void placeXML(ArFragment arFragment, Anchor anchor, int file) {
            ViewGroup group = (ViewGroup) View.inflate(this, R.layout.activity_main2, null);
            LinearLayout topLayout = group.findViewById(R.id.topLayout);
            LinearLayout bodyLayout = group.findViewById(R.id.bodyLayout);


            titleBuilder("R Building", topLayout);
            hasAllGendersBathroom(topLayout);
            hasComputers(topLayout);

            String info = "The R building is home to the arts and humanities division." +
                    " ESL classes can also be found here, and there's a dance studio downstairs." +
                    " A cafe sells coffee on the first floor."; //will want to alter later

            textViewBuilder(info, bodyLayout);
            textViewBuilder("Human Resources (HR): Location R130(425) | Fax 564-3173", bodyLayout);
            phoneBuilder("HR", "564-2274(425)", bodyLayout);

//            ViewGroup group2 = (ViewGroup) View.inflate(this, R.layout.activity_main2, null);
//            activityButtonBuilder("Arts and Humanities", group.getContext(), group2.getClass(), false, bodyLayout);
//
//            LinearLayout topLayout2 = group2.findViewById(R.id.topLayout);
//            LinearLayout bodyLayout2 = group2.findViewById(R.id.bodyLayout);
//
//            subTitleBuilder("Arts and Humanities Departments", topLayout2);



            //LinearLayout layout = group.findViewById(R.id.testLinearLayout);
            //TextView text = new TextView(this);
            //text.setText("Test Test");
            //layout.addView(text);

            ViewRenderable.builder()
                    .setView(this, group)
                    .setVerticalAlignment(ViewRenderable.VerticalAlignment.BOTTOM)
                    .setHorizontalAlignment(ViewRenderable.HorizontalAlignment.LEFT)
                    .build()
                    .thenAccept(renderable -> addNodeToScene(arFragment, anchor, renderable));

        }



        @RequiresApi(api = Build.VERSION_CODES.N)
        private void onUpdateFrame(FrameTime frameTime) {
            Frame frame = arFragment.getArSceneView().getArFrame();
            Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
            for (AugmentedImage augmentedImage : augmentedImages) {
                if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                    if (augmentedImage.getName().equals("model") && shouldAddModel) {
                        placeXML(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), R.layout.hello_instructions);
                        shouldAddModel = false;
                    }
                }
            }
        }
        public boolean setupAugmentedImagesDb(Config config, Session session) {
            AugmentedImageDatabase augmentedImageDatabase;
            Bitmap bitmap = loadAugmentedImage();
            if (bitmap == null) {
                return false;
            }
            augmentedImageDatabase = new AugmentedImageDatabase(session);
            augmentedImageDatabase.addImage("model", bitmap);
            config.setAugmentedImageDatabase(augmentedImageDatabase);
            return true;
        }
        private Bitmap loadAugmentedImage() {
            try (InputStream is = getAssets().open("posterTrial.jpg")) {
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
            return null;
        }
        private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
            AnchorNode anchorNode = new AnchorNode(anchor);
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setRenderable(renderable);
            node.setParent(anchorNode);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
            node.select();
        }

    }
