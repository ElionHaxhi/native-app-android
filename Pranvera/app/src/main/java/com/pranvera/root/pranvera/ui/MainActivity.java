package com.pranvera.root.pranvera.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.adapter.PatientListAdapter;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorPatients;
import com.pranvera.root.pranvera.model.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ActionBar.TabListener{

    protected JSONObject doc=null;
    private int docId;

    private int theOnLongClickSelectedItemPatient=-1;


    private ListView PatientsListViewAdapter;
    private List<Patient> patients=null;
    private JSONArray jsonArray;

    public void setJSONOBJECT(JSONObject doc)
    {
        this.doc=doc;
    }

    public JSONObject getJSONOBJET(){
        return doc;
    }

    public void setOnLongClickSelectedItemPatients(int theOnLongClickSelectedItemPatient)
    {
        this.theOnLongClickSelectedItemPatient=theOnLongClickSelectedItemPatient;
    }
    public int getOnLongClickSelectedItemPatients(){
        return this.theOnLongClickSelectedItemPatient;
    }

    public  static final String TAG = MainActivity.class.getSimpleName();

    public static final int UPDATE_PATIENT = 0;
    public static final int DELETE_PATIENT = 1;



    protected DialogInterface.OnClickListener mDialogInterface = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:JSONArray array=null;
                    try {
                        array=new JSONArray(getIntent().getStringExtra("listArrayPatientsOfDoctor"));

                        JSONObject theWhichPatient=(JSONObject)array.get(getOnLongClickSelectedItemPatients());

                        // richiamo la patientform
                            Intent intent = new Intent(MainActivity.this,PatientFormActivity.class);
                        intent.putExtra("theClickedPatient",theWhichPatient.toString());
                        intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:JSONArray array1=null;
                    // qui devo lancare un async task
                    // che mi elimina l'elemento richiesto ed mi riporta nel mainactivity
                    try {
                        array1=new JSONArray(getIntent().getStringExtra("listArrayPatientsOfDoctor"));


                        JSONObject theWhichPatient2=(JSONObject)array1.get(getOnLongClickSelectedItemPatients());

                        getIntent().putExtra("theClickedPatient",theWhichPatient2.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new DeleteAPatient().execute();
                    break;
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);


       // if(getIntent().getStringExtra("Doc")!=null){

        if(preferences.contains("Doc")){


            String target=preferences.getString("Doc","");
            getIntent().putExtra("Doc",target);

        JSONObject j = null;
        int id=0;
        try {
            j = new JSONObject(target);
            id=(Integer)j.get("id");
            setJSONOBJECT(j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.docId = id;


               // new ProcessPatientList().execute();
            NetAsync();
            PatientsListViewAdapter = (ListView) findViewById(R.id.patient_cell_item);


            //long tap
            PatientsListViewAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub
                    //faccio lo set del id del patiente
                    setOnLongClickSelectedItemPatients(pos);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setItems(R.array.patient_choices,mDialogInterface);
                    AlertDialog dialog = builder.create();
                    dialog.show();
//                    Log.v("long clicked", "pos: " + pos);

                    return true;
                }
            });
            PatientsListViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                    Intent intent = new Intent(MainActivity.this,VisitListActivity.class);
                    //intent.putExtra("theClickedPatient",theWhichPatient.toString());

                    JSONArray array=null;
                    JSONObject theWhichPatient=null;
                    try {
                        array=new JSONArray(getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                        theWhichPatient=(JSONObject)array.get(pos);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONArray array1= null;
                    JSONObject thePatientId=null;
                    String thePat=null;
                    String position=null;
                    try {
                        array1 = new JSONArray(getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                        thePatientId=(JSONObject)array1.get(pos);
                        thePat=thePatientId.getString("id");
                        position=String.valueOf(pos);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                    intent.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                    intent.putExtra("PatientId",thePat);
                    intent.putExtra("PatientStock",thePatientId.toString());
                    intent.putExtra("theClickedPatient",theWhichPatient.toString());

                    intent.putExtra("Position",position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                   // Log.v("long clicked", "pos: " + pos);

                }
            });
        }
        else
            navigateToLogin();
        //   }



    }



    @Override
    public void onResume () {
        super.onResume();
        Toast.makeText(this, "Resume MED", Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // if (id == R.id.action_settings) {
        //     return true;
        // }




        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();



        switch(itemId){

            case R.id.action_logout_on_main:
                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);

                preferences.unregisterOnSharedPreferenceChangeListener(
                        new SharedPreferences.OnSharedPreferenceChangeListener() {
                            @Override
                            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                            }
                        }
                );
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            case  R.id.action_refresh_on_main:
                break;
            case R.id.action_new_on_main:
              //  Intent intent2 = new Intent(MainActivity.this,PatientFormActivity.class);
                //Intent intent2 = new Intent(this,PatientFormActivity.class);

                //       String strjson=jsonArray.toString();

                //        intent2.putExtra("JsonArrString",strjson);
                // startActivityForResult(intent2, 0);
                //         intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //         intent2.addFlags(intent2.FLAG_ACTIVITY_CLEAR_TASK);
               // startActivity(intent2);
               // navigateToLogin();
                Intent intent2 = new Intent(this,PatientFormActivity.class);
                intent2.putExtra("Doc",getIntent().getStringExtra("Doc"));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    public void setListAdapter(JSONArray retval){

        JSONObject tmp=null;
        JSONObject pictures=null;
        JSONObject personalinfoId=null;

        List<Patient> patients=new ArrayList<Patient>();
        Patient[] tmpPatient=new Patient[retval.length()];
        String name;
        String image;
        String tel="cell: ";

        for(int i=0;i<retval.length();i++){
            try {
                tmp = retval.getJSONObject(i);
                personalinfoId=(JSONObject)tmp.get("personalInfoId");
                name=(String)personalinfoId.get("name");

                if(personalinfoId.isNull("cellphone"))
                    tel="";

                else
                    tel=(String)personalinfoId.get("cellphone");


                tmpPatient[i]= new Patient();
                tmpPatient[i].setName(name);
                tmpPatient[i].setTel(tel);
                pictures=(JSONObject)tmp.get("pictureId");
                image=(String)pictures.get("filename");
                tmpPatient[i].setImage(image);

                patients.add(tmpPatient[i]);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PatientsListViewAdapter.setAdapter(new PatientListAdapter(patients, this));





    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    private class ProcessPatientList extends AsyncTask<String,String,JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {

                    JSONArray retval=null;

                     retval=new ApiConnectorPatients().GetPatientList(docId);




                     return retval;

        }
        @Override
        protected void onPostExecute(JSONArray patients){




                jsonArray=patients;
                getIntent().putExtra("listArrayPatientsOfDoctor",jsonArray.toString());
                setListAdapter(patients);


        }
    }

    private class DeleteAPatient extends AsyncTask<String,String,JSONObject> {

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

            JSONArray updatedArray=null;
            try {
                updatedArray=new JSONArray(getIntent().getStringExtra("listArrayPatientsOfDoctor"));

                // qui devo rimuovere questa stringa
                updatedArray.remove(getOnLongClickSelectedItemPatients());

                getIntent().putExtra("listArrayPatientsOfDoctor",updatedArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setListAdapter(updatedArray);


        }
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(MainActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
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
                new ProcessPatientList().execute();
            }
            else{
                //nDialog.dismiss();
                nDialog.setMessage("The server is down. Sorry!");
                nDialog.setIndeterminate(true);


            }
        }
    }

    public void NetAsync(){
        new NetCheck().execute();
    }


}
