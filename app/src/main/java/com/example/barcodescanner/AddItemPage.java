package com.example.barcodescanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.barcodescanner.items.ItemsPage;
import com.example.barcodescanner.settings.SettingsPage;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;


public class AddItemPage extends AppCompatActivity {
    private MaterialButton galleryBtn;
    private FloatingActionButton cameraBtn;
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
        setContentView(R.layout.add_item_page);

        //Buttons
        cameraBtn = findViewById(R.id.cameraFab);
        cameraBtn.show();
        //galleryBtn = findViewById(R.id.galleryBtn);

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
        GIFGallery = new GIFGallery(AddItemPage.this, imageIv);

        //init image picker from camera
        GIFCamera = new GIFCamera(AddItemPage.this, imageIv, resultTv, productIdTv, productNameTv, productImageUrlTv);

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
//        galleryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //check permissions
//                if(checkStoragePermission()){
//                    //permissions allowed, open gallery
//                    GIFGallery.pickFromGallery();
//                }else {
//                    //permissions not allowed, request
//                    requestStoragePermission();
//                }
//            }
//        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemsMenu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.storageMenu:
                        Intent storagePage = new Intent(AddItemPage.this, StoragePage.class);
                        AddItemPage.this.startActivity(storagePage);
                        return true;
                    case R.id.itemsMenu:
                        Intent itemsPage = new Intent(AddItemPage.this, ItemsPage.class);
                        AddItemPage.this.startActivity(itemsPage);
                        return true;
                    case R.id.settingsMenu:
                        Intent settingsPage = new Intent(AddItemPage.this, SettingsPage.class);
                        AddItemPage.this.startActivity(settingsPage);
                        return true;
                }
                return false;
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