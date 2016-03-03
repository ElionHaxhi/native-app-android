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

import com.pranvera.root.pranvera.model.Patient;
import com.pranvera.root.pranvera.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;


public class PatientListAdapter extends BaseAdapter {
    private List<Patient> patients;
    private Activity activity;
    private static LayoutInflater inflater =null;
    //private static final String baseUrlForImage = "http://192.168.56.1:8080/pranveraimages/patients/";
    private static final String baseUrlForImage = "http://spring-pranvera.rhcloud.com/pranveraimages/patients/";


    public PatientListAdapter(List<Patient> patients,Activity a){
        this.patients=patients;
        this.activity=a;
        inflater=(LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return patients == null ? 0 : patients.size();
    }

    public Patient getItem(int position) {
        return patients.get(position);
    }
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = getItem(position);
        View view = convertView;
        final ListCell cell;


        if (view == null) {
            view = inflater.inflate(R.layout.patient_cell_item, parent, false);

            cell=new ListCell();


            cell.patientName = (TextView)view.findViewById(R.id.patient_name);
            cell.patientTel=(TextView)view.findViewById(R.id.patient_tel);
            cell.patientImage=(ImageView)view.findViewById(R.id.patient_image);

            cell.patientName.setText(patient.getName());
            cell.patientTel.setText(patient.getTel());
            cell.patientImage.setImageResource(R.drawable.avatar_empty);

            view.setTag(cell);
        }
        else
        {
            cell = (ListCell) view.getTag();
        }

        //change the data of cell
        try {
            cell.patientName.setText(patient.getName());
            cell.patientTel.setText(patient.getTel());
            cell.patientImage.setImageResource(R.drawable.avatar_empty);


            //String nameOfImage = "p1.jpg";

            String nameOfImage=patient.getImagePatient();

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
                    cell.patientImage.setImageBitmap(bitmap);
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
        private TextView patientName;
        private TextView patientTel;
        private ImageView patientImage;
    }
}

















