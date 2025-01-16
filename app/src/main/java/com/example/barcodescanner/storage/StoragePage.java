package com.example.barcodescanner.storage;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.AddItemPage;
import com.example.barcodescanner.R;
import com.example.barcodescanner.items.ItemsPage;
import com.example.barcodescanner.settings.SettingsPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

public class StoragePage extends AppCompatActivity {
    private MaterialButton editStorageBtn, addItemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_page);

        editStorageBtn = findViewById(R.id.editStorageBtn);
        addItemBtn = findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemPage = new Intent(StoragePage.this, AddItemPage.class);
                StoragePage.this.startActivity(addItemPage);
            }
        });

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
