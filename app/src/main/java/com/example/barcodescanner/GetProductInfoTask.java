package com.example.barcodescanner;

import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class GetProductInfoTask implements Callable<String> {
    private String url;
    private String barcode;

    public GetProductInfoTask(String url, String barcode) {
        this.url = url;
        this.barcode = barcode;
    }

    @Override
    public String call() throws Exception {
        String response = "";
        try {
            HttpRequest request = new HttpRequest(url);
            request.prepare();
            response = request.sendAndReadString();
            Log.d("CallResponse", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
