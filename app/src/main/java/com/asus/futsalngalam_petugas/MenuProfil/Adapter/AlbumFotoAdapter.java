package com.asus.futsalngalam_petugas.MenuProfil.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.futsalngalam_petugas.MenuProfil.Model.AlbumFoto;
import com.asus.futsalngalam_petugas.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumFotoAdapter extends RecyclerView.Adapter<AlbumFotoAdapter.ViewHolder> {

    Context context;
    List<AlbumFoto> albumFotoList;

    public AlbumFotoAdapter(Context context, List<AlbumFoto> TempList) {
        this.context = context;
        this.albumFotoList = TempList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foto_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumFoto UploadInfo = albumFotoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return albumFotoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
        }
    }
}