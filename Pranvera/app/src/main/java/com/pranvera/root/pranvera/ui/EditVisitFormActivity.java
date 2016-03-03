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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorVisits;
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

public class EditVisitFormActivity extends Activity {


//    private  String urlForImageServer = "http://192.168.56.1:8080/pranveraimages/visits/";
    private  String urlForImageServer = "http://spring-pranvera.rhcloud.com/pranveraimages/visits/";




    public static final String TAG = EditVisitFormActivity.class.getSimpleName();


    public static final int PICK_PHOTO_REQUEST =1;

    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int TAKE_PHOTO_REQUEST =3;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10mb

    protected Uri mMediaUri;

    static ImageView imageView;

    static TextView goal_exam_desc;
    static EditText goal_exam_editText;


    static TextView terapia;
    static EditText teraphy_editText;


    static TextView diagnostic;
    static EditText diagnostic_editText;


    static TextView anamnesi;
    static EditText anamnesi_editText;


    static JSONObject laVisita=null;

    static JSONArray theDescriptionList=null;


    Intent mIntent=null;

    private ImageView imagineVisit=null;

    JSONObject imageObject=null;


    private ImageButton imageButton=null;

    protected DialogInterface.OnClickListener mDialogInterface = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if(mMediaUri == null){
                        Toast.makeText(EditVisitFormActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
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

    private Uri getOutputMediaFileUri(int mediaType) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if(isExternalStorageAvailable()){
            // get the URI

            //1. Get the external storage dir
            String appName = EditVisitFormActivity.this.getString(R.string.app_name);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_visit_form);

            JSONObject tmp=null;
            JSONObject subtmp=null;
            JSONObject imagevisit=null;
            String theurlofimage=null;



            Integer theId=null;


        imageView = (ImageView) this.findViewById(R.id.edit_image_of_visit);

        goal_exam_desc=(TextView)this.findViewById(R.id.textview_dettagli);
        goal_exam_editText = (EditText) this.findViewById(R.id.edittext_goalexam_retval);
        terapia = (TextView) this.findViewById(R.id.textview_terapy);
        teraphy_editText = (EditText) this.findViewById(R.id.edittext_terapy_retval);
        diagnostic = (TextView) this.findViewById(R.id.textview_diagnostic);
        diagnostic_editText = (EditText) this.findViewById(R.id.edit_diagnostic_retval);
        anamnesi = (TextView) this.findViewById(R.id.textview_anamnesi);
        anamnesi_editText = (EditText) this.findViewById(R.id.edittext_anamnesi_retval);

        imagineVisit = (ImageView)this.findViewById(R.id.edit_image_of_visit);


        imageButton = (ImageButton)this.findViewById(R.id.editFotoVisit);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // li devi passare il Dialog listener
                // setOnLongClickSelectedItemPatients(pos);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditVisitFormActivity.this);
                builder.setItems(R.array.camera_choices,mDialogInterface);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


















            try {

                //laVisita =new JSONObject(bundle.getString("TheVisit"));


                if(getIntent().getStringExtra("TheVisit")!=null)
                {
                    JSONObject tempObject=null;
                    JSONObject tempPicturesIdMenouno=null;

                    laVisita =new JSONObject(getIntent().getStringExtra("TheVisit"));


                    try {

                        tempObject=new JSONObject(getIntent().getStringExtra("TheVisit"));
                        if(tempObject.getInt("id")==-1){
                            tempPicturesIdMenouno = new JSONObject(getIntent().getStringExtra("pictureId"));

                            getIntent().putExtra("TheVisit", generateTheJSONVisitPost(getIntent().getStringExtra("PatientStock"),
                                    getIntent().getStringExtra("anamnesi"),
                                    getIntent().getStringExtra("esameobiettivo"),
                                    getIntent().getStringExtra("terapia"),
                                    getIntent().getStringExtra("diagnosi")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    laVisita=new JSONObject(generateTheJSONVisitPost(getIntent().getStringExtra("PatientStock"),
                            getIntent().getStringExtra("anamnesi"),
                            getIntent().getStringExtra("esameobiettivo"),
                            getIntent().getStringExtra("terapia"),
                            getIntent().getStringExtra("diagnosi")));
                }




                imagevisit=(JSONObject)laVisita.get("pictureId");
                theId=laVisita.getInt("id");
                theurlofimage=urlForImageServer+imagevisit.getString("filename");



                imageObject=laVisita.getJSONObject("pictureId");



            } catch (JSONException e) {
                e.printStackTrace();
            }






                try {

                    if(laVisita.getInt("id")!=-1) {

                    theDescriptionList = (JSONArray) laVisita.get("descriptionList");

                    for (int i = 0; i < theDescriptionList.length(); i++) {

                        tmp = (JSONObject) theDescriptionList.get(i);
                        subtmp = (JSONObject) tmp.get("type");

                        if (subtmp.getInt("id") == 1) {
                            anamnesi.setText(subtmp.getString("name"));

                            if (!tmp.isNull("content"))
                                anamnesi_editText.setText(tmp.getString("content"));
                            else anamnesi_editText.setText("");

                        } else if (subtmp.getInt("id") == 2) {
                            goal_exam_desc.setText(subtmp.getString("name"));

                            if (!tmp.isNull("content"))
                                goal_exam_editText.setText(tmp.getString("content"));
                            else goal_exam_editText.setText("");

                        } else if (subtmp.getInt("id") == 3) {

                            terapia.setText(subtmp.getString("name"));

                            if (!tmp.isNull("content"))
                                teraphy_editText.setText(tmp.getString("content"));
                            else teraphy_editText.setText("");
                        } else if (subtmp.getInt("id") == 4) {
                            diagnostic.setText(subtmp.getString("name"));

                            if (!tmp.isNull("content"))
                                diagnostic_editText.setText(tmp.getString("content"));
                            else diagnostic_editText.setText("");

                        } else {
                            // qui ce un errore
                            Log.v("ERRORE NELL DESCRIPTIONLIST", "pos: ");
                        }



                    }//fine for

                        if(imageObject.getInt("id")!=1){
                            imageButton.setVisibility(View.INVISIBLE);
                        }
                    }//fine else
                    else
                    {


                            theDescriptionList = (JSONArray) laVisita.get("descriptionList");

                            for (int i = 0; i < theDescriptionList.length(); i++) {

                                tmp = (JSONObject) theDescriptionList.get(i);
                                subtmp = (JSONObject) tmp.get("type");

                                if (subtmp.getInt("id") == 1) {
                                    anamnesi.setText(subtmp.getString("name"));

                                    if (!tmp.isNull("content"))
                                        anamnesi_editText.setText(tmp.getString("content"));
                                    else anamnesi_editText.setText("");

                                } else if (subtmp.getInt("id") == 2) {
                                    goal_exam_desc.setText(subtmp.getString("name"));

                                    if (!tmp.isNull("content"))
                                        goal_exam_editText.setText(tmp.getString("content"));
                                    else goal_exam_editText.setText("");

                                } else if (subtmp.getInt("id") == 3) {

                                    terapia.setText(subtmp.getString("name"));

                                    if (!tmp.isNull("content"))
                                        teraphy_editText.setText(tmp.getString("content"));
                                    else teraphy_editText.setText("");
                                } else if (subtmp.getInt("id") == 4) {
                                    diagnostic.setText(subtmp.getString("name"));

                                    if (!tmp.isNull("content"))
                                        diagnostic_editText.setText(tmp.getString("content"));
                                    else diagnostic_editText.setText("");

                                } else {
                                    // qui ce un errore
                                    Log.v("ERRORE NELL DESCRIPTIONLIST", "pos: ");
                                }

                            }
                        //imagevisit=thePatientOnUpdate.getJSONObject("picturesId");



                        JSONObject verifyPicturesId=null;

                        verifyPicturesId=laVisita.getJSONObject("pictureId");
                        if(verifyPicturesId.getInt("id")!=2)
                            imageButton.setVisibility(View.INVISIBLE);


                    }//fine else


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
                    imagineVisit.setImageBitmap(bitmap);
                }
            }.execute(theurlofimagesecond);


        }
    }

    private class UploadAFile extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;

            byte[] fileBytes = FileHelper.getByteArrayFromFile(EditVisitFormActivity.this, mMediaUri);

            String fileName = FileHelper.getFileName(EditVisitFormActivity.this, mMediaUri,"filetype");

            try {
                retval= new ApiConnectorVisits().uploadPictureToServer(fileBytes,fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject pictures){

            //qui devi settare l'id del jsonobject picturesId
            getIntent().putExtra("Picture",pictures.toString());
            JSONObject theClickedVisit=null;
            imageObject=pictures;
            try {
                theClickedVisit = new JSONObject(getIntent().getStringExtra("TheVisit").toString());
                theClickedVisit.put("pictureId", pictures);

                Intent intent = getIntent();
                finish();

                intent.putExtra("TheVisit",theClickedVisit.toString());
                intent.putExtra("Doc",getIntent().getStringExtra("Doc"));

                intent.putExtra("anamnesi",anamnesi_editText.getText().toString());
                intent.putExtra("esameobiettivo",goal_exam_editText.getText().toString());
                intent.putExtra("terapia",teraphy_editText.getText().toString());
                intent.putExtra("diagnosi",diagnostic_editText.getText().toString());
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


    public String generateTheJSONVisitPost(String patientIdString,String mAnamnesi,String mEsameobietivo,String mTerapia,String mDiagnosi){

        // qui devo creare la strutura della visita nuova
        // non sevre piu la visita vechia me una nuova con un id visita -1 e -1 sarano anche
        // gli id nella description list
        JSONObject laNewVisita=new JSONObject();
        JSONArray descriptionList = new JSONArray();

        JSONObject anamnesi=new JSONObject();
        JSONObject typeAnamnesi=new JSONObject();

        JSONObject esameobietivo=new JSONObject();
        JSONObject typeEsameobietivo=new JSONObject();

        JSONObject terapia=new JSONObject();
        JSONObject typeTerapia=new JSONObject();

        JSONObject diagnosi=new JSONObject();
        JSONObject typeDiagnosi=new JSONObject();

        JSONObject picturesId = new JSONObject();

        JSONObject patientId=null;
        try {
            typeAnamnesi.putOpt("id",new Integer(1));
            typeAnamnesi.put("name", "Anamnesi");
            anamnesi.putOpt("id", new Integer(-1));
            anamnesi.put("content", mAnamnesi);
            anamnesi.put("type",typeAnamnesi);

            typeEsameobietivo.putOpt("id",new Integer(2));
            typeEsameobietivo.put("name", "Esame obiettivo");
            esameobietivo.putOpt("id", new Integer(-1));
            esameobietivo.put("content", mEsameobietivo);
            esameobietivo.put("type",typeEsameobietivo);


            typeTerapia.putOpt("id",new Integer(3));
            typeTerapia.put("name", "Terapia");
            terapia.putOpt("id", new Integer(-1));
            terapia.put("content", mTerapia);
            terapia.put("type",typeTerapia);


            typeDiagnosi.putOpt("id",new Integer(4));
            typeDiagnosi.put("name", "Diagnosi");
            diagnosi.putOpt("id", new Integer(-1));
            diagnosi.put("content", mDiagnosi);
            diagnosi.put("type",typeDiagnosi);


            descriptionList.put(anamnesi);
            descriptionList.put(esameobietivo);
            descriptionList.put(terapia);
            descriptionList.put(diagnosi);

            picturesId.putOpt("id", new Integer(1));
            picturesId.put("filename","default.jpg");

            patientId = new JSONObject(patientIdString);

            laNewVisita.putOpt("id",new Integer(-1));
            laNewVisita.put("creationDate",new Date().getTime());
            laNewVisita.put("lastmodDate",null);
            laNewVisita.put("trash",false);
            laNewVisita.put("trashDate",null);
            laNewVisita.put("detailList",null);
            laNewVisita.put("descriptionList",descriptionList);
            laNewVisita.put("pictureId",picturesId);
            laNewVisita.put("keywordId",null);
            laNewVisita.put("patientId",patientId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


       return laNewVisita.toString();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_visit_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch(id){

            case R.id.action_save_button:

                try {
                    if(laVisita.getInt("id")!=-1)
                    new PersistAVisitUpdate().execute();
                    else new PersistAVisitNew().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case  R.id.action_back_button:
                // devi implementare il back

                Intent intentback = new Intent(EditVisitFormActivity.this, VisitListActivity.class);

                intentback.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
                intentback.putExtra("Doc",getIntent().getStringExtra("Doc"));
                intentback.putExtra("Position",getIntent().getStringExtra("Position"));
                // intentedit.putExtra("Pos",getIntent().getStringExtra("Pos"));
                intentback.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                intentback.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));
                intentback.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                intentback.putExtra("listArrayVisitsOfPatient",getIntent().getStringExtra("listArrayVisitsOfPatient"));


                intentback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentback.addFlags(intentback.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentback);
                break;
        }






        return super.onOptionsItemSelected(item);
    }
    //qui ci va una classe asincrona per persistere il server
    private class PersistAVisitUpdate extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;
            String theDoctorList=getIntent().getStringExtra("Doc");
            String thePatientToUpdate=getIntent().getStringExtra("theClickedPatient");
            JSONObject picturesId=null;

            if(laVisita.isNull("pictureId")){
                picturesId=new JSONObject();
                try {
                    picturesId.put("id",2);
                    picturesId.put("filename","default.png");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    picturesId=laVisita.getJSONObject("pictureId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if(laVisita.getInt("id")!=-1){
                retval = new ApiConnectorVisits().GetUpdateVisitResponse(
                laVisita.getInt("id"),
                goal_exam_editText.getText().toString(),
                teraphy_editText.getText().toString(),
                diagnostic_editText.getText().toString(),
                anamnesi_editText.getText().toString(),
                laVisita.toString(),picturesId);
                }
                else{
                    retval = new ApiConnectorVisits().GetNewVisitResponse(
                            goal_exam_editText.getText().toString(),
                            teraphy_editText.getText().toString(),
                            diagnostic_editText.getText().toString(),
                            anamnesi_editText.getText().toString(),
                            laVisita.toString(),
                            picturesId);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //retval=new ApiConnectorPatients().GetPatientResponse(name.getText().toString(), surname.getText().toString(), address.getText().toString(), email.getText().toString(), celulare.getText().toString(), telefono.getText().toString(),theDoctorList.toString(),thePatientToUpdate,picturesId);
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject job){
            String strlavisitaaggiornata=job.toString();

            Intent intentedit = new Intent(EditVisitFormActivity.this, VisitListActivity.class);

            intentedit.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
            intentedit.putExtra("Doc",getIntent().getStringExtra("Doc"));
            intentedit.putExtra("Position",getIntent().getStringExtra("Position"));
            intentedit.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
            intentedit.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));
            intentedit.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));


            // devo creare un metodo per aggiornare la visita nuova
          //  String listArrayVisitsOfPatientUpdate=null;
          //  listArrayVisitsOfPatientUpdate=updateArrayVisitsOfPatient(strlavisitaaggiornata,getIntent().getStringExtra("listArrayVisitsOfPatient"));

            //String tmp=getIntent().getStringExtra("listArrayVisitsOfPatient").replace("listArrayVisitsOfPatient",listArrayVisitsOfPatientUpdate);

        //    intentedit.putExtra("listArrayVisitsOfPatient",listArrayVisitsOfPatientUpdate);

            intentedit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentedit.addFlags(intentedit.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentedit);
        }
    }

    public String updateArrayVisitsOfPatient(String theNewVisit,String listArrayVisitsOfPatient){
        JSONObject v=null;
        JSONObject tmp=null;
        JSONArray aarr=null;
        JSONArray descriptionList=null;


        try {
            v=new JSONObject(theNewVisit);
            aarr=new JSONArray(listArrayVisitsOfPatient);
            for(int i=0;i<aarr.length();i++){
                tmp=aarr.getJSONObject(i);
                if (v.getInt("id")==tmp.getInt("id")){

                    aarr.getJSONObject(i).put("descriptionList",v.get("descriptionList"));


                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aarr.toString();
    }

    private class PersistAVisitNew extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject retval=null;
            String theDoctorList=getIntent().getStringExtra("Doc");
            String thePatientToUpdate=getIntent().getStringExtra("theClickedPatient");
            JSONObject picturesId=null;




            if(laVisita.isNull("pictureId")){
                picturesId=new JSONObject();
                try {
                    picturesId.put("id",2);
                    picturesId.put("filename","default.png");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    picturesId=laVisita.getJSONObject("pictureId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if(laVisita.getInt("id")!=-1){
                    retval = new ApiConnectorVisits().GetUpdateVisitResponse(
                            laVisita.getInt("id"),
                            goal_exam_editText.getText().toString(),
                            teraphy_editText.getText().toString(),
                            diagnostic_editText.getText().toString(),
                            anamnesi_editText.getText().toString(),
                            laVisita.toString(),picturesId);
                }
                else{
                    retval = new ApiConnectorVisits().GetNewVisitResponse(
                            goal_exam_editText.getText().toString(),
                            teraphy_editText.getText().toString(),
                            diagnostic_editText.getText().toString(),
                            anamnesi_editText.getText().toString(),
                            laVisita.toString(),picturesId
                    );

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //retval=new ApiConnectorPatients().GetPatientResponse(name.getText().toString(), surname.getText().toString(), address.getText().toString(), email.getText().toString(), celulare.getText().toString(), telefono.getText().toString(),theDoctorList.toString(),thePatientToUpdate,picturesId);
            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject job){
            String strlavisitaaggiornata=job.toString();

            Intent intentedit = new Intent(EditVisitFormActivity.this,VisitListActivity.class);

            intentedit.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
            intentedit.putExtra("Doc",getIntent().getStringExtra("Doc"));
            intentedit.putExtra("Position",getIntent().getStringExtra("Position"));
            intentedit.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
            intentedit.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));
            intentedit.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));


            // devo creare un metodo per aggiornare la visita nuova
           // String listArrayVisitsOfPatientUpdate=null;
           // listArrayVisitsOfPatientUpdate=addANewVisitOnArrayVisitsOfPatient(strlavisitaaggiornata,getIntent().getStringExtra("listArrayVisitsOfPatient"));

           // intentedit.putExtra("listArrayVisitsOfPatient",listArrayVisitsOfPatientUpdate);


            intentedit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentedit.addFlags(intentedit.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentedit);
        }
    }

    //aggiorna l'array in locale aggiungendo un componente nuovo
    public String addANewVisitOnArrayVisitsOfPatient(String theNewVisit,String listArrayVisitsOfPatient){
        JSONObject v=null;
        JSONObject tmp=null;
        JSONArray aarr=null;
        JSONArray descriptionList=null;


        try {
            v=new JSONObject(theNewVisit);
            aarr=new JSONArray(listArrayVisitsOfPatient);
            aarr.put(v);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aarr.toString();
    }




}
