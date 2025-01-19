package com.example.barcodescanner.encapsulator;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.barcodescanner.AddItemPage;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.button.MaterialButton;

public class StorageEncap {
    private int image;
    private String storageTitle;
    private String storageDescription;

    private MaterialButton editStorageBtn, addItemBtn;

    public StorageEncap(int idimage, String storageTitle, String storageDescription, MaterialButton addItemBtn, MaterialButton editStorageBtn){
        this.image = idimage;
        this.storageTitle = storageTitle;
        this.storageDescription = storageDescription;
        this.addItemBtn = addItemBtn;
        this.editStorageBtn = editStorageBtn;
    }

    public String get_storageTitle(){return storageTitle;}
    public String get_storageDescription(){return storageDescription;}
    public int get_idImage(){return image;}


}