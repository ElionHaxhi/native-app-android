package com.pranvera.root.pranvera.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by root on 05/01/15.
 */
public class ModelJsonObjectVisit {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public ModelJsonObjectVisit() {

    }


    // qeusto metodo dovrebbe essere usato per fare l'update di un paziente
    public JSONObject createAVisitObjectJSON(String goalExam,String terapy,String diagnosi,String anamnesi,String theStockVisit,JSONObject picturesId) {

        JSONObject visita=null;
        JSONArray arr=null;
        JSONObject theObject=null;
        JSONObject type=null;


        try {
            visita=new JSONObject(theStockVisit);
            visita.put("pictureId",picturesId);

            arr=(JSONArray)visita.get("descriptionList");



            for(int i=0;i<arr.length();i++){
                theObject=arr.getJSONObject(i);
                type=theObject.getJSONObject("type");
                if(type.getInt("id")==1)
                {
                    arr.getJSONObject(i).put("content",anamnesi);
                }
                else if(type.getInt("id")==2)
                {
                    arr.getJSONObject(i).put("content",goalExam);

                }
                else if(type.getInt("id")==3)
                {
                    arr.getJSONObject(i).put("content",terapy);

                }
                else if(type.getInt("id")==4)
                {
                    arr.getJSONObject(i).put("content",diagnosi);

                }
                else
                {
                    Log.v("ERRORE NELL NON PERSITI BENE I DATI", "pos: ");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return visita;

    }

    public JSONObject updateAVisitObjectJSON(String goalExam,String terapy,String diagnosi,String anamnesi,String theStockVisit,JSONObject picturesId) {

        JSONObject visita=null;
        JSONArray arr=null;
        JSONObject theObject=null;
        JSONObject type=null;


        try {
            visita=new JSONObject(theStockVisit);
            visita.put("pictureId",picturesId);

            arr=(JSONArray)visita.get("descriptionList");



            for(int i=0;i<arr.length();i++){
                theObject=arr.getJSONObject(i);
                type=theObject.getJSONObject("type");
                if(type.getInt("id")==1)
                {
                    arr.getJSONObject(i).put("content",anamnesi);
                }
                else if(type.getInt("id")==2)
                {
                    arr.getJSONObject(i).put("content",goalExam);

                }
                else if(type.getInt("id")==3)
                {
                    arr.getJSONObject(i).put("content",terapy);

                }
                else if(type.getInt("id")==4)
                {
                    arr.getJSONObject(i).put("content",diagnosi);

                }
                else
                {
                    Log.v("ERRORE NELL NON PERSITI BENE I DATI", "pos: ");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return visita;

    }

}
