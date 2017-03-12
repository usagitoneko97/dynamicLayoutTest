package usagitoneko.volleytest;

import android.app.DownloadManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    Gson mGson;
   // GsonResponse mgsonResponse;
    JSONObject mJSONObject;
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://raw.githubusercontent.com/usagitoneko97/Stm32-and-nfc02A1-led-control/finalOfLayout/jsonDummy.json";
        //mTextView.setText("Result: "+ mJSONObject.toString());
        //View sampleView = DynamicView.createView(this, mJSONObject, SampleViewHolder.class);
        //sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        //setContentView(sampleView);
        getJsonObject(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
    }

    public void getJsonObject ( String url ,final VolleyCallback callback){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                        //mTextView.setText("Response: " + response.toString());
                        mGson = new Gson();
                        //mgsonResponse = mGson.fromJson(response.toString(),GsonResponse.class);
                        //mTextView.setText(mgsonResponse.getGlossary().getGlossDiv().getGlossList().getGlossEntry().getID());
                        View sampleView = DynamicView.createView(MainActivity.this, response, ViewIds.SampleViewHolder.class);
                        sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                        setContentView(sampleView);
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


}
