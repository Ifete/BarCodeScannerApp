package com.example.barcodescanner;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;

public class BarcodeCode {

    private MaterialButton cameraBtn, galleryBtn, scanBtn;
    private ImageView imageIv;
    private TextView resultTv;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;


    private String[] cameraPermissions;
    private String[] storagePermissions;

    //Uri of picked image
    private Uri imageUri = null;

    private static final String TAG = "MAIN_TAG";

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;


}
