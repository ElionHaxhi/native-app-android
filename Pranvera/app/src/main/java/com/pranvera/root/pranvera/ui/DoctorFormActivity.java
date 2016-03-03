package com.pranvera.root.pranvera.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorPatients;

import org.json.JSONObject;

public class DoctorFormActivity extends Activity {

    protected Button mSalvaButtonOnDoctor;


    private EditText name;
    private EditText surname;
    private EditText address;
    private EditText email;
    private EditText celulare;
    private EditText telefono;
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        this.name = (EditText) this.findViewById(R.id.nameDoctor);
        this.surname = (EditText) this.findViewById(R.id.surnameDoctor);
        this.address = (EditText) this.findViewById(R.id.addresDoctor);
        this.email = (EditText) this.findViewById(R.id.emailDoctor);

        this.celulare = (EditText) this.findViewById(R.id.cellularDoctor);
        this.telefono = (EditText) this.findViewById(R.id.telDoctor);
        this.username = (EditText) this.findViewById(R.id.usernameDoctor);
        this.password = (EditText) this.findViewById(R.id.passwordDoctor);


        mSalvaButtonOnDoctor = (Button) findViewById(R.id.salvaButtonOnDoctor);
        mSalvaButtonOnDoctor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(intent);
                // devo eseguire l'async task
                new PersistADoctor().execute();

            }
        });

        //fare un asynctask per persistere il dotore
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(DoctorFormActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class PersistADoctor extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;
            //  retval= new ModelJsonObject().getDataFromTheNet();
            retval=new ApiConnectorPatients().GetDoctorResponse(name.getText().toString(), surname.getText().toString(), address.getText().toString(), email.getText().toString(), celulare.getText().toString(), telefono.getText().toString(), username.getText().toString(), password.getText().toString());
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject job){
            //  Intent i = new Intent(FromActivity.this, ToActivity.class);
            //  i.putExtra("MyObjectAsString", target);
            //Intent intent = new Intent(DoctorFormActivity.this,LoginActivity.class);
            String str=job.toString();
            //intent.putExtra("Doc",str);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("Doc",str);
            startActivity(intent);
        }
    }
}
