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

package devfest.hackathon.trashrecognition;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.base.Objects;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import devfest.hackathon.trashrecognition.camera.CameraSource;
import devfest.hackathon.trashrecognition.camera.CameraSourcePreview;
import devfest.hackathon.trashrecognition.camera.GraphicOverlay;
import devfest.hackathon.trashrecognition.camera.WorkflowModel;
import devfest.hackathon.trashrecognition.camera.WorkflowModel.WorkflowState;
import devfest.hackathon.trashrecognition.common.RecognitionResult;
import devfest.hackathon.trashrecognition.objectdetection.MultiObjectProcessor;
import devfest.hackathon.trashrecognition.objectdetection.ProminentObjectProcessor;
import devfest.hackathon.trashrecognition.productsearch.BottomSheetScrimView;
import devfest.hackathon.trashrecognition.productsearch.Product;
import devfest.hackathon.trashrecognition.productsearch.SearchEngine;
import devfest.hackathon.trashrecognition.productsearch.SearchedObject;
import devfest.hackathon.trashrecognition.settings.PreferenceUtils;
import devfest.hackathon.trashrecognition.settings.SettingsActivity;

/**
 * Demonstrates the object detection and visual search workflow using camera preview.
 */
public class LiveObjectDetectionActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "LiveObjectActivity";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private View settingsButton;
    private View flashButton;
    private Chip promptChip;
    private AnimatorSet promptChipAnimator;
    private ExtendedFloatingActionButton searchButton;
    private AnimatorSet searchButtonAnimator;
    private ProgressBar searchProgressBar;
    private WorkflowModel workflowModel;
    private WorkflowState currentWorkflowState;
    private SearchEngine searchEngine;

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomSheetScrimView bottomSheetScrimView;
    private TextView bottomSheetTitleView;
    private Bitmap objectThumbnailForBottomSheet;
    private boolean slidingSheetUpFromHiddenState;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvTypeOfTrash;
    private ImageView ivImageGeneral;

    private Button btnUploadFirebase;

    private FirebaseFirestore mFirestore;

    private static final String NAME_KEY = "address";

    private static final String EMAIL_KEY = "amount";

    private static final String PHONE_KEY = "description";

    private static List<RecognitionResult> solutions = new ArrayList<RecognitionResult>();
    private Dialog dialog;

    private Button btnSearchConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMockDataSolution();
        searchEngine = new SearchEngine(getApplicationContext());

        setContentView(R.layout.activity_live_object);
        //get Instance from firebase
        mFirestore=FirebaseFirestore.getInstance();
        btnUploadFirebase=findViewById(R.id.btnUploadFirebase);

        btnSearchConsumer=findViewById(R.id.btnSearchConsumer);


        preview = findViewById(R.id.camera_preview);
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);
        cameraSource = new CameraSource(graphicOverlay);

        promptChip = findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator =
                (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);

        searchButton = findViewById(R.id.product_search_button);
        searchButton.setOnClickListener(this);
        searchButtonAnimator =
                (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.search_button_enter);
        searchButtonAnimator.setTarget(searchButton);

        searchProgressBar = findViewById(R.id.search_progress_bar);

        setUpBottomSheet();

        findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvTypeOfTrash = findViewById(R.id.tvTypeOfTrash);
        ivImageGeneral = findViewById(R.id.ivImageGeneral);
        setUpWorkflowModel();
        initButton();
    }

    private void initialDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.material_dialog);
        dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        workflowModel.markCameraFrozen();
        settingsButton.setEnabled(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        currentWorkflowState = WorkflowState.NOT_STARTED;
        cameraSource.setFrameProcessor(
                PreferenceUtils.isMultipleObjectsMode(this)
                        ? new MultiObjectProcessor(graphicOverlay, workflowModel)
                        : new ProminentObjectProcessor(graphicOverlay, workflowModel));
        workflowModel.setWorkflowState(WorkflowState.DETECTING);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentWorkflowState = WorkflowState.NOT_STARTED;
        stopCameraPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        searchEngine.shutdown();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.product_search_button) {
            searchButton.setEnabled(false);
            workflowModel.onSearchButtonClicked();

        } else if (id == R.id.bottom_sheet_scrim_view) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        } else if (id == R.id.close_button) {
            onBackPressed();

        } else if (id == R.id.flash_button) {
            if (flashButton.isSelected()) {
                flashButton.setSelected(false);
                cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                flashButton.setSelected(true);
                cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }

        } else if (id == R.id.settings_button) {
            // Sets as disabled to prevent the user from clicking on it too fast.
            settingsButton.setEnabled(false);
            startActivity(new Intent(this, SettingsActivity.class));

        }
        else if(id==R.id.btnUploadFirebase){

        }
    }

    private void startCameraPreview() {
        if (!workflowModel.isCameraLive() && cameraSource != null) {
            try {
                workflowModel.markCameraLive();
                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Failed to start camera preview!", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void stopCameraPreview() {
        if (workflowModel.isCameraLive()) {
            workflowModel.markCameraFrozen();
            flashButton.setSelected(false);
            preview.stop();
        }
    }

    private void setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        Log.d(TAG, "Bottom sheet new state: " + newState);
                        bottomSheetScrimView.setVisibility(
                                newState == BottomSheetBehavior.STATE_HIDDEN ? View.GONE : View.VISIBLE);
                        graphicOverlay.clear();

                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                workflowModel.setWorkflowState(WorkflowState.DETECTING);
                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
                            case BottomSheetBehavior.STATE_EXPANDED:
                            case BottomSheetBehavior.STATE_HALF_EXPANDED:
                                slidingSheetUpFromHiddenState = false;
                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                            case BottomSheetBehavior.STATE_SETTLING:
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        SearchedObject searchedObject = workflowModel.searchedObject.getValue();
                        if (searchedObject == null || Float.isNaN(slideOffset)) {
                            return;
                        }

                        int collapsedStateHeight =
                                Math.min(bottomSheetBehavior.getPeekHeight(), bottomSheet.getHeight());
                        if (slidingSheetUpFromHiddenState) {
                            RectF thumbnailSrcRect =
                                    graphicOverlay.translateRect(searchedObject.getBoundingBox());
                            bottomSheetScrimView.updateWithThumbnailTranslateAndScale(
                                    objectThumbnailForBottomSheet,
                                    collapsedStateHeight,
                                    slideOffset,
                                    thumbnailSrcRect);

                        } else {
                            bottomSheetScrimView.updateWithThumbnailTranslate(
                                    objectThumbnailForBottomSheet, collapsedStateHeight, slideOffset, bottomSheet);
                        }
                    }
                });

        bottomSheetScrimView = findViewById(R.id.bottom_sheet_scrim_view);
        bottomSheetScrimView.setOnClickListener(this);

        bottomSheetTitleView = findViewById(R.id.bottom_sheet_title);
    }

    private void setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.workflowState.observe(
                this,
                workflowState -> {
                    if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                        return;
                    }

                    currentWorkflowState = workflowState;
                    Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

                    if (PreferenceUtils.isAutoSearchEnabled(this)) {
                        stateChangeInAutoSearchMode(workflowState);
                    } else {
                        stateChangeInManualSearchMode(workflowState);
                    }
                });

        // Observes changes on the object to search, if happens, fire product search request.
        workflowModel.objectToSearch.observe(
                this, object -> searchEngine.search(object, workflowModel));

        // Observes changes on the object that has search completed, if happens, show the bottom sheet
        // to present search result.
        workflowModel.searchedObject.observe(
                this,
                searchedObject -> {
                    if (searchedObject != null) {
                        Product productRecognition = searchedObject.getProductList().get(0);
                        RecognitionResult recognitionResult = findSolutionByLabel(productRecognition.getTitle());
                        objectThumbnailForBottomSheet = searchedObject.getObjectThumbnail();
                        bottomSheetTitleView.setText(productRecognition.getTitle());
                        bottomSheetTitleView.setText(recognitionResult.getTitle());
                        tvTitle.setText(recognitionResult.getTitle());
                        tvTypeOfTrash.setText(recognitionResult.getTypeOfTrashText());
                        tvDescription.setText(recognitionResult.getDescription());

                        try {
                            AssetManager assetManager = getAssets();
                            InputStream ims = assetManager.open(recognitionResult.getImageGeneralUrl());
                            Drawable d = Drawable.createFromStream(ims, null);
                            ivImageGeneral.setImageDrawable(d);
                            ims.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        slidingSheetUpFromHiddenState = true;
                        bottomSheetBehavior.setPeekHeight(preview.getHeight() / 2);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
    }



    private void stateChangeInAutoSearchMode(WorkflowState workflowState) {
        boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

        searchButton.setVisibility(View.GONE);
        searchProgressBar.setVisibility(View.GONE);
        switch (workflowState) {
            case DETECTING:
            case DETECTED:
            case CONFIRMING:
                promptChip.setVisibility(View.VISIBLE);
                promptChip.setText(
                        workflowState == WorkflowState.CONFIRMING
                                ? R.string.prompt_hold_camera_steady
                                : R.string.prompt_point_at_an_object);
                startCameraPreview();
                break;
            case CONFIRMED:
                promptChip.setVisibility(View.VISIBLE);
                promptChip.setText(R.string.prompt_searching);
                stopCameraPreview();
                break;
            case SEARCHING:
                searchProgressBar.setVisibility(View.VISIBLE);
                promptChip.setVisibility(View.VISIBLE);
                promptChip.setText(R.string.prompt_searching);
                stopCameraPreview();
                break;
            case SEARCHED:
                promptChip.setVisibility(View.GONE);
                stopCameraPreview();
                break;
            default:
                promptChip.setVisibility(View.GONE);
                break;
        }

        boolean shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
        if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
            promptChipAnimator.start();
        }
    }

    private void stateChangeInManualSearchMode(WorkflowState workflowState) {
        boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);
        boolean wasSearchButtonGone = (searchButton.getVisibility() == View.GONE);

        searchProgressBar.setVisibility(View.GONE);
        switch (workflowState) {
            case DETECTING:
            case DETECTED:
            case CONFIRMING:
                promptChip.setVisibility(View.VISIBLE);
                promptChip.setText(R.string.prompt_point_at_an_object);
                searchButton.setVisibility(View.GONE);
                startCameraPreview();
                break;
            case CONFIRMED:
                promptChip.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
                searchButton.setEnabled(true);
                searchButton.setBackgroundColor(Color.WHITE);
                startCameraPreview();
                break;
            case SEARCHING:
                promptChip.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
                searchButton.setEnabled(false);
                searchButton.setBackgroundColor(Color.GRAY);
                searchProgressBar.setVisibility(View.VISIBLE);
                stopCameraPreview();
                break;
            case SEARCHED:
                promptChip.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                stopCameraPreview();
                break;
            default:
                promptChip.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                break;
        }

        boolean shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
        if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
            promptChipAnimator.start();
        }

        boolean shouldPlaySearchButtonEnteringAnimation =
                wasSearchButtonGone && (searchButton.getVisibility() == View.VISIBLE);
        if (shouldPlaySearchButtonEnteringAnimation && !searchButtonAnimator.isRunning()) {
            searchButtonAnimator.start();
        }
    }

    private static void setupMockDataSolution() {
        RecognitionResult solutionForMetal = new RecognitionResult();
        RecognitionResult solutionForGlass = new RecognitionResult();
        RecognitionResult solutionForBattery = new RecognitionResult();
        RecognitionResult solutionForCardboard = new RecognitionResult();
        RecognitionResult solutionForOrganic = new RecognitionResult();
        RecognitionResult solutionForPlastic = new RecognitionResult();
        RecognitionResult solutionForPaper = new RecognitionResult();
        RecognitionResult solutionForTrash = new RecognitionResult();
        RecognitionResult solutionForElectronic = new RecognitionResult();
        solutionForMetal.setLabel("metal");
        solutionForMetal.setTitle("Kim loại");
        solutionForMetal.setImageGeneralUrl("metal.png");
        solutionForMetal.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForMetal.setTypeOfTrashText("Chất thải tái chế");
        solutionForGlass.setLabel("glass");
        solutionForGlass.setImageGeneralUrl("glass.jpg");
        solutionForGlass.setTitle("Thủy tinh");
        solutionForGlass.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForGlass.setTypeOfTrashText("Chất thải tái chế");
        solutionForBattery.setLabel("batteries");
        solutionForBattery.setImageGeneralUrl("battery.png");
        solutionForBattery.setTitle("Pin, ắc quy");
        solutionForBattery.setDescription("Phải mang đến điểm thu hồi chất thải nguy h");
        solutionForBattery.setTypeOfTrashText("Chất thải rất độc hại");
        solutionForCardboard.setLabel("cardboard");
        solutionForCardboard.setTitle("Giấy bìa cứng");
        solutionForCardboard.setImageGeneralUrl("cardboard.jpg");
        solutionForCardboard.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForCardboard.setTypeOfTrashText("Chất thải tái chế");
        solutionForOrganic.setLabel("organic");
        solutionForOrganic.setImageGeneralUrl("organic.jpg");
        solutionForOrganic.setTitle("Chất thải thực vật dễ phân hủy");
        solutionForOrganic.setDescription("Cho nào túi màu xanh lục hoặc có nhãn chất thải hữu cơ. Xử lý chuyển thành phân b");
        solutionForOrganic.setTypeOfTrashText("Rác hữu cơ");
        solutionForPlastic.setLabel("plastic");
        solutionForPlastic.setImageGeneralUrl("plastic.png");
        solutionForPlastic.setTitle("Nhựa");
        solutionForPlastic.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForPlastic.setTypeOfTrashText("Chất thải tái chế");
        solutionForTrash.setLabel("trash");
        solutionForTrash.setImageGeneralUrl("trash.png");
        solutionForTrash.setTitle("Nhựa tái chế");
        solutionForTrash.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForTrash.setTypeOfTrashText("Chất thải tái chế");
        solutionForElectronic.setLabel("electronic");
        solutionForElectronic.setImageGeneralUrl("electronic.jpg");
        solutionForElectronic.setTitle("Đồ điện tử");
        solutionForElectronic.setDescription("Phải mang đến điểm thu hồi chất thải nguy hồi");
        solutionForElectronic.setTypeOfTrashText("Chất thải rất độc hại");
        solutionForPaper.setLabel("paper");
        solutionForPaper.setImageGeneralUrl("paper.jpg");
        solutionForPaper.setTitle("Giấy loại");
        solutionForPaper.setDescription("Người dân chủ động bán/cho tặng người hoặc hệ thống thu gom phế liệu");
        solutionForPaper.setTypeOfTrashText("Chất thải tái chế");
        solutions.add(solutionForMetal);
        solutions.add(solutionForGlass);
        solutions.add(solutionForBattery);
        solutions.add(solutionForCardboard);
        solutions.add(solutionForOrganic);
        solutions.add(solutionForPlastic);
        solutions.add(solutionForTrash);
        solutions.add(solutionForPaper);
    }

    private static RecognitionResult findSolutionByLabel(String label) {
        for (RecognitionResult recognitionResult : solutions) {
            if (recognitionResult.getLabel().equals(label)) {
                return recognitionResult;
            }
        }
        return null;
    }

}
