package usagitoneko.volleytest;

import android.app.DownloadManager;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import json2view.DynamicView;
import json2view.DynamicViewId;

public class MainActivity extends AppCompatActivity {
    private View led2;
    private View ledBlue;
    private View ledGreen;
    private View ledOrange;
    private JSONObject mResult;
    private View sampleView;
    private Bundle viewBundle;
    SimpleFragmentPagerAdapter pageAdapter;
    private ViewPager pager;
    Gson mGson;
   // GsonResponse mgsonResponse;
    JSONObject mJSONObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://raw.githubusercontent.com/usagitoneko97/dynamicLayoutTest/master/4buttonjson.json";
        //mTextView.setText("Result: "+ mJSONObject.toString());
        //View sampleView = DynamicView.createView(this, mJSONObject, SampleViewHolder.class);
        //sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        //setContentView(sampleView);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        mGson = new Gson();
                        //mgsonResponse = mGson.fromJson(response.toString(),GsonResponse.class);
                        /*View sampleView = DynamicView.createView(MainActivity.this, response, ViewIds.SampleViewHolder.class);
                        sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                        setContentView(sampleView);*/
                        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        viewBundle = new Bundle();
                        viewBundle.putString("viewString", response.toString());
                        pageAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), viewBundle);
                        pager = (ViewPager)findViewById(R.id.pager);
                        pager.setAdapter(pageAdapter);

                        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(pager);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText("ERROR!!!!");
                        // TODO Auto-generated method stub

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }


    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private Bundle fragmentBundle;

        public SimpleFragmentPagerAdapter (FragmentManager fm, Bundle data){
            super(fm);
            fragmentBundle = data;
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
                final remoteDisplayLayout mainFragment = new remoteDisplayLayout();
                mainFragment.setArguments(this.fragmentBundle);
                return (mainFragment);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "LOG";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}
