package com.example.barcodescanner;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private MaterialButton cameraBtn, galleryBtn, scanBtn, JSONBtn;
    private ImageView imageIv;
    private TextView resultTv, productIdTv, productNameTv, productImageUrlTv;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;


    private String[] cameraPermissions;
    private String[] storagePermissions;

    //Uri of picked image
    public Uri imageUri = null;

    GIFGallery GIFGallery;
    GIFCamera GIFCamera;
    ScanBCInfo ScanBCInfo;
    private ExecutorService executorService;

    String url = "https://world.openfoodfacts.org/api/v0/product/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        scanBtn = findViewById(R.id.scanBtn);
        JSONBtn = findViewById(R.id.jsonBtn);

        //ImageViews
        imageIv = findViewById(R.id.imageIv);
        productIdTv = findViewById(R.id.productIdTv);
        productNameTv = findViewById(R.id.productNameTv);
        productImageUrlTv = findViewById(R.id.productImageTv);

        //TextViews
        resultTv = findViewById(R.id.resultTv);

        //permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};//image from camera: Camera and Storage
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};//Image from gallery: Storage


        //init image picker from gallery
        GIFGallery = new GIFGallery(MainActivity.this, imageIv);

        //init image picker from camera
        GIFCamera = new GIFCamera(MainActivity.this, imageIv, resultTv, productIdTv, productNameTv, productImageUrlTv);

        // Initialize the ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        //Handle camenraBtn click, check permissions related to Camera(i.e WRITE_EXTERNAL_STORAGE, CAMERA) and take picture
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if(checkCameraPermission()){
                    //permissions allowed, open camera
                    GIFCamera.pickImageCamera();
                }else {
                    //permissions not allowed, request
                    requestCameraPermission();
                }
            }
        });

        //Handle galleryBtn click, check permissions related to Gallery(i.e READ_EXTERNAL_STORAGE) and open gallery
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if(checkStoragePermission()){
                    //permissions allowed, open gallery
                    GIFGallery.pickFromGallery();
                }else {
                    //permissions not allowed, request
                    requestStoragePermission();
                }
                scanItem();
                getJSONIfo();
            }
        });

        //Handle scanBtn click scan barcode from image
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = GIFGallery.getImageUri();
                if (imageUri == null) {
                    imageUri = GIFCamera.getImageUri();
                }
                if(imageUri == null){
                    Toast.makeText(MainActivity.this, "Please pick an image", Toast.LENGTH_SHORT).show();
                }else {
                    //init barcode scanner
                    ScanBCInfo = new ScanBCInfo(MainActivity.this, imageIv, resultTv, imageUri, productIdTv, productNameTv, productImageUrlTv);
                    ScanBCInfo.detectResultFromImage();
//                    String barcode = ScanBCInfo.getBarcodeData();
//                    getProductInfo(url, barcode);
                }
            }
        });


        JSONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = ScanBCInfo.getBarcodeData();
                url = url + barcode + ".json";
                getProductInfo(url, barcode);
            }
        });
    }

    //Function to scan info from image
    private void scanItem(){

        imageUri = GIFGallery.getImageUri();
        if (imageUri == null) {
            imageUri = GIFCamera.getImageUri();
        }
        if(imageUri == null){
            Toast.makeText(MainActivity.this, "Please pick an image", Toast.LENGTH_SHORT).show();
        }else {
            //init barcode scanner
            ScanBCInfo = new ScanBCInfo(MainActivity.this, imageIv, resultTv, imageUri, productIdTv, productNameTv, productImageUrlTv);
            ScanBCInfo.detectResultFromImage();
        }
    }

    private void getJSONIfo(){
        String barcode = ScanBCInfo.getBarcodeData();
        url = url + barcode + ".json";
        getProductInfo(url, barcode);
    }


    /*GET JSON DATA*/
    private void getProductInfo(String url, String barcode) {
        GetProductInfoTask task = new GetProductInfoTask(url, barcode);
        Future<String> future = executorService.submit(task);

        executorService.execute(() -> {
            try {
                String response = future.get();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject product = jsonObject.getJSONObject("product");
                    String productId = product.getString("id");
                    String genericNameEs = product.getString("generic_name_es");
                    String imageUrl = product.getString("image_front_small_url");
                    // Set data to TextView
                    Log.d("product", "Product ID: " + productId + "\n" +
                            "Generic Name: " + genericNameEs + "\n" +
                            "Image URL: " + imageUrl);
                    productIdTv.setText(String.format("Product ID: %s", productId));
                    productNameTv.setText(String.format("Generic Name: %s", genericNameEs));
                    productImageUrlTv.setText(String.format("Image URL: %s", imageUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                    productIdTv.setText("Failed to get product info");
                }
//                runOnUiThread(() -> {
//                    // Parse JSON response
//
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the ExecutorService
        executorService.shutdown();
    }



    /* PERMISSIONS SECTION */
    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (getPackageManager().PERMISSION_GRANTED);
        //return true if permissions WRITE_EXTERNAL_STORAGE is granted and false if not
        return result;
    }

    private void requestStoragePermission() {
        //request the storage permission (for gallery image pick)
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera and storage permission is enabled or not
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (getPackageManager().PERMISSION_GRANTED);
        //check if storage permission is enabled or not
        boolean resultStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (getPackageManager().PERMISSION_GRANTED);
        //return true if permissions WRITE_EXTERNAL_STORAGE and CAMERA is granted and false if not
        return resultCamera && resultStorage;
    }

    private void requestCameraPermission() {
        //request the camera and storage permission (for camera image pick)
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    //check if camera permissions granted or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //if both permissions are granted
                    if(cameraAccepted && storageAccepted) {
                        //permissions enabled
                        GIFCamera.pickImageCamera();
                    }else {
                        //permissions denied cant launch camera
                        Toast.makeText(this, "Camera & Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    //permission granted
                    if(storageAccepted) {
                        //permission enabled
                        GIFGallery.pickFromGallery();
                    }
                }else {
                    //permission denied cant pick image from gallery
                    Toast.makeText(this, "Storage permission required...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}