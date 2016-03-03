package com.pranvera.root.pranvera.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.utils.TouchImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class VisitPagerActivity extends Activity {

//    private static final String baseUrlForImage = "http://192.168.56.1:8080/docmanimages/visits/";
    private static final String baseUrlForImage = "http://spring-pranvera.rhcloud.com/docmanimages/visits/";



    private DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_image);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        String retval=getIntent().getStringExtra("NameOfFile");
        String url=baseUrlForImage+retval;
        final TouchImageView imageView = (TouchImageView)findViewById(R.id.imagePagerView);

        Picasso.with(this)
                .load(url)
                .into(imageView);


        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {

            @Override
            public void onMove() {
                PointF point = imageView.getScrollPosition();
                RectF rect = imageView.getZoomedRect();
                float currentZoom = imageView.getCurrentZoom();
                boolean isZoomed = imageView.isZoomed();

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.visit_pager, menu);
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
}
