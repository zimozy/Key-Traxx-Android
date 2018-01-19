package com.example.keytraxx;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ibgtraining4 on 12/29/17.
 */

public class bleAdapter extends RecyclerView.Adapter<bleAdapter.ViewHolder> {

    private String[] dataSet;

    public bleAdapter(String[] myDataset) {
        dataSet = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextAppearance(parent.getContext(), R.style.TextAppearance_AppCompat_Headline);

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { holder.textView.setText(dataSet[position]); }

    @Override
    public int getItemCount() { return dataSet.length; }







    // INNER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView view) {
            super(view);
            this.textView = view;
        }
    }
    // ------------

}