package com.pranvera.root.pranvera.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.model.Visit;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by root on 11/01/15.
 */



public class VisitListAdapter extends BaseAdapter {
    private List<Visit> visits;
    private Activity activity;
    private static LayoutInflater inflater =null;
    //private static final String baseUrlForImage = "http://192.168.56.1:8080/pranveraimages/visits/";
    private static final String baseUrlForImage = "http://spring-pranvera.rhcloud.com/pranveraimages/visits/";


    public VisitListAdapter(List<Visit> visits,Activity a){
        this.visits=visits;
        this.activity=a;
        inflater=(LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return visits == null ? 0 : visits.size();
    }

    public Visit getItem(int position) {
        return visits.get(position);
    }
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Visit visit = getItem(position);
        View view = convertView;
        final ListCell cell;


        if (view == null) {
            view = inflater.inflate(R.layout.visit_cell_item, parent, false);

            cell=new ListCell();


            cell.contentName = (TextView)view.findViewById(R.id.content_name);
            cell.visitImage=(ImageView)view.findViewById(R.id.visit_image);

            cell.contentName.setText(visit.getContent());
            cell.visitImage.setImageResource(R.drawable.avatar_empty);

            view.setTag(cell);
        }
        else
        {
            cell = (ListCell) view.getTag();
        }

        //change the data of cell
        try {
            cell.contentName.setText(visit.getContent());
            cell.visitImage.setImageResource(R.drawable.avatar_empty);


            //String nameOfImage = "p1.jpg";

            String nameOfImage=visit.getImageVisit();

            String urlForImageServer = baseUrlForImage + nameOfImage;


            new AsyncTask<String, Void, Bitmap>(){

                @Override
                protected Bitmap doInBackground(String... params){

                    String url = params[0];
                    Bitmap icon = null;

                    try
                    {
                        InputStream in = new java.net.URL(url).openStream();
                        icon = BitmapFactory.decodeStream(in);
                    }
                    catch(MalformedURLException e){
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    return icon;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    cell.visitImage.setImageBitmap(bitmap);
                }
            }.execute(urlForImageServer);


        }
        catch(Exception e){
            e.printStackTrace();

        }





        return view;
    }

    private class ListCell
    {
        private TextView contentName;
        private ImageView visitImage;
    }
}


















