package usagitoneko.volleytest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import json2view.DynamicView;


public class remoteDisplayLayout extends Fragment {

    public remoteDisplayLayout(){};
    public interface VolleyCallback{
        void onSuccess(JSONObject result, View sampleView);
    }

    VolleyCallback volleyCallback;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "https://raw.githubusercontent.com/usagitoneko97/dynamicLayoutTest/master/4buttonjson.json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        //mgsonResponse = mGson.fromJson(response.toString(),GsonResponse.class);
                        View sampleView = DynamicView.createView(getActivity(), response, ViewIds.SampleViewHolder.class);
                        sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                        volleyCallback.onSuccess(response, sampleView);
                        //setContentView(sampleView);
                        //callback.onSuccess(response, sampleView);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText("ERROR!!!!");
                        // TODO Auto-generated method stub

                    }
                });
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if(context instanceof Activity){
            a = (Activity) context;
        }
        else{
            a= null;
        }
        try{
            volleyCallback = (VolleyCallback) a;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement onSomeEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        volleyCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_display_layout, container, false);
        return view;
    }
}
