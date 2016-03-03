package com.pranvera.root.pranvera.parsers;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 05/01/15.
 */
public class ModelJsonObject {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public ModelJsonObject() {

    }
    // qui devo creare il patien personalizato
    public JSONObject createAPatient() throws JSONException {
        JSONObject patient=null;
        JSONObject picturesId=new JSONObject();
        JSONObject personalInfoId=new JSONObject();

        JSONObject couponId = new JSONObject();
        JSONObject peronsalInfoIdDoctor = new JSONObject();
        JSONObject doctor = new JSONObject();


        JSONObject clientId = new JSONObject();
        JSONObject referent = new JSONObject();

        referent.put("id",12);
        referent.put("name","test");
        referent.put("surname","coupon");
        referent.put("address",null);
        referent.put("email",null);
        referent.put("cellphone",null);
        referent.put("telephone",null);

        clientId.put("id",1);
        clientId.put("name","elionhaxhi");
        clientId.put("description","desc");
        clientId.put("trash",false);
        clientId.put("trashDate",null);
        clientId.put("referent",referent);

        couponId.put("id",1);
        couponId.put("nrCoupon","6");
        couponId.put("size",6);
        couponId.put("valid",true);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2014, Calendar.MAY, 20, 15, 00, 00);
        Date creation = cal.getTime();

        couponId.put("creationDate",creation.getTime());


        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(0);
        cal2.set(2016, Calendar.AUGUST, 28, 16, 15, 20);
        Date deadLine = cal2.getTime();


        couponId.put("deadLine", deadLine.getTime());
        couponId.put("trash",false);
        couponId.put("trashDate",null);
        couponId.put("clientId",clientId);


        peronsalInfoIdDoctor.put("id",1);
        peronsalInfoIdDoctor.put("name","admin");
        peronsalInfoIdDoctor.put("surname","admin");
        peronsalInfoIdDoctor.put("address",null);
        peronsalInfoIdDoctor.put("email",null);
        peronsalInfoIdDoctor.put("cellphone",null);
        peronsalInfoIdDoctor.put("telephone",null);

        doctor.put("id",6);
        doctor.put("username","admin");
        doctor.put("password","admin");
        doctor.put("trash",false);
        doctor.put("trashDate",null);
        doctor.put("registrationDate",null);

        doctor.put("personalInfoId",peronsalInfoIdDoctor);
        doctor.put("couponId",couponId);






        JSONArray doctorList=new JSONArray();

        doctorList.put(doctor);

        picturesId.put("id",1);
        picturesId.put("filename","default.jpg");

        personalInfoId.put("id",-1);
        personalInfoId.put("name","eclipse");
        personalInfoId.put("surname","kepler");
        personalInfoId.put("address","");
        personalInfoId.put("email","eclipsecepler@gmail.com");
        personalInfoId.put("cellphone","22222222");
        personalInfoId.put("telephone",null);

        patient = new JSONObject();



        patient.put("id", -1);
        Date registrationDate= new Date();
        patient.put("date",registrationDate.getTime());
        patient.put("trash",false);
        patient.put("trashDate",null);
        patient.put("doctorList",doctorList);
        patient.put("personalInfoId",personalInfoId);
        patient.put("picturesId",picturesId);









