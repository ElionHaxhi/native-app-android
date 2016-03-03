package com.pranvera.root.pranvera.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.adapter.SectionsPagerAdapter;
import com.pranvera.root.pranvera.apiconnector.ApiConnectorVisits;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VisitFormActivity extends FragmentActivity implements ActionBar.TabListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link } derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    String goalExam=null;


    private static final String ARG_SECTION_NUMBER = "section_number";

   // private static  String urlForImageServer = "http://192.168.56.1:8080/pranveraimages/visits/";
   // private static final String baseUrlForImage = "http://192.168.56.1:8080/pranveraimages/visits/";



    JSONArray visitjsonarr=null;
    JSONObject thevisitjsonobject=null;
    Integer pos=null;






    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    TextView goal_exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_form);




        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(VisitFormActivity.this,getSupportFragmentManager(),getIntent().getStringExtra("TheVisit"));
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);





        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);


            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.visit_form, menu);
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

                Intent intent = new Intent(VisitFormActivity.this,VisitListActivity.class);
                intent.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
                intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
                intent.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                intent.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));
                intent.putExtra("listArrayVisitsOfPatient",getIntent().getStringExtra("listArrayVisitsOfPatient"));
                // devo aggiornare
                intent.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                intent.putExtra("Position",getIntent().getStringExtra("Position"));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            case  R.id.action_edit_button:


                Intent intentedit = new Intent(VisitFormActivity.this,EditVisitFormActivity.class);



                intentedit.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
                intentedit.putExtra("Doc",getIntent().getStringExtra("Doc"));
                intentedit.putExtra("Pos",getIntent().getStringExtra("Pos"));
                intentedit.putExtra("listArrayVisitsOfPatient",getIntent().getStringExtra("listArrayVisitsOfPatient"));
                intentedit.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
                intentedit.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
                intentedit.putExtra("Position",getIntent().getStringExtra("Position"));

                intentedit.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));


                try {
                        visitjsonarr =new JSONArray(getIntent().getStringExtra("listArrayVisitsOfPatient"));


                    if(getIntent().getStringExtra("Pos")!=null)
                    {
                        pos=new Integer(String.valueOf(getIntent().getStringExtra("Pos")));


                        thevisitjsonobject=(JSONObject)visitjsonarr.get(pos);
                        intentedit.putExtra("TheVisit",thevisitjsonobject.toString());
                    }
                    else
                    {
                        intentedit.putExtra("TheVisit",getIntent().getStringExtra("TheVisit"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(intentedit);



                break;
            case R.id.action_discard_button:

                new DeleteAVisit().execute();

                break;

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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());



    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int pos=tab.getPosition();


    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    private class DeleteAVisit extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject retval=null;
            JSONObject kyPatient=null;
            Integer visId=null;
            JSONObject tmp=null;


            try {

                tmp=new JSONObject(getIntent().getStringExtra("TheVisit").toString());
                visId=tmp.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            retval=new ApiConnectorVisits().DeleteAVisit(visId);



            return retval;
        }
        @Override
        protected void onPostExecute(JSONObject patient){

            Intent intent = new Intent(VisitFormActivity.this,VisitListActivity.class);
            intent.putExtra("PatientId",getIntent().getStringExtra("PatientId"));
            intent.putExtra("Doc",getIntent().getStringExtra("Doc"));
            intent.putExtra("Pos",getIntent().getStringExtra("Pos"));
            intent.putExtra("listArrayPatientsOfDoctor",getIntent().getStringExtra("listArrayPatientsOfDoctor"));
            intent.putExtra("PatientStock",getIntent().getStringExtra("PatientStock"));



            intent.putExtra("theClickedPatient",getIntent().getStringExtra("theClickedPatient"));
            intent.putExtra("Position",getIntent().getStringExtra("Position"));



            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
    }





}
