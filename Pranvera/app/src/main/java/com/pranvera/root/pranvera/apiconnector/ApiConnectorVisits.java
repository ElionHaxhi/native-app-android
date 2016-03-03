package com.pranvera.root.pranvera.apiconnector;

import android.util.Log;

import com.pranvera.root.pranvera.parsers.ModelJsonObjectVisit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by root on 11/01/15.
 */
public class ApiConnectorVisits {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    public JSONArray GetVisitList(int id){
        //url getting all cutomers
        //String url="http://146.148.31.123/docman/webresources/patients/7";
        // String url="http://192.168.1.101:8081/familyweb/webresources/users";
        // String url="http://192.168.1.101/phpsfiles/php/login.php";
        //String url="http://192.168.56.1:8080/docman/webresources/visits/"+id;



        //String url="http://192.168.56.1:8080/pranvera/ws/vs/"+id;

        String url="http://spring-pranvera.rhcloud.com/pranvera/ws/vs/"+id;



        // String url="http://192.168.1.102:8080/docman/webresources/patients/"+id;

        //get httpResponse Object from url
        //get HttpEntity from Http Response Object

        HttpEntity httpEntity=null;

        try{
            DefaultHttpClient httpClient=new DefaultHttpClient();//default httpclient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse=httpClient.execute(httpGet);
            httpEntity=httpResponse.getEntity();
        }
        catch(ClientProtocolException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        JSONArray jsonArray=null;

        if(httpEntity !=null){

            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            }
            catch(JSONException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return jsonArray;
    }


    //qui inserisco una nuova visita faccendo il post
    public JSONObject GetNewVisitResponse(String goalExam,String terapy,String diagnosi,String anamnesi, String theStockVisit,JSONObject picturesId){



//        String url="http://192.168.56.1:8080/docman/webresources/visits";
        //String url="http://192.168.56.1:8080/pranvera/ws/vs/";

        String url="http://spring-pranvera.rhcloud.com/pranvera/ws/vs/";



        // String url="http://192.168.1.102:8080/docman/webresources/doctors";

        JSONObject theVisit=null;

        theVisit=new ModelJsonObjectVisit().createAVisitObjectJSON(goalExam,terapy,diagnosi,anamnesi,theStockVisit,picturesId);


        //get httpResponse Object from url
        //get HttpEntity from Http Response Object


        try{
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(theVisit.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);


            HttpEntity httpEntity=httpResponse.getEntity();
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
        /** try {
         jObj = new JSONObject(json);
         } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
         }*/

        // return JSON String
        return jObj;
    }

    public JSONObject GetUpdateVisitResponse(int id,String goalExam,String terapy,String diagnosi,String anamnesi, String theStockVisit,JSONObject picturesId){


        // ochio da modifichare l'id!!!!!!

//        String url="http://192.168.56.1:8080/docman/webresources/visits/"+id;


        //String url="http://192.168.56.1:8080/pranvera/ws/vs/"+id;

        String url="http://spring-pranvera.rhcloud.com/pranvera/ws/vs/"+id;




        // String url="http://192.168.1.102:8080/docman/webresources/doctors";

        JSONObject theVisit=null;

        theVisit=new ModelJsonObjectVisit().updateAVisitObjectJSON(goalExam,terapy,diagnosi,anamnesi,theStockVisit,picturesId);


        //get httpResponse Object from url
        //get HttpEntity from Http Response Object


        try{
            HttpPut httpPost = new HttpPut(url);
            StringEntity entity = new StringEntity(theVisit.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);


            HttpEntity httpEntity=httpResponse.getEntity();
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
        /** try {
         jObj = new JSONObject(json);
         } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
         }*/

        // return JSON String
        return jObj;
    }

    public JSONObject DeleteAVisit(int id){
        //url getting all cutomers
        //String url="http://146.148.31.123/docman/webresources/patients/7";
        // String url="http://192.168.1.101:8081/familyweb/webresources/users";
        // String url="http://192.168.1.101/phpsfiles/php/login.php";
//        String url="http://192.168.56.1:8080/docman/webresources/visits/"+id;


        //String url="http://192.168.56.1:8080/pranvera/ws/vs/"+id;

        String url="http://spring-pranvera.rhcloud.com/pranvera/ws/vs/"+id;



        // String url="http://192.168.1.102:8080/docman/webresources/patients/"+id;

        //get httpResponse Object from url
        //get HttpEntity from Http Response Object

        String httpStringResponse="";
        JSONObject retval=null;
        try{
            DefaultHttpClient httpClient=new DefaultHttpClient();//default httpclient
            HttpDelete httpDelete = new HttpDelete(url);

            HttpResponse httpResponse=httpClient.execute(httpDelete);
            //httpStringResponse=httpResponse.;

            retval = new JSONObject();
            retval.put("ok",1);
        }
        catch(ClientProtocolException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return retval;
    }

    public JSONObject uploadPictureToServer(byte [] theBytes,String filename) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();

//        HttpPost httppost = new HttpPost("http://192.168.56.1:8080/docman/FileUpload");
        //HttpPost httppost = new HttpPost("http://192.168.56.1:8080/pranvera/FU");


        HttpPost httppost = new HttpPost("http://spring-pranvera.rhcloud.com/pranvera/FU");




        //HttpPost httppost = new HttpPost("http://192.168.1.102:8080/docman/FileUpload");

        ByteArrayBody bab = new ByteArrayBody(theBytes,String.valueOf(new Date().getTime()));



        MultipartEntity mpEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);

        ContentBody cb= new StringBody("../pranveraimages/visits");

        mpEntity.addPart("file", bab);
        mpEntity.addPart("destination",cb);



        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        //  String gtbby=EntityUtils.toString(resEntity);

        //  String ret=gtbby.substring(1,gtbby.length());
        // Header hd= (Header) resEntity.getContentEncoding();
        JSONObject retval=null;
        String trvbrk=EntityUtils.toString(response.getEntity());
        try {
            retval=new JSONObject(trvbrk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            // System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        //is = resEntity.getContent();


        httpclient.getConnectionManager().shutdown();





        return retval;

    }




}
