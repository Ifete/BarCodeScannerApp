package com.example.barcodescanner.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.AddItemPage;
import com.example.barcodescanner.CustomAdapter;
import com.example.barcodescanner.R;
import com.example.barcodescanner.encapsulator.ItemEncap;
import com.example.barcodescanner.settings.SettingsPage;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ItemsPage extends AppCompatActivity {
    private FloatingActionButton addBtnFloating;
    private ListView itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_page);

        //Add new item floating button
        addBtnFloating = findViewById(R.id.btnAddItemFloating);
        addBtnFloating.show();
        addBtnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemsPage = new Intent(ItemsPage.this, AddItemPage.class);
                ItemsPage.this.startActivity(addItemsPage);
            }
        });

        //Item list inflater
        itemsList = findViewById(R.id.itemsList);

        ArrayList<ItemEncap> itemData = new ArrayList<>();
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Masa Pizza", "Nevera", 5));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Coca Cola", "Nevera", 10));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Lechuga", "Nevera", 15));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Pechuga de Pollo", "Congelador", 8));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Queso Cheddar", "Nevera", 12));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Manzanas", "Despensa", 20));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Papas Fritas", "Despensa", 30));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Pan Integral", "Despensa", 7));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Filete de Merluza", "Congelador", 6));
        itemData.add(new ItemEncap(R.drawable.ic_food_foreground, "Helado de Vainilla", "Congelador", 4));

        itemsList.setAdapter(new CustomAdapter(this, R.layout.items_item,itemData){
            @Override
            public void ondataEntry (Object entrada, View view){
                TextView itemTitle = (TextView) view.findViewById(R.id.titleItem);
                TextView itemStorage = (TextView) view.findViewById(R.id.titleStorage);
                ImageView itemImage = (ImageView) view.findViewById(R.id.imgItem);
                TextView itemQuantity = (TextView) view.findViewById(R.id.quantity);

                itemTitle.setText(((ItemEncap) entrada).get_foodTitle());
                itemStorage.setText(((ItemEncap) entrada).get_storage());
                itemImage.setImageResource(((ItemEncap) entrada).get_idImage());
                itemQuantity.setText(String.valueOf(((ItemEncap) entrada).get_qty()));
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
