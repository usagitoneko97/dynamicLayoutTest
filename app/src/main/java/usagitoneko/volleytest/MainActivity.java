package usagitoneko.volleytest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RemoteDisplayLayout.getRemoteDisplayStatus  {

    NfcAdapter mNfcAdapter;
    private JSONObject mResult;
    private View sampleView;
    private Bundle viewBundle;
    private SimpleFragmentPagerAdapter pageAdapter;
    private ViewPager pager;
    private List<Boolean>allLedStatus;
    public final int LED2 =0;
    public final int LED_GREEN =1;
    public final int LED_BLUE = 2;
    public final int LED_ORANGE = 3;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private Bundle bundle;
    Gson mGson;
   // GsonResponse mgsonResponse;
    JSONObject mJSONObject;

    @Override
    public void getAllLedStatus(List<Boolean> allLedStatus) {
        this.allLedStatus = allLedStatus;
    }

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
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC. ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            //inform user NFC is disabled
        } else {
            handleIntent(getIntent());
            //display whatever title desired
        }

        bundle = new Bundle();
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

    @Override
    protected void onPause() {
        if(mNfcAdapter!=null)
            stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        if(mNfcAdapter!=null)
            setupForegroundDispatch(this, mNfcAdapter);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } /*else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }*/ //TECH_DISCOVERED will filter on the onNewIntent
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
        {
           // log.append("\nNfc type intent: ACTION_TECH_DISCOVERED");
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NfcV nfcv = NfcV.get(detectedTag);
            if(nfcv == null){
                //log.append("\nError! NfcV not detected");
                //not nfcV type
            }
            else {
                try {
                    nfcv.connect();
                    if (nfcv.isConnected()) {
                        //log.append("\nsucessfully connected to nfc type V");
                        byte[] buffer;
                       //log.append("\nBegin writing to tag...");
                        int resultAllLed = 0x10;//initial value predefined
                        if (allLedStatus.get(LED2)) {
                            resultAllLed = resultAllLed | (1 << 0); //set bit 0
                        }
                        if (allLedStatus.get(LED_GREEN)) {
                            resultAllLed = resultAllLed | (1 << 1); //set bit 1
                        }
                        if (allLedStatus.get(LED_BLUE)) {
                            resultAllLed = resultAllLed | (1 << 2); //set bit 2
                        }
                        if (allLedStatus.get(LED_ORANGE)) {
                            resultAllLed = resultAllLed | (1 << 3); //set bit 3
                        }
                        Toast.makeText(this, "prepare for transmit", Toast.LENGTH_SHORT).show();
                        buffer = nfcv.transceive(new byte[]{(byte) 0x02, (byte) 0x21, (byte) 0, (byte) resultAllLed, (byte) 0x00, (byte) 0x72, (byte) 0x75}); //11 instead of 01 is because to avoid nfcv cant read 00 bug
                        // TODO: 23/2/2017   should do checking at buffer
                        Toast.makeText(this, "successfully write in the tag! ", Toast.LENGTH_SHORT).show();
                        //log.append("\nsuccessfully write in the tag! ");

                        //log.append("\nBegin Reading from tag...");
                        RemoteDisplayLayout mainFragment = (RemoteDisplayLayout) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":0");

                        buffer = nfcv.transceive(new byte[]{0x02, 0x20, (byte) 0}); //read 0th byte (total 4 bytes)
                        //log.append("\nsuccessfully read from the tag! ");
                        int ledStatus = toInteger(buffer);

                        //log.append("led status: " + ledStatus + ", " + numberToHex(ledStatus));
                        LedState ledState = new LedState( ledStatus);
                        bundle.putBoolean("led2", ledState.isLed2State());
                        bundle.putBoolean("ledGreen", ledState.isGreenLedState());
                        bundle.putBoolean("ledBlue", ledState.isBlueLedState());
                        bundle.putBoolean("ledOrange", ledState.isOrangeLedState());

                        //log.append("\nClosing nfcv connection...");
                        nfcv.close();
                    }else
                        Toast.makeText(this, "ERROR!", Toast.LENGTH_SHORT).show();
                        //log.append("\nNot connected to the tag");
                } catch (IOException e) {
                    //log.append("\nError");
                }

            }
        }
        else {  //has NDEF inside the tag
            //log.append("\nNDEF data found inside!");
            handleIntent(intent); //read data on the tag and display to the textview
            if (isNfcIntent(intent)) {
                NdefMessage ndefMessage = createTextMessage("hello there!");

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeTag(tag, ndefMessage);
            }
        }
    }
    boolean isNfcIntent(Intent intent) {
        return intent.hasExtra(NfcAdapter.EXTRA_TAG);
    }
    public int toInteger(byte[] bytes){
        int result =0;
        for(int i=3;i>0;i--){
            result<<=8;
            result +=bytes[i];
        }
        return result;
    }
    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[]{NfcV.class.getName()}}; //added NfcV

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@ink BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
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
                final RemoteDisplayLayout mainFragment = new RemoteDisplayLayout();
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

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //nfc_result.setText("Read content: " + result);
            }
        }
    }
    boolean writeTag( Tag detectedTag, NdefMessage message) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "The data cannot written to tag, Tag capacity is " + ndef.getMaxSize() + " bytes, message is "
                            + size + " bytes.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                ndef.close();
                Toast.makeText(this, "Message is written!",
                        Toast.LENGTH_SHORT).show();
                return true;
            } else {
                NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
                if (ndefFormat != null) {
                    try {
                        ndefFormat.connect();
                        ndefFormat.format(message);
                        ndefFormat.close();
                        Toast.makeText(this, "The data is written to the tag ",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to format tag",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NDEF is not supported",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Write opreation is failed",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
