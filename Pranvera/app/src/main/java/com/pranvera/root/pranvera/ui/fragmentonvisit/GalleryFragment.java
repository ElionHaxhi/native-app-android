package com.pranvera.root.pranvera.ui.fragmentonvisit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pranvera.root.pranvera.R;
import com.pranvera.root.pranvera.adapter.GridAdapter;
import com.pranvera.root.pranvera.model.VisitGalleryCell;
import com.pranvera.root.pranvera.ui.VisitPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/01/15.
 */
public class GalleryFragment extends Fragment {

    public static final String TAG=GalleryFragment.class.getSimpleName();
    protected GridView mGridView;
    protected List<VisitGalleryCell> mCella;
    Bundle bundle=null;
    protected AbsListView listView;
    List<VisitGalleryCell> retval=new ArrayList<VisitGalleryCell>();
    DisplayImageOptions options;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallerygrid, container, false);
        listView = (GridView) rootView.findViewById(R.id.fragmentListView);


        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTextView);
        bundle=this.getArguments();

        bundle.getString("TheVisit");
        JSONObject jsonObject;
        JSONArray subarray;
        JSONArray subsubarray;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //qui devo lanciare un activity


                Intent intent = new Intent(getActivity(), VisitPagerActivity.class);
                String theName=retval.get(position).getImage();
                intent.putExtra("NameOfFile", theName);
                startActivity(intent);
            }
        });


        try {
            jsonObject=new JSONObject(bundle.getString("TheVisit").toString());
            subarray=jsonObject.getJSONArray("detailList");

            JSONObject jsonpictures=null;
            JSONObject tmp=null;
            for(int i=0;i<subarray.length();i++){
                tmp=subarray.getJSONObject(i);
                subsubarray=tmp.getJSONArray("pictureList");

                for (int j=0;j<subsubarray.length();j++){

                    jsonpictures=subsubarray.getJSONObject(j);



                    retval.add(new VisitGalleryCell(jsonpictures.getString("filename")));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(listView.getAdapter() == null){




            GridAdapter adapter = new GridAdapter(getActivity(),retval);
            listView.setAdapter(adapter);
        }
        else{
            ((GridAdapter)listView.getAdapter()).refill(retval);
        }

        return rootView;
    }


}
