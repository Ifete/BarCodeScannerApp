package com.example.barcodescanner;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;

public class ImageFromGallery {

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

    public void pickFromGallery() {
        //Intent to pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

//    public final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    //Image picked from gallery
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        //get the uri of image picked from gallery
//                        Intent data = result.getData();
//                        imageUri = data.getData();
//                        Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
//                        //set to ImageView
//                        imageIv.setImageURI(imageUri);
//                    }else {
//                        //Failed picking image from gallery
//                        Toast.makeText(MainActivity, "Failed to pick image from gallery", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//    );
}
