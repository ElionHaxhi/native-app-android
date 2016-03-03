package com.pranvera.root.pranvera.ui.fragmentonvisit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranvera.root.pranvera.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by root on 12/01/15.
 */
public class DetailFragment extends Fragment {
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    //private  String urlForImageServer = "http://192.168.56.1:8080/docmanimages/visits/";
    private  String urlForImageServer = "http://spring-pranvera.rhcloud.com/pranveraimages/visits/";

    static ImageView imageView;

    static TextView goal_exam_desc;
    //public static TextView goal_exam;
    static TextView goal_exam2;

    static TextView terapia;
    static TextView terapia_desc;

    static TextView diagnostic;
    static TextView diagnostic_desc;

    static TextView anamnesi;
    static TextView anamnesi_desc;

    static JSONObject laVisita=null;

    static JSONArray theDescriptionList=null;

    static EditText goal_exam_editText;
    static EditText teraphy_editText;
    static EditText diagnostic_editText;
    static EditText anamnesi_editText;

    Intent mIntent=null;




    public DetailFragment() {


    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
       // mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);

        JSONObject tmp=null;
        JSONObject subtmp=null;
        JSONObject imagevisit=null;
        String theurlofimage=null;



        Integer theId=null;

        Bundle bundle=this.getArguments();

        try {

            laVisita =new JSONObject(bundle.getString("TheVisit"));
            imagevisit=(JSONObject)laVisita.get("pictureId");
            theId=laVisita.getInt("id");
            theurlofimage=urlForImageServer+imagevisit.getString("filename");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(theId!=-1) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            imageView = (ImageView) rootView.findViewById(R.id.visit_image_of_visit);
            goal_exam2 = (TextView) rootView.findViewById(R.id.textview_dettagli);
            goal_exam_desc = (TextView) rootView.findViewById(R.id.textview_dettagli_retval);
            terapia = (TextView) rootView.findViewById(R.id.textview_terapy);
            terapia_desc = (TextView) rootView.findViewById(R.id.textview_terapy_retval);
            diagnostic = (TextView) rootView.findViewById(R.id.textview_diagnostic);
            diagnostic_desc = (TextView) rootView.findViewById(R.id.textview_diagnostic_retval);
            anamnesi = (TextView) rootView.findViewById(R.id.textview_anamnesi);
            anamnesi_desc = (TextView) rootView.findViewById(R.id.textview_anamnesi_retval);



            try {
                theDescriptionList = (JSONArray) laVisita.get("descriptionList");


                for(int i=0;i<theDescriptionList.length();i++){

                    tmp = (JSONObject) theDescriptionList.get(i);
                    subtmp = (JSONObject) tmp.get("type");

                    if(subtmp.getInt("id")==1)
                    {

                        anamnesi.setText(subtmp.getString("name"));

                        if (!tmp.isNull("content"))
                            anamnesi_desc.setText(tmp.getString("content"));
                        else anamnesi_desc.setText("");
                    }
                    else if(subtmp.getInt("id")==2)
                    {

                        goal_exam2.setText(subtmp.getString("name"));

                        if (!tmp.isNull("content"))
                            goal_exam_desc.setText(tmp.getString("content"));
                        else goal_exam_desc.setText("");
                    }
                    else if(subtmp.getInt("id")==3)
                    {
                        terapia.setText(subtmp.getString("name"));

                        if (!tmp.isNull("content"))
                            terapia_desc.setText(tmp.getString("content"));
                        else terapia_desc.setText("");

                    }
                    else if(subtmp.getInt("id")==4)
                    {

                        diagnostic.setText(subtmp.getString("name"));

                        if (!tmp.isNull("content"))
                            diagnostic_desc.setText(tmp.getString("content"));
                        else diagnostic_desc.setText("");
                    }
                    else{
                        // qui ce un errore
                        Log.v("ERRORE NELL DESCRIPTIONLIST", "pos: ");
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }


            new AsyncTask<String, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(String... params) {

                    String url = params[0];
                    Bitmap icon = null;

                    try {
                        InputStream in = new java.net.URL(url).openStream();
                        icon = BitmapFactory.decodeStream(in);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return icon;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            }.execute(theurlofimage);


            return rootView;
        }
        else{
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            return  rootView;
        }
    }

    private static String getTheStockVisit(String theStockPatientId,Intent intent){
        JSONObject patientId=null;
        JSONObject picturesId=null;
        JSONArray descriptionList=null;
        JSONArray detailList=null;

        JSONObject theVisit=null;


        JSONObject esameobietivo=null;
        JSONObject anamnesi=null;
        JSONObject terapia=null;
        JSONObject diagnosi=null;


        JSONObject typeesameobietivo=null;
        JSONObject typeanamnesi=null;
        JSONObject typeterapia=null;
        JSONObject typediagnosi=null;


        try {
            patientId=new JSONObject(theStockPatientId);

            picturesId=new JSONObject();
            picturesId.put("id",1);
            picturesId.put("filename","default.jpg");

            descriptionList=new JSONArray();
            detailList=new JSONArray();


            esameobietivo=new JSONObject();
            esameobietivo.put("id",-1);
            esameobietivo.put("content","");

            typeesameobietivo=new JSONObject();
            typeesameobietivo.put("id",2);
            typeesameobietivo.put("name","Esame obiettivo");

            esameobietivo.put("type",typeesameobietivo);

            anamnesi=new JSONObject();
            anamnesi.put("id", -1);
            anamnesi.put("content","");

            typeanamnesi=new JSONObject();
            typeanamnesi.put("id", 1);
            typeanamnesi.put("name", "Anamnesi");

            anamnesi.put("type",typeanamnesi);

            terapia=new JSONObject();
            terapia.put("id",-1);
            terapia.put("content","");

            typeterapia=new JSONObject();
            typeterapia.put("id",3);
            typeterapia.put("name","Terapia");

            terapia.put("type",typeterapia);

            diagnosi=new JSONObject();
            diagnosi.put("id",-1);
            diagnosi.put("content","");

            typediagnosi=new JSONObject();
            typediagnosi.put("id",4);
            typediagnosi.put("name","Diagnosi");

            diagnosi.put("type",typediagnosi);

            descriptionList.put(esameobietivo);
            descriptionList.put(anamnesi);
            descriptionList.put(terapia);
            descriptionList.put(diagnosi);

            theVisit=new JSONObject();
            theVisit.put("id",-1);
            theVisit.put("creationDate",new Date().getTime());
            theVisit.put("lastmodDate",null);
            theVisit.put("trash",null);
            theVisit.put("trashDate",null);
            theVisit.put("detailList",detailList);
            theVisit.put("descriptionList",descriptionList);
            theVisit.put("picturesId",picturesId);
            theVisit.put("keywordId",null);
            theVisit.put("patientId",patientId);









        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("TheNewVisit",theVisit.toString());

        return theVisit.toString();
    }


}
