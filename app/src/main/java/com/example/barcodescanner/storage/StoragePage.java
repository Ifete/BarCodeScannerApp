package com.example.barcodescanner.storage;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.AddItemPage;
import com.example.barcodescanner.CustomAdapter;
import com.example.barcodescanner.R;
import com.example.barcodescanner.encapsulator.ItemEncap;
import com.example.barcodescanner.encapsulator.StorageEncap;
import com.example.barcodescanner.items.ItemsPage;
import com.example.barcodescanner.settings.SettingsPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StoragePage extends AppCompatActivity {
    private MaterialButton editStorageBtn, addItemBtn;
    private GridView itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_page);

        //Assigning the gridview to the itemsList variable
        itemsList = findViewById(R.id.storageList);

        //Creating the data for the storage items
        ArrayList<StorageEncap> itemData = new ArrayList<>();
        itemData.add(new StorageEncap(R.drawable.ic_fridge_foreground, "Nevera", "Nevera de la cocina", addItemBtn, editStorageBtn));
        itemData.add(new StorageEncap(R.drawable.ic_fridge_foreground, "Congelador", "Congelador de la cocina", addItemBtn, editStorageBtn));
        itemData.add(new StorageEncap(R.drawable.ic_fridge_foreground, "Despensa", "Despensa de la cocina", addItemBtn, editStorageBtn));
        itemData.add(new StorageEncap(R.drawable.ic_fridge_foreground, "Alacena", "Alacena de la cocina", addItemBtn, editStorageBtn));

        //Setting the adapter for the gridview
        itemsList.setAdapter(new CustomAdapter(this, R.layout.storage_item,itemData){
            @Override
            public void ondataEntry (Object entrada, View view){
                TextView storageTitle = (TextView) view.findViewById(R.id.txtTitleStorage);
                TextView storageDescription = (TextView) view.findViewById(R.id.txtDescriptionStorage);
                ImageView storageImage = (ImageView) view.findViewById(R.id.imgStorage);

                storageTitle.setText(((StorageEncap) entrada).get_storageTitle());
                storageDescription.setText(((StorageEncap) entrada).get_storageDescription());
                storageImage.setImageResource(((StorageEncap) entrada).get_idImage());

                MaterialButton addItemBtn = (MaterialButton) view.findViewById(R.id.btnAddItem);
                MaterialButton editStorageBtn = (MaterialButton) view.findViewById(R.id.btnEditStorage);

                addItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent addItemPage = new Intent(StoragePage.this, AddItemPage.class);
                        StoragePage.this.startActivity(addItemPage);
                    }
                });
            }
        });





        //Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.storageMenu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.storageMenu:
                        return true;
                    case R.id.itemsMenu:
                        Intent itemsPage = new Intent(StoragePage.this, ItemsPage.class);
                        StoragePage.this.startActivity(itemsPage);
                        return true;
                    case R.id.settingsMenu:
                        Intent settingsPage = new Intent(StoragePage.this, SettingsPage.class);
                        StoragePage.this.startActivity(settingsPage);
                        return true;
                }
                return false;
            }
        });

    }


}
