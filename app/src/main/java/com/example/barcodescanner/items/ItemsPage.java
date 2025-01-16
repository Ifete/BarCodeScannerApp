package com.example.barcodescanner.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.AddItemPage;
import com.example.barcodescanner.R;
import com.example.barcodescanner.settings.SettingsPage;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class ItemsPage extends AppCompatActivity {
    private FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_page);
        addBtn = findViewById(R.id.plusFab);
        addBtn.show();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemsPage = new Intent(ItemsPage.this, AddItemPage.class);
                ItemsPage.this.startActivity(addItemsPage);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemsMenu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.storageMenu:
                        Intent itemsPage = new Intent(ItemsPage.this, StoragePage.class);
                        ItemsPage.this.startActivity(itemsPage);
                        return true;
                    case R.id.itemsMenu:
                        return true;
                    case R.id.settingsMenu:
                        Intent settingsPage = new Intent(ItemsPage.this, SettingsPage.class);
                        ItemsPage.this.startActivity(settingsPage);
                        return true;
                }
                return false;
            }
        });
    }
}