        return patient;

    }
    // crea un doctor in formto json per inserirlo ne db
    public JSONObject createADoctorObjectJSON(String name,String surname,String address,String email,String celulare,String telafono,String username,String password) throws JSONException {
        JSONObject doctor=null;



        JSONObject personalinfoId= new JSONObject();
        personalinfoId.put("id",-1);
        personalinfoId.put("name",name);
        personalinfoId.put("surname",surname);
        personalinfoId.put("address",address);
        personalinfoId.put("email",email);
        personalinfoId.put("cellphone",celulare);
        personalinfoId.put("telephone",telafono);


        doctor=new JSONObject();
        doctor.put("id",-1);
        doctor.put("username",username);
        doctor.put("password",password);
        doctor.put("trash",null);
        doctor.put("trashDate",null);
        doctor.put("registrationDate",null);
        doctor.put("personalInfoId",personalinfoId);
        doctor.put("couponId",null);

return doctor;
    }
    public JSONObject createAPatientObjectJSON(String name,String surname,String address,String email,String celulare,String telafono,JSONArray theDocList,String thePatientToUpdate,JSONObject picturesId) throws JSONException {
        JSONObject patient=null;

        // ho il theDocList gia pronto per infilarlo al JSONObjec

        if(thePatientToUpdate!=null){
            JSONObject theJsonPatientToUpdte=new JSONObject(thePatientToUpdate);

            if(new Integer(theJsonPatientToUpdte.get("id").toString())==-1){
                    JSONObject personalinfoId= new JSONObject();
                    personalinfoId.put("id",-1);
                    personalinfoId.put("name",name);
                    personalinfoId.put("surname",surname);
                    personalinfoId.put("address",address);
                    personalinfoId.put("email",email);
                    personalinfoId.put("cellphone",celulare);
                    personalinfoId.put("telephone",telafono);

                    if(picturesId.isNull("id"))
                    {
                        picturesId = new JSONObject();
                        picturesId.put("id", 1);
                        picturesId.put("filename","default.png");
                    }


                    patient=new JSONObject();
                    patient.put("id",-1);
                    patient.put("date",(new Date().getTime()));
                    patient.put("trash",null);
                    patient.put("trashDate",null);

                   // JSONArray doctorList=new JSONArray();
                   // doctorList.put(new JSONObject(theDocList));
                    patient.put("doctorList",theDocList);

                    patient.put("personalInfoId",personalinfoId);
                    patient.put("pictureId",picturesId);

                return patient;
            }
        else
            {
                JSONObject personalinfoIdDue=(JSONObject)theJsonPatientToUpdte.get("personalInfoId");
                //personalinfoIdDue.put("id",-1);
                personalinfoIdDue.put("name",name);
                personalinfoIdDue.put("surname",surname);
                personalinfoIdDue.put("address",address);
                personalinfoIdDue.put("email",email);
                personalinfoIdDue.put("cellphone",celulare);
                personalinfoIdDue.put("telephone",telafono);

                theJsonPatientToUpdte.put("personalInfoId",personalinfoIdDue);
                theJsonPatientToUpdte.put("pictureId",picturesId);
                return theJsonPatientToUpdte;
            }
        }
        else{
            JSONObject personalinfoId= new JSONObject();
            personalinfoId.put("id",-1);
            personalinfoId.put("name",name);
            personalinfoId.put("surname",surname);
            personalinfoId.put("address",address);
            personalinfoId.put("email",email);
            personalinfoId.put("cellphone",celulare);
            personalinfoId.put("telephone",telafono);

            if(picturesId.isNull("id"))
            {
                picturesId = new JSONObject();
                picturesId.put("id", 1);
                picturesId.put("filename","default.png");
            }


            patient=new JSONObject();
            patient.put("id",-1);
            patient.put("date",(new Date().getTime()));
            patient.put("trash",null);
            patient.put("trashDate",null);

            JSONArray doctorList=new JSONArray();
            //doctorList.put(new JSONObject(theDocList));
            patient.put("doctorList",theDocList);

            patient.put("personalInfoId",personalinfoId);
            patient.put("pictureId",picturesId);

            return patient;

        }


    }






    public JSONObject getDataFromTheNet() throws IOException {
        // Create the POST object and add the parameters
        JSONObject objectToPost=null;
        try {
            objectToPost=this.createAPatient();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost("http://192.168.56.1:8081/Docman/webresources/patients");
        StringEntity entity = new StringEntity(objectToPost.toString(), HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse = client.execute(httpPost);
        //return response.getEntity().getContent().;



        try{

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }


}
