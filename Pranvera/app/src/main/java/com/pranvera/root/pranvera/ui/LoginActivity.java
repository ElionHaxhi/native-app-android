package com.pranvera.root.pranvera.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorPatients;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    protected Button mLoginButton;
    protected TextView mSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        ActionBar actionBar = getActionBar();
        actionBar.hide();


       /**  try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }*/


        if(getIntent().getStringExtra("Doc")!=null){
            Intent intent2 = new Intent(this,MainActivity.class);
            intent2.putExtra("Doc",getIntent().getStringExtra("Doc"));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent2);
            finish();
        }else{
            this.username = (EditText) this.findViewById(R.id.usernameField);
            this.password = (EditText) this.findViewById(R.id.passwordField);


            mSignUp = (TextView) findViewById(R.id.signUpText);
            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this,DoctorFormActivity.class);
                    startActivity(intent);
                }
            });

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !username.getText().toString().equals("")) && ( !password.getText().toString().equals("")) )
                {
                    NetAsync(view);
                }
                else if ( ( !password.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_LONG).show();
                }
                else if ( ( !username.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // qui devo creare l'async task per post data
    private class ProcessLogin extends AsyncTask<String,String,JSONObject> {

        private ProgressDialog progressDialog=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Contacting Servers");
            progressDialog.setMessage("Logging in ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;
            //  retval= new ModelJsonObject().getDataFromTheNet();
            retval=new ApiConnectorPatients().GetLoginResponse(username.getText().toString(),password.getText().toString());
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject job){
            //  Intent i = new Intent(FromActivity.this, ToActivity.class);
            //  i.putExtra("MyObjectAsString", target);


            try {
                if(job.getInt("id")!=0){

                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Doc",job.toString());




                editor.apply();

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                String str=job.toString();
                //intent.putExtra("Doc",str);
                intent.putExtra("Doc",str);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                    progressDialog.dismiss();
                startActivity(intent);

                }
                else
                {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(),
                            "Incorrect username/password", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
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
                    //URL url = new URL("http://192.168.56.1:8080/pranvera");
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
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();

                Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

}
