package com.example.barcodescanner;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ScanBCInfo {

    private Context context;
    private ImageView imageIv;
    private Uri imageUri;
    private TextView resultTv, productIdTv, productNameTv, productImageUrlTv;;
    private static final String TAG = "MAIN_TAG";

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;

    private String barcodeData;

    String url = "https://world.openfoodfacts.org/api/v0/product/";
    private ExecutorService executorService;


    //constructor
    public ScanBCInfo(Context context, ImageView imageIv, TextView resultTv, Uri imageUri,TextView productIdTv, TextView productNameTv, TextView productImageUrlTv) {
        this.context = context;
        this.imageIv = imageIv;
        this.resultTv = resultTv;
        this.productIdTv = productIdTv;
        this.productNameTv = productNameTv;
        this.productImageUrlTv = productImageUrlTv;
        this.imageUri = imageUri;
        this.executorService = Executors.newSingleThreadExecutor(); //init executor service
        //init barcode scanner
        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_ALL_FORMATS)
                .build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);
    }

    public void detectResultFromImage() {
        try {

            //prepare image from image uri
            InputImage inputImage = InputImage.fromFilePath(context, imageUri);
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
                            Toast.makeText(context, "Failed scanning due to: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }catch (Exception e){
            Toast.makeText(context, "Failed scanning due to: "+e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "detectResultFromImage: "+e.getMessage());
        }
    }

    private void extractBarcodeData(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes) {
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            //Get data from barcode
            String rawValue = barcode.getRawValue();
            /*The following types are suported:
             * Barcode.TYPE_UNKNOWN, Barcode.TYPE_CONTACT_INFO,Barcode.TYPE_EMAIL, Barcode.TYPE_ISBN, Barcode.TYPE_PHONE,
             * Barcode.TYPE_PRODUCT, Barcode.TYPE_SMS, Barcode.TYPE_TEXT, Barcode.TYPE_URL, Barcode.TYPE_WIFI, Barcode.TYPE_GEO
             * Barcode.TYPE_CALENDAR_EVENT, Barcode.TYPE_DRIVER_LICENSE */
            int valueType = barcode.getValueType();
            barcodeData = rawValue; // Set barcodeData for all types
            //manage each type separately
            switch (valueType) {
                case Barcode.TYPE_URL: {
                    String title = barcode.getUrl().getTitle();
                    String url = barcode.getUrl().getUrl();
                    resultTv.setText("Type: URL (" + valueType + ")\nTitle: " + title + "\nURL: " + url);
                }
                break;
                case Barcode.TYPE_EMAIL: {
                    String email = barcode.getEmail().getAddress();
                    String subject = barcode.getEmail().getSubject();
                    String body = barcode.getEmail().getBody();
                    resultTv.setText("Type: EMAIL (" + valueType + ")\nEmail: " + email + "\nSubject: " + subject + "\nBody: " + body);
                }
                break;
                case Barcode.TYPE_PHONE: {
                    String phone = barcode.getPhone().getNumber();
                    resultTv.setText("Type: PHONE (" + valueType + ")\nPhone: " + phone);
                }
                break;
                case Barcode.TYPE_SMS: {
                    String phone = barcode.getSms().getPhoneNumber();
                    String message = barcode.getSms().getMessage();
                    resultTv.setText("Type: SMS (" + valueType + ")\nPhone: " + phone + "\nMessage: " + message);
                }
                break;
                case Barcode.TYPE_WIFI: {
                    String ssid = barcode.getWifi().getSsid();
                    String password = barcode.getWifi().getPassword();
                    int type = barcode.getWifi().getEncryptionType();
                    resultTv.setText("Type: WIFI (" + valueType + ")\nSSID: " + ssid + "\nPassword: " + password + "\nType: " + type);
                }
                break;
                case Barcode.TYPE_GEO: {
                    double lat = barcode.getGeoPoint().getLat();
                    double lng = barcode.getGeoPoint().getLng();
                    resultTv.setText("Type: GEO (" + valueType + ")\nLatitude: " + lat + "\nLongitude: " + lng);
                }
                break;
                case Barcode.TYPE_CALENDAR_EVENT: {
                    String description = barcode.getCalendarEvent().getDescription();
                    String location = barcode.getCalendarEvent().getLocation();
                    String organizer = barcode.getCalendarEvent().getOrganizer();
                    String status = barcode.getCalendarEvent().getStatus();
                    String summary = barcode.getCalendarEvent().getSummary();
                    long start = barcode.getCalendarEvent().getStart().getSeconds();
                    long end = barcode.getCalendarEvent().getEnd().getSeconds();
                    resultTv.setText("Type CALENDAR (: " + valueType + ")\nDescription: " + description + "\nLocation: " + location + "\nOrganizer: " + organizer +
                            "\nStatus: " + status + "\nSummary: " + summary + "\nStart" + start + "\nEnd" + end);
                }
                break;
                case Barcode.TYPE_DRIVER_LICENSE: {
                    String firstName = barcode.getDriverLicense().getFirstName();
                    String middleName = barcode.getDriverLicense().getMiddleName();
                    String lastName = barcode.getDriverLicense().getLastName();
                    resultTv.setText("Type: DRIVER LICENSE (" + valueType + ")\nFirst Name: " + firstName + "\nMiddle Name: " + middleName + "\nLast Name: " + lastName);
                }
                break;
                case Barcode.TYPE_TEXT: {
                    String text = barcode.getDisplayValue();
                    resultTv.setText("Type: TEXT (" + valueType + ")\nText: " + text);
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
                    resultTv.setText("Type: UNKNOWN (" + valueType + ")\nName: " + name + "\nOrganization: " + organization + "\nTitle: " + title);
                }
                break;
                case Barcode.TYPE_ISBN: {
                    String isbn = barcode.getDisplayValue();
                    resultTv.setText("Type: ISBN (" + valueType + ")\nISBN: " + isbn);
                }
                break;
                case Barcode.TYPE_PRODUCT: {
                    String product = barcode.getDisplayValue();
                    //resultTv.setText("Type: PRODUCT (" + valueType + ")\nProduct: " + product);
                    resultTv.setText("Product: " + product);
                    url = url + product + ".json";
                    getProductInfo(url, product);
                }
                break;
                default: {
                    resultTv.setText("rawValue: " + rawValue + "\nvalueType: " + valueType);
                }
            }
        }


    }

    public String getBarcodeData() {
        return barcodeData;
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
}

