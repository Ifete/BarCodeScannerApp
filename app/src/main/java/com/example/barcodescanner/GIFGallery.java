// ImageFromGallery.java
package com.example.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class GIFGallery {

    private Context context;
    private ImageView imageIv;
    private Uri imageUri;
    private static final String TAG = "MAIN_TAG";
    private final ActivityResultLauncher<Intent> galleryLauncher;

    // Constructor
    public GIFGallery(Context context, ImageView imageIv) {
        this.context = context;
        this.imageIv = imageIv;
        this.galleryLauncher = ((AppCompatActivity) context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                            imageIv.setImageURI(imageUri);
                        } else {
                            Toast.makeText(context, "Failed to pick image from gallery", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // Method to pick image from gallery
    public void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    // Getter method to get image uri
    public Uri getImageUri() {
        return imageUri;
    }
}