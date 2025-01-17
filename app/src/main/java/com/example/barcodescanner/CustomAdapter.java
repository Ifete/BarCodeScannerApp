package com.example.barcodescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class CustomAdapter extends BaseAdapter {
    private ArrayList<?> dataEntry;
    private int R_layout_IdView;
    private Context context;

    public CustomAdapter(Context context, int R_layout_IdView, ArrayList<?> dataEntry){
        super();
        this.context = context;
        this.dataEntry = dataEntry;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public int getCount() {
        return dataEntry.size();
    }

    @Override
    public Object getItem(int posicion) {
        return dataEntry.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
    public void ondataEntry(Object dataEntry, View view){

    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {
        if (view==null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView,null);
        }

        ondataEntry(dataEntry.get(posicion),view);
        return view;
    }
}
