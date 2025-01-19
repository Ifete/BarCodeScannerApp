package com.example.barcodescanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GIFCamera {

    private Context context;
    private ImageView imageIv;
    private Uri imageUri;
    private TextView resultTv;
    private TextInputEditText productIdTv, productNameTv, productImageUrlTv;
    private static final String TAG = "MAIN_TAG";

    private final ActivityResultLauncher<Intent> cameraLauncher;
    ScanBCInfo ScanBCInfo;


    public GIFCamera(Context context, ImageView imageIv, TextView resultTv, TextInputEditText productIdTv, TextInputEditText productNameTv, TextInputEditText productImageUrlTv) {
        this.context = context;
        this.imageIv = imageIv;
        this.resultTv = resultTv;
        this.productIdTv = productIdTv;
        this.productNameTv = productNameTv;
        this.productImageUrlTv = productImageUrlTv;
        this.cameraLauncher = ((AppCompatActivity) context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (imageUri != null) {
                                Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                                imageIv.setImageURI(imageUri);
                                ScanBCInfo = new ScanBCInfo(context, imageIv, resultTv, imageUri, productIdTv, productNameTv, productImageUrlTv);
                                ScanBCInfo.detectResultFromImage();


                            } else {
                                Log.e(TAG, "onActivityResult: imageUri is null");
                                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");

        imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        if (imageUri != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(cameraIntent);
        } else {
            Log.e(TAG, "pickImageCamera: Failed to create imageUri");
            Toast.makeText(context, "Failed to create image file", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri() {
        return imageUri;
    }




}

