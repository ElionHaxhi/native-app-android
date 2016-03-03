package com.pranvera.root.pranvera.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorPatients;
import com.pranvera.root.pranvera.utils.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientFormActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    //private  String urlForImageServer = "http://192.168.56.1:8080/pranveraimages/patients/";
    private  String urlForImageServer = "http://spring-pranvera.rhcloud.com/pranveraimages/patients/";



    protected Button mCancelButton;

    protected Button mSalvaButtonOnPatient;

    public static final int PICK_PHOTO_REQUEST =1;

    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int TAKE_PHOTO_REQUEST =3;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10mb

    protected Uri mMediaUri;


    private EditText name;
    private EditText surname;
    private EditText address;
    private EditText email;
    private EditText celulare;
    private EditText telefono;

    private ImageView imagine;

    private ImageButton imageButton;

    private String retvalDoctor;

    JSONObject thePatientOnUpdate= null;
    JSONObject tmp=null;
    JSONObject imageObject=null;


    protected DialogInterface.OnClickListener mDialogInterface = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if(mMediaUri == null){
                        Toast.makeText(PatientFormActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
                    }
                    else{
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }

                    break;
                case 1:

                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        //Intent intentforPatient = new Intent(PatientFormActivity.this,MainActivity.class);



        this.name = (EditText) this.findViewById(R.id.namePatient);
        this.surname = (EditText) this.findViewById(R.id.surnamePatient);
        this.address = (EditText) this.findViewById(R.id.addresPatient);
        this.email = (EditText) this.findViewById(R.id.emailPatient);

        this.celulare = (EditText) this.findViewById(R.id.cellularPatient);
        this.telefono = (EditText) this.findViewById(R.id.telPatient);

        imagine = (ImageView)this.findViewById(R.id.edit_image_of_patient);

        imageButton = (ImageButton)this.findViewById(R.id.editFotoPatient);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // li devi passare il Dialog listener
               // setOnLongClickSelectedItemPatients(pos);

                AlertDialog.Builder builder = new AlertDialog.Builder(PatientFormActivity.this);
                builder.setItems(R.array.camera_choices,mDialogInterface);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        String theurlofimage=null;


        if(getIntent().getStringExtra("theClickedPatient")!=null)
        {
            JSONObject tempObject=null;
            JSONObject tempPicturesIdMenouno=null;

            try {

                tempObject=new JSONObject(getIntent().getStringExtra("theClickedPatient"));
                if(tempObject.getInt("id")==-1){
                    tempPicturesIdMenouno = new JSONObject(getIntent().getStringExtra("pictureId"));

                    getIntent().putExtra("theClickedPatient", generateAPatientStruct(getIntent().getStringExtra("Doc"),
                            getIntent().getStringExtra("name"),
                            getIntent().getStringExtra("surname"),
                            getIntent().getStringExtra("address"),
                            getIntent().getStringExtra("email"),
                            getIntent().getStringExtra("cellphone"),
                            getIntent().getStringExtra("telephone"),
                            tempPicturesIdMenouno));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else
        {
            //String name,String surname,String address, String email,String cellphone,String telephone
            JSONObject tempPicturesId=null;

            try {
                tempPicturesId=new JSONObject();
                tempPicturesId.putOpt("id",1);
                tempPicturesId.put("filename","default.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getIntent().putExtra("theClickedPatient", generateAPatientStruct(getIntent().getStringExtra("Doc"),
                    getIntent().getStringExtra("name"),
                    getIntent().getStringExtra("surname"),
                    getIntent().getStringExtra("address"),
                    getIntent().getStringExtra("email"),
                    getIntent().getStringExtra("cellphone"),
                    getIntent().getStringExtra("telephone"),
                    tempPicturesId));
        }




        //per i due casi aggiorna i valori del layout





        try {
            thePatientOnUpdate = new JSONObject(getIntent().getStringExtra("theClickedPatient").toString());
            imageObject=thePatientOnUpdate.getJSONObject("pictureId");

            theurlofimage=urlForImageServer+imageObject.getString("filename");

            if(new Integer(thePatientOnUpdate.get("id").toString())!=-1){


                tmp=(JSONObject)thePatientOnUpdate.get("personalInfoId");
                if(!tmp.isNull("name"))
                    name.setText(tmp.getString("name"));
                if(!tmp.isNull("surname"))
                    surname.setText(tmp.getString("surname"));

                if(!tmp.isNull("address"))
                    address.setText(tmp.getString("address"));

                if(!tmp.isNull("email"))
                    email.setText(tmp.getString("email"));

                if(!tmp.isNull("cellphone"))
                    celulare.setText(tmp.getString("cellphone"));

                if(!tmp.isNull("telephone"))
                    telefono.setText(tmp.getString("telephone"));

                if(imageObject.getInt("id")!=1){
                    imageButton.setVisibility(View.INVISIBLE);
                }
            }
            else{

                tmp=(JSONObject)thePatientOnUpdate.get("personalInfoId");
                if(!tmp.isNull("name"))
                    name.setText(tmp.getString("name"));
                if(!tmp.isNull("surname"))
                    surname.setText(tmp.getString("surname"));

                if(!tmp.isNull("address"))
                    address.setText(tmp.getString("address"));

                if(!tmp.isNull("email"))
                    email.setText(tmp.getString("email"));

                if(!tmp.isNull("cellphone"))
                    celulare.setText(tmp.getString("cellphone"));

                if(!tmp.isNull("telephone"))
                    telefono.setText(tmp.getString("telephone"));

                JSONObject verifyPicturesId=null;

                verifyPicturesId=thePatientOnUpdate.getJSONObject("pictureId");
                if(verifyPicturesId.getInt("id")!=1)
                    imageButton.setVisibility(View.INVISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // devo generare un nuovo patient
        // oviamente con devi id -1 e
        // pero devo fare attenzione a generare the doctor list che sono i datti del dottore


        //lasync task deveessere per i due casi di image
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
                            imagine.setImageBitmap(bitmap);
                        }
                    }.execute(theurlofimage);

    }

    public String generateAPatientStruct(String theDoctorObject,String name,String surname,String address, String email,String cellphone,String telephone,JSONObject tempPicturesId){
        // devi prendere the doctorlist dal dottore
        // e po lo devi aggiungere sul nuovo paxiente come doctorlist
        JSONObject personalInfoId = null;
        JSONObject picturesId=null;
        JSONObject patient=null;
        JSONArray doctorList=null;


        try {
            personalInfoId=new JSONObject();
            personalInfoId.put("id",-1);
            personalInfoId.put("name",name);
            personalInfoId.put("surname",surname);
            personalInfoId.put("address",address);
            personalInfoId.put("email",email);
            personalInfoId.put("cellphone",cellphone);
            personalInfoId.put("telephone",telephone);

            picturesId=tempPicturesId;


            patient=new JSONObject();

            doctorList= new JSONArray();
            doctorList.put(new JSONObject(theDoctorObject));

            patient.putOpt("id", new Integer(-1));
            patient.put("date",new Date().getTime());
            patient.put("trash",false);
            patient.put("trashDate",null);
            patient.put("doctorList",doctorList);
            patient.put("personalInfoId",personalInfoId);
            patient.put("pictureId",picturesId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return patient.toString();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch(id){


            case R.id.action_save_button_patient:
                new PersistAPatient().execute();
                break;
            case R.id.action_cancel_patient_button:
                Intent intent2 = new Intent(PatientFormActivity.this,MainActivity.class);
                //  String theString=getIntent().getStringExtra("JsonArrString");
                //  if(theString!=null)
                intent2.putExtra("Doc",getIntent().getStringExtra("Doc"));

                // startActivityForResult(intent2, 0);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.addFlags(intent2.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private Uri getOutputMediaFileUri(int mediaType) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if(isExternalStorageAvailable()){
            // get the URI

            //1. Get the external storage dir
            String appName = PatientFormActivity.this.getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);

            //2. Create out subdirectory
            if(! mediaStorageDir.exists()){
                if(! mediaStorageDir.mkdirs()){
                    Log.e(TAG, "Failed to create directory.");
                    return null;
                }
            }
            //3. Create a file name

            //4. Create the file
            File mediaFile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmms", Locale.ITALIAN).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if(mediaType == MEDIA_TYPE_IMAGE){
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            else{
                return null;
            }
            Log.d(TAG,"File: " + Uri.fromFile(mediaFile));
            //5. Return the file's URI

            return Uri.fromFile(mediaFile);
        }
        else{
            return null;
        }

    }

    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        else{
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == PICK_PHOTO_REQUEST){
                if(data == null){
                    Toast.makeText(this, "error data null", Toast.LENGTH_LONG).show();
                }
                else{
                    mMediaUri = data.getData();
                }
                Log.i(TAG, "Media URI: " + mMediaUri);
            }
            else{
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }
            getIntent().setData(mMediaUri);
            getIntent().putExtra("filetype","image");
            new UploadAFile().execute();

            String theurlofimagesecond=null;

            try {
                theurlofimagesecond=urlForImageServer+imageObject.getString("filename");
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
                    imagine.setImageBitmap(bitmap);
                }
            }.execute(theurlofimagesecond);


        }
    }

    private class UploadAFile extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;

            byte[] fileBytes = FileHelper.getByteArrayFromFile(PatientFormActivity.this, mMediaUri);

            String fileName = FileHelper.getFileName(PatientFormActivity.this, mMediaUri,"filetype");

            try {
                retval= new ApiConnectorPatients().uploadPictureToServer(fileBytes,fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject pictures){

            //qui devi settare l'id del jsonobject picturesId
            getIntent().putExtra("Picture",pictures.toString());
            JSONObject theClickedPatient=null;
            imageObject=pictures;
            try {
                theClickedPatient = new JSONObject(getIntent().getStringExtra("theClickedPatient").toString());
                theClickedPatient.put("pictureId", pictures);

                Intent intent = getIntent();
                finish();

                intent.putExtra("theClickedPatient",theClickedPatient.toString());
                intent.putExtra("Doc",getIntent().getStringExtra("Doc"));

                intent.putExtra("name",name.getText().toString());
                intent.putExtra("surname",surname.getText().toString());
                intent.putExtra("address",address.getText().toString());
                intent.putExtra("email",email.getText().toString());
                intent.putExtra("cellphone",celulare.getText().toString());
                intent.putExtra("telephone",telefono.getText().toString());
                intent.putExtra("pictureId",pictures.toString());



                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // qui devi fare un reload del patient
               // imagine
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //getIntent().putExtra("Doc",getIntent().getStringExtra("Doc"));
        }
    }


    private class PersistAPatient extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;
            String theDoctorList = null;
            String thePatientToUpdate=getIntent().getStringExtra("theClickedPatient");
            JSONObject theClickedPatient=null;
            JSONArray doctorList = null;
            try {
                theClickedPatient= new JSONObject(thePatientToUpdate);
                doctorList= (JSONArray) theClickedPatient.get("doctorList");
                //theDoctorList=doctorList.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject picturesId=null;
            if(theClickedPatient.isNull("pictureId")){
            picturesId=new JSONObject();
            try {
                picturesId.put("id",1);
                picturesId.put("filename","default.png");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
            else{
                try {
                    picturesId=theClickedPatient.getJSONObject("pictureId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            retval=new ApiConnectorPatients().GetPatientResponse(name.getText().toString(), surname.getText().toString(), address.getText().toString(), email.getText().toString(), celulare.getText().toString(), telefono.getText().toString(),doctorList,thePatientToUpdate,picturesId);
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject job){
            String str=job.toString();

            Intent intent = new Intent(PatientFormActivity.this, MainActivity.class);
            intent.putExtra("Doc",getIntent().getStringExtra("Doc"));

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
