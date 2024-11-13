package com.example.barcodescanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        scanBtn = findViewById(R.id.scanBtn);

        //ImageViews
        imageIv = findViewById(R.id.imageIv);

        //TextViews
        resultTv = findViewById(R.id.resultTv);

        //permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};//image from camera: Camera and Storage
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};//Image from gallery: Storage

        //init barcode scanner
        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_ALL_FORMATS)
                .build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);


        //Handle camenraBtn click, check permissions related to Camera(i.e WRITE_EXTERNAL_STORAGE, CAMERA) and take picture
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if(checkCameraPermission()){
                    //permissions allowed, open camera
                    pickImageCamera();
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
                    pickFromGallery();
                }else {
                    //permissions not allowed, request
                    requestStoragePermission();
                }
            }
        });

        //Handle scanBtn click scan barcode from image
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri == null){
                    Toast.makeText(MainActivity.this, "Please pick an image", Toast.LENGTH_SHORT).show();
                }else {
                    detectResultFromImage();
                }
            }
        });
    }

    private void detectResultFromImage() {
        try {
            //prepare image from image uri
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            //start scanning the image for barcode
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage).
                    addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            //task completed successfully,we can get details of barcode
                            extractBarcodeData(barcodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed scanning image for barcode
                            Toast.makeText(MainActivity.this, "Failed scanning due to: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }catch (Exception e){
            Toast.makeText(this, "Failed scanning due to: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void extractBarcodeData(List<Barcode> barcodes) {
        for (Barcode barcode: barcodes){
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            //Get data from barcode
            String rawValue = barcode.getRawValue();
            /*The following types are suported:
            * Barcode.TYPE_UNKNOWN, Barcode.TYPE_CONTACT_INFO,Barcode.TYPE_EMAIL, Barcode.TYPE_ISBN, Barcode.TYPE_PHONE,
            * Barcode.TYPE_PRODUCT, Barcode.TYPE_SMS, Barcode.TYPE_TEXT, Barcode.TYPE_URL, Barcode.TYPE_WIFI, Barcode.TYPE_GEO
            * Barcode.TYPE_CALENDAR_EVENT, Barcode.TYPE_DRIVER_LICENSE */
            int valueType = barcode.getValueType();
            //manage each type separately
            switch (valueType){
                case Barcode.TYPE_URL:{
                    String title = barcode.getUrl().getTitle();
                    String url = barcode.getUrl().getUrl();
                    resultTv.setText("Type: "+valueType+"\nTitle: "+title+"\nURL: "+url);
                }
                break;
                case Barcode.TYPE_EMAIL:{
                    String email = barcode.getEmail().getAddress();
                    String subject = barcode.getEmail().getSubject();
                    String body = barcode.getEmail().getBody();
                    resultTv.setText("Type: "+valueType+"\nEmail: "+email+"\nSubject: "+subject+"\nBody: "+body);
                }
                break;
                case Barcode.TYPE_PHONE:{
                    String phone = barcode.getPhone().getNumber();
                    resultTv.setText("Type: "+valueType+"\nPhone: "+phone);
                }
                break;
                case Barcode.TYPE_SMS:{
                    String phone = barcode.getSms().getPhoneNumber();
                    String message = barcode.getSms().getMessage();
                    resultTv.setText("Type: "+valueType+"\nPhone: "+phone+"\nMessage: "+message);
                }
                break;
                case Barcode.TYPE_WIFI:{
                    String ssid = barcode.getWifi().getSsid();
                    String password = barcode.getWifi().getPassword();
                    int type = barcode.getWifi().getEncryptionType();
                    resultTv.setText("Type: "+valueType+"\nSSID: "+ssid+"\nPassword: "+password+"\nType: "+type);
                }
                break;
                case Barcode.TYPE_GEO:{
                    double lat = barcode.getGeoPoint().getLat();
                    double lng = barcode.getGeoPoint().getLng();
                    resultTv.setText("Type: "+valueType+"\nLatitude: "+lat+"\nLongitude: "+lng);
                }
                break;
                case Barcode.TYPE_CALENDAR_EVENT:{
                    String description = barcode.getCalendarEvent().getDescription();
                    String location = barcode.getCalendarEvent().getLocation();
                    String organizer = barcode.getCalendarEvent().getOrganizer();
                    String status = barcode.getCalendarEvent().getStatus();
                    String summary = barcode.getCalendarEvent().getSummary();
                    long start = barcode.getCalendarEvent().getStart().getSeconds();
                    long end = barcode.getCalendarEvent().getEnd().getSeconds();
                    resultTv.setText("Type: "+valueType+"\nDescription: "+description+"\nLocation: "+location+"\nOrganizer: "+organizer+
                            "\nStatus: "+status+"\nSummary: "+summary+"\nStart"+start+"\nEnd"+end);
                }
                break;
                case Barcode.TYPE_DRIVER_LICENSE: {
                    String firstName = barcode.getDriverLicense().getFirstName();
                    String middleName = barcode.getDriverLicense().getMiddleName();
                    String lastName = barcode.getDriverLicense().getLastName();
                }
                break;
                case Barcode.TYPE_TEXT:{
                    String text = barcode.getDisplayValue();
                    resultTv.setText("Type: "+valueType+"\nText: "+text);
                }
                break;
                case Barcode.TYPE_UNKNOWN: {
                    resultTv.setText("Type: " + valueType + "\nUnknown type");
                }
                break;
                case Barcode.TYPE_CONTACT_INFO: {
                    String name = barcode.getContactInfo().getName().getFormattedName();
                    String organization = barcode.getContactInfo().getOrganization();
                    String title = barcode.getContactInfo().getTitle();
                    resultTv.setText("Type: " + valueType + "\nName: " + name + "\nOrganization: " + organization + "\nTitle: " + title);
                }
                break;
                case Barcode.TYPE_ISBN: {
                    String isbn = barcode.getDisplayValue();
                    resultTv.setText("Type: " + valueType + "\nISBN: " + isbn);
                }
                break;
                case Barcode.TYPE_PRODUCT: {
                    String product = barcode.getDisplayValue();
                    resultTv.setText("Type: " + valueType + "\nProduct: " + product);
                }
                break;
                default:{
                    resultTv.setText("rawValue: "+rawValue+"\nvalueType: "+valueType);
                }
            }
        }



    }

    private void pickFromGallery() {
        //Intent to pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Image picked from gallery
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //get the uri of image picked from gallery
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                        //set to ImageView
                        imageIv.setImageURI(imageUri);
                    }else {
                        //Failed picking image from gallery
                        Toast.makeText(MainActivity.this, "Failed to pick image from gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickImageCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Image picked from camera
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //get the uri of image taken from camera
                        Intent data = result.getData();
                        //we already have the image in imageUri using function pickImageCamera()
                        Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                        //set to ImageView
                        imageIv.setImageURI(imageUri);
                    }else {
                        //Failed taking picture from camera
                        Toast.makeText(MainActivity.this, "Failed taking picture from camera", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

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
                        pickImageCamera();
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
                        pickFromGallery();
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