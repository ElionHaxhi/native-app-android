package com.pranvera.root.pranvera.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.model.VisitGalleryCell;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by root on 17/01/15.
 */
public class GridAdapter extends ArrayAdapter<VisitGalleryCell> {
        protected Context mContext;
        private Activity activity;
        protected List<VisitGalleryCell> mImages;
        String urls=null;

        DisplayImageOptions options;

        private static final String baseUrlForImage = "http://192.168.56.1:8080/pranveraimages/visits/";

        String tmp="https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg";

        public GridAdapter(Context context, List<VisitGalleryCell> images){
            super(context, R.layout.grid_cell,images);
            mContext = context;
            mImages = images;

    }



    public View getView(int position, View convertView, ViewGroup parent) {

       final ViewHolder holder;
        View view=convertView;
        if(view==null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_cell, null);
            holder=new ViewHolder();
            holder.iconImageView=(ImageView)view.findViewById(R.id.grid_image);

            view.setTag(holder);

        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        VisitGalleryCell visitgalerry =mImages.get(position);
        String lastpjg=visitgalerry.getImage();

        if(lastpjg.equals("")){
            holder.iconImageView.setImageResource(R.drawable.avatar_empty);
        }
        else{
            //String hash = MD5Util.md5Hex(email);
            String gravatarUrl = baseUrlForImage +lastpjg ;
            Picasso.with(mContext)
                    .load(gravatarUrl)
                    .placeholder(R.drawable.avatar_empty)
                    .into(holder.iconImageView);

        }


        return view;



    }

    public void refill(List<VisitGalleryCell> images){
        mImages.clear();
        mImages.addAll(images);
    }

    static class ViewHolder{
        ImageView iconImageView;
        ProgressBar progressBar;

    }




}
