package com.pranvera.root.pranvera.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.adapter.VisitListAdapter;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorPatients;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorVisits;
import com.pranvera.root.pranvera.model.Visit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VisitListActivity extends Activity {

    private JSONArray jsonArray;
    private ListView VisitListViewAdapter;
    private List<Visit> visits=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        Integer patientId=new Integer(getIntent().getStringExtra("PatientId"));

        String leVisite=getIntent().getStringExtra("listArrayVisitsOfPatient");

        NetAsync();

        VisitListViewAdapter = (ListView) findViewById(R.id.listView);

       //e un thread asinchrono che chiama i restfu solo quando provengo da un paziente

            //new ProcessVisitList().execute();



        VisitListViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long id) {
                JSONArray visitjsonarr=null;
                JSONObject thevisitjsonobject=null;
                Intent intent = new Intent(VisitListActivity.this, VisitFormActivity.class);



                String patientIdStok=getPatientIdStok(getIntent().getStringExtra("listArrayVisitsOfPatient"));



                intent.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
                intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                String pospos=String.valueOf(pos);
                intent.putExtra("Pos",pospos);

                intent.putExtra("listArrayVisitsOfPatient",getIntent().getStringExtra("listArrayVisitsOfPatient"));
                intent.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                intent.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                intent.putExtra("Position",getIntent().getStringExtra("Position"));

                intent.putExtra("PatientStock",patientIdStok);


                try {
                    visitjsonarr =new JSONArray(getIntent().getStringExtra("listArrayVisitsOfPatient"));
                    thevisitjsonobject=(JSONObject)visitjsonarr.get(pos);
                    intent.putExtra("TheVisit",thevisitjsonobject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                Log.v("long clicked", "pos: " + pos);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.visit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.action_back_button:

                Intent intent = new Intent(VisitListActivity.this, MainActivity.class);
                intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                intent.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            case  R.id.action_new_on_main:

                // in questo caso dovrei prendere i stok cioe l'entita patientId
                // detailList e keyword vano settato a null
                // invece pictures id sara di default a poi quando andro a fare una foto si modifichera

               // String patientIdStok=getPatientIdStok(getIntent().getStringExtra("listArrayVisitsOfPatient"));

                Intent intent2 = new Intent(VisitListActivity.this, EditVisitFormActivity.class);
                intent2.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
                intent2.putExtra("Doc",getIntent().getStringExtra("Doc"));
                //intent2.putExtra("Pos",getIntent().getStringExtra("Pos"))
                intent2.putExtra("listArrayVisitsOfPatient",getIntent().getStringExtra("listArrayVisitsOfPatient"));
                intent2.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                intent2.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));
                intent2.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                intent2.putExtra("Position",getIntent().getStringExtra("Position"));
                startActivity(intent2);
                break;
            case R.id.action_update_patient:
                Intent updateIntent = new Intent(VisitListActivity.this,PatientFormActivity.class);
                updateIntent.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                updateIntent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(updateIntent);
                break;
            case R.id.action_delete_patient:
                DeleteAsync();



        }

        return super.onOptionsItemSelected(item);
    }



    public String getPatientIdStok(String jsonArrOfVisits){
        JSONObject visitStock=null;
        JSONObject patientIdStock=null;

        JSONArray arr;

        try {
            arr=new JSONArray(jsonArrOfVisits);
            visitStock=arr.getJSONObject(0);
            patientIdStock=(JSONObject)visitStock.get("patientId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return patientIdStock.toString();
    }


    public void setListAdapter(JSONArray retval){

        JSONObject tmp=null;
        JSONObject pictures=null;

        JSONArray descriptionList=null;
        JSONObject content=null;
        JSONObject type=null;
        JSONObject ilComponente=null;


        List<Visit> visits=new ArrayList<Visit>();
        Visit[] tmpVisit=new Visit[retval.length()];
        String contentName="";
        String image;
        for(int i=0;i<retval.length();i++){

                    try {

                        tmp = retval.getJSONObject(i);
                        descriptionList=(JSONArray)tmp.get("descriptionList");

                                for(int j=0;j<descriptionList.length();j++)
                                {
                                    ilComponente=descriptionList.getJSONObject(j);
                                    type=ilComponente.getJSONObject("type");

                                    if(type.getInt("id")==2){

                                        if(ilComponente.isNull("content")){

                                        }
                                        else{
                                            contentName=(String)ilComponente.get("content");
                                        }



                                        //break;
                                    }
                                }
                        tmpVisit[i]= new Visit();
                        tmpVisit[i].setContent(contentName);
                        pictures=(JSONObject)tmp.get("pictureId");
                        image=(String)pictures.get("filename");
                        tmpVisit[i].setImage(image);
                        visits.add(tmpVisit[i]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


        }

        VisitListViewAdapter.setAdapter(new VisitListAdapter(visits, VisitListActivity.this));

    }

    private class ProcessVisitList extends AsyncTask<String,String,JSONArray> {

        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(VisitListActivity.this);
            nDialog.setTitle("Checking Data");
            nDialog.setMessage("Please wait a sec..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray retval=null;


            Integer patientId=new Integer(getIntent().getStringExtra("PatientId"));





           /** String leVisite=getIntent().getStringExtra("listArrayVisitsOfPatient");
            if(leVisite!=null){
                JSONArray nonchiamoirest=null;
                Log.v("NON CHIAMO I REST PERVISIT", "pos: ");
                try {
                    nonchiamoirest=new JSONArray(leVisite);
                    setListAdapter(nonchiamoirest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return nonchiamoirest;
            }

            else
            {


                retval=new ApiConnectorVisits().GetVisitList(patientId);



                return retval;
            }*/
            retval=new ApiConnectorVisits().GetVisitList(patientId);
            return retval;
        }



        @Override
        protected void onPostExecute(JSONArray visits){

            nDialog.dismiss();


            jsonArray=visits;
            getIntent().putExtra("listArrayVisitsOfPatient",jsonArray.toString());
           // getIntent().getStringExtra("listArrayVisitsOfPatient").replace("listArrayVisitsOfPatient",jsonArray.toString());
            setListAdapter(visits);

        }
    }

    //deelete the patient from here
    private class DeleteAPatient extends AsyncTask<String,String,JSONObject> {

        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(VisitListActivity.this);
            nDialog.setTitle("Checking Data");
            nDialog.setMessage("Please wait a sec..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject retval=null;
            JSONObject kyPatient=null;
            Integer patId=null;

            getIntent().getStringExtra("theClickedPatient");
            try {
                kyPatient=new JSONObject(getIntent().getStringExtra("theClickedPatient"));
                patId=new Integer(kyPatient.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            retval=new ApiConnectorPatients().DeleteAPatient(patId);



            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject patient){

            nDialog.dismiss();

            Intent deleteIntent = new Intent(VisitListActivity.this,MainActivity.class);
            deleteIntent.putExtra("Doc",getIntent().getStringExtra("Doc"));
            deleteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deleteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(deleteIntent);



        }
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(VisitListActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
//                    URL url = new URL("http://192.168.56.1:8080/pranvera");
                    URL url = new URL("http://spring-pranvera.rhcloud.com/pranvera");




                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessVisitList().execute();
            }
            else{
                //nDialog.dismiss();
                nDialog.setMessage("The server is down. Sorry!");
                nDialog.setIndeterminate(true);
                nDialog.show();



            }
        }
    }

    private class NetCheckForDelete extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(VisitListActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
//                    URL url = new URL("http://192.168.56.1:8080/pranvera");
                    URL url = new URL("http://spring-pranvera.rhcloud.com/pranvera");





                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new DeleteAPatient().execute();
            }
            else{
                //nDialog.dismiss();
                nDialog.setMessage("The server is down. Sorry!");
                nDialog.setIndeterminate(true);
                nDialog.show();



            }
        }
    }

    public void NetAsync(){
        new NetCheck().execute();
    }
    public void DeleteAsync(){
        new NetCheckForDelete().execute();
    }
}
