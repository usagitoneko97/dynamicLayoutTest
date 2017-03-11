package usagitoneko.volleytest;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Gson mGson;
    GsonResponse mgsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView mTextView = (TextView)findViewById(R.id.textView);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://raw.githubusercontent.com/usagitoneko97/Stm32-and-nfc02A1-led-control/finalOfLayout/jsonDummy.json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.setText("Response: " + response.toString());
                        mGson = new Gson();
                        mgsonResponse = mGson.fromJson(response.toString(),GsonResponse.class);
                        mTextView.setText(mgsonResponse.getGlossary().getGlossDiv().getGlossList().getGlossEntry().getID());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("ERROR!!!!");
                        // TODO Auto-generated method stub

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
