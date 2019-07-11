package com.example.workinprogress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ImageViewHolder> {
    private String[] images;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ImageViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GalleryRecyclerViewAdapter(String[] images) {
        this.images = images;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_view, parent,false);
        ImageViewHolder holder = new ImageViewHolder(imageView);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(images[position]));
        holder.imageView.setContentDescription(images[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return images.length;
    }



}