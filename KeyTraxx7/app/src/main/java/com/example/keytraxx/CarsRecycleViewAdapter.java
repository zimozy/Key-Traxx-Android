package com.example.keytraxx;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ibgtraining4 on 1/3/18.
 */

class CarsRecycleViewAdapter extends RecyclerView.Adapter {

    private String[] dataSet;

    public CarsRecycleViewAdapter(String[] dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
