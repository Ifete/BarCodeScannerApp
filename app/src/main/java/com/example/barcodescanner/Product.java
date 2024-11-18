package com.example.barcodescanner;

public class Product {
    private String name;
    private String price;
    private String barcode;

    public Product(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
    }


}
