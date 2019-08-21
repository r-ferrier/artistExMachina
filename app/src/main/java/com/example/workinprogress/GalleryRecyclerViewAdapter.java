package com.example.workinprogress;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ImageViewHolder> {

    private File[] images;

    // class to create temporary holding spaces for each image
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }

    //constructor simply takes in the array of image sources
    public GalleryRecyclerViewAdapter(File[] images) {
        this.images = images;
    }

    //this method is invoked by the layoutManager when it is setting up the view. It will inflate
    //the layout being used as a thumbnail and add it to its list of images to lay out.
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_view, parent,false);
        ImageViewHolder holder = new ImageViewHolder(imageView);
        return holder;
    }

    // this method takes the empty image holders and inserts the images from images[] into them
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(images[position].getPath()));
        holder.imageView.setContentDescription(images[position].getPath());
    }

    // returns the dataset size
    @Override
    public int getItemCount() {
        return images.length;
    }
}