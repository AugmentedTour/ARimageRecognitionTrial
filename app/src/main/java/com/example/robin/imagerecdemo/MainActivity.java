package com.example.robin.imagerecdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
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
import com.google.ar.sceneform.ux.ScaleController;
import com.google.ar.sceneform.ux.TransformableNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppActivityBuilderMethods{
    ArFragment arFragment;

    TextView mainInfo;
    TextView officeHours;

    // Put in the URL this activity will be parsing from
    private final String THIS_ONES_URL = "https://www.bellevuecollege.edu/artshum/";

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
            phoneBuilder("HR", "564-2274(425)", bodyLayout);

            titleBuilder("R Building", topLayout);
            hasAllGendersBathroom(topLayout);
            hasComputers(topLayout);

            mainInfo = textViewBuilder("Loading...", bodyLayout);
            officeHours = textViewBuilder("Loading...", bodyLayout);

            textViewBuilder("Human Resources (HR): Location R130(425) | Fax 564-3173", bodyLayout);

            // --- Async task ---
            new ParseWebpageTask().execute(THIS_ONES_URL);

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
                    if ((augmentedImage.getName().equals("model") || augmentedImage.getName().equals("map")
                            || augmentedImage.getName().equals("rBuilding") || augmentedImage.getName().equals("rSign"))
                            && shouldAddModel) {
                        placeXML(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), R.layout.hello_instructions);
                        shouldAddModel = false;
                    }
                }
            }
        }
        public boolean setupAugmentedImagesDb(Config config, Session session) {
            AugmentedImageDatabase augmentedImageDatabase;
            augmentedImageDatabase = new AugmentedImageDatabase(session);
            Bitmap bitmap = loadAugmentedImage("posterTrial.jpg");
            if (bitmap != null) {
                augmentedImageDatabase.addImage("model", bitmap);
            }
            //add evacuation map image
            bitmap = loadAugmentedImage("evacuationMap.jpg");
            if (bitmap != null) {
                augmentedImageDatabase.addImage("map", bitmap);
            }

            //add r building image
            bitmap = loadAugmentedImage("rBuildingTrial.jpg");
            if (bitmap != null) {
                augmentedImageDatabase.addImage("rBuilding", bitmap);
            }

            //add r building sign image
            bitmap = loadAugmentedImage("rSign.jpg");
            if (bitmap != null) {
                augmentedImageDatabase.addImage("rSign", bitmap);
            }

            config.setAugmentedImageDatabase(augmentedImageDatabase);
            return true;
        }
        private Bitmap loadAugmentedImage(String fileName) {
            try (InputStream is = getAssets().open(fileName)) {
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
            return null;
        }
        private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
            AnchorNode anchorNode = new AnchorNode(anchor);
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());

            //rotate node
            node.setLocalRotation(Quaternion.axisAngle(new Vector3(1f, 0, 0), 270f));
            //node.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), 90f));

            //set scale
            ScaleController scaler = node.getScaleController();
            scaler.setMinScale(0.2f);
            scaler.setMaxScale(0.3f);


            node.setLocalPosition(new Vector3(-.2f, 0, .2f));

            node.setRenderable(renderable);
            node.setParent(anchorNode);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
            node.select();
        }



    //This is used to parse the webpage. Just due to how different each page's parsing will be,
    //We'll probably need a custom one of these for every activity.
    //Following something similar to this here though should cover that.
    private class ParseWebpageTask extends AsyncTask<String, Void, String[]> {
        protected String[] doInBackground(String... urls) { //this is set up for one url but technically it could be easily changed to accommodate several
            try {
                return grabData(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Use this to set all of the text things
        protected void onPostExecute(String[] result) {
            mainInfo.setText(result[0]);
            officeHours.setText(result[1]);
        }

        //Grab all the data in here and put it into a String[]
        public String[] grabData(String url) throws IOException {
            Document doc = Jsoup.connect(url).get();
            Elements para = doc.getElementsByTag("p");
            Elements hours = doc.getElementsByClass("well");
            String[] strings = {para.first().text(), hours.first().text()};
            return strings;
        }

    }

    }
