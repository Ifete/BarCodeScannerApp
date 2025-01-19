package com.example.barcodescanner.encapsulator;

public class ItemEncap {
    private int image;
    private String foodTitle;
    private String storage;
    private int qty;

    public ItemEncap(int idimage, String foodTitle, String storage, int qty){
        this.image = idimage;
        this.foodTitle = foodTitle;
        this.storage = storage;
        this.qty = qty;
    }

    public String get_foodTitle(){return foodTitle;}
    public String get_storage(){return storage;}
    public int get_idImage(){return image;}
    public int get_qty(){return qty;}

}