/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package devfest.hackathon.trashrecognition.productsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import devfest.hackathon.trashrecognition.objectdetection.DetectedObject;

/**
 * A fake search engine to help simulate the complete work flow.
 */
public class SearchEngine {

    private static final String TAG = "SearchEngine";
    private FirebaseVisionImageLabeler labeler = null;
    Context context;

    public interface SearchResultListener {
        void onSearchCompleted(DetectedObject object, List<Product> productList);
    }

    public SearchEngine(Context context) {
        this.context = context;
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("my_local_model")
                .setAssetFilePath("manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);
        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("my_local_model")
                        .setConfidenceThreshold(0)
                        .build();

        try {
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
        } catch (FirebaseMLException e) {

        }
    }

    public void search(DetectedObject object, SearchResultListener listener) {
//        storeImage(object.getBitmapRaw());
        // Crops the object image out of the full image is expensive, so do it off the UI thread.
        labeler.processImage(FirebaseVisionImage.fromBitmap(object.getBitmap()))
                .addOnSuccessListener(labels -> {
                    String nameLabel;
                    nameLabel = labels.get(0).getText();
                    Product product = new Product();
                    product.setTitle(nameLabel);


                    List<Product> productList = new ArrayList<>();
                    productList.add(product);
                    listener.onSearchCompleted(object, productList);
                    }
                })
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, "Failed to create product search request!", e);
//                            // Remove the below dummy code after your own product search backed hooked up.
//                            List<Product> productList = new ArrayList<>();
//                            for (int i = 0; i < 8; i++) {
//                                productList.add(
//                                        new Product(/* imageUrl= */ "", "Product title " + i, "Product subtitle " + i));
//                            }
//                            listener.onSearchCompleted(object, productList);
                        });
    }


    public void shutdown() {
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
