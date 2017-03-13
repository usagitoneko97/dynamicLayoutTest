package usagitoneko.volleytest;

import android.app.DownloadManager;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import json2view.DynamicView;
import json2view.DynamicViewId;

public class MainActivity extends AppCompatActivity implements remoteDisplayLayout.VolleyCallback  {
    private View led2;
    private View ledBlue;
    private View ledGreen;
    private View ledOrange;
    private JSONObject mResult;
    private View sampleView;
    Gson mGson;
   // GsonResponse mgsonResponse;
    JSONObject mJSONObject;

    @Override
    public void onSuccess(JSONObject result, View sampleView) {
        this.mResult = result;
        this.sampleView = sampleView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fManager = getSupportFragmentManager();
        Fragment frag = fManager.findFragmentById(R.id.remoteLayout);
        if(frag == null){
            frag = new remoteDisplayLayout();
            fManager.beginTransaction().add(R.id.remoteLayout, frag).commit();
        }
        //setContentView(sampleView);
        //setContentView(R.layout.activity_main);
        //RequestQueue queue = Volley.newRequestQueue(this);
       // String url = "https://raw.githubusercontent.com/usagitoneko97/dynamicLayoutTest/master/4buttonjson.json";
        //mTextView.setText("Result: "+ mJSONObject.toString());
        //View sampleView = DynamicView.createView(this, mJSONObject, SampleViewHolder.class);
        //sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        //setContentView(sampleView);

    }



}
