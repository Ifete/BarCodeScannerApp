package com.example.barcodescanner.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.example.barcodescanner.items.ItemsPage;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settingsMenu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.storageMenu:
                        Intent storagePage = new Intent(SettingsPage.this, StoragePage.class);
                        SettingsPage.this.startActivity(storagePage);
                        return true;
                    case R.id.itemsMenu:
                        Intent itemsPage = new Intent(SettingsPage.this, ItemsPage.class);
                        SettingsPage.this.startActivity(itemsPage);
                        return true;
                    case R.id.settingsMenu:
                        return true;
                }
                return false;
            }
        });
    }
}
