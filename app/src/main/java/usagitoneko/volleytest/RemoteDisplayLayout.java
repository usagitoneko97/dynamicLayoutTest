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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import json2view.DynamicView;


public class RemoteDisplayLayout extends Fragment implements View.OnClickListener  {

    private View led2;
    private View ledBlue;
    private View ledGreen;
    private View ledOrange;
    private static final  Object led2Tag = 0;
    private static final Object greenLedTag = 1;
    private static final Object blueLedTag = 2;
    private static final Object orangeLedTag = 3;
    public final int LED2 =1;
    public final int LED_GREEN =2;
    public final int LED_BLUE = 3;
    public final int LED_ORANGE = 4;
    private List<Boolean> allLedStatus = new ArrayList<>();

    public interface getRemoteDisplayStatus{
        public void getAllLedStatus (List<Boolean>allLedStatus);
    }
    getRemoteDisplayStatus mGetRemoteDisplayStatus;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            mGetRemoteDisplayStatus = (getRemoteDisplayStatus) a;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement getRemoteDisplayStatus");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGetRemoteDisplayStatus = null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_display_layout, container, false);
        final Bundle args = getArguments();
        try {
            JSONObject viewJSON = new JSONObject(args.getString("viewString"));
            View firstFragmentView = DynamicView.createView(getActivity(),viewJSON, ViewIds.SampleViewHolder.class );
            firstFragmentView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
            led2 = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).led2;
            ledGreen = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).ledGreen;
            ledBlue = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).ledBlue;
            ledOrange = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).ledOrange;

            Switch led2Switch = (Switch)led2;
            Switch ledGreenSwitch = (Switch)ledGreen;
            Switch ledBlueSwitch = (Switch)ledBlue;
            Switch ledOrangeSwitch = (Switch)ledOrange;

            led2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        allLedStatus.set(LED2, true);
                    }
                    else{
                        allLedStatus.set(LED2, false);
                    }
                }
            });
            ledGreenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        allLedStatus.set(LED_GREEN, true);
                    }
                    else{
                        allLedStatus.set(LED_GREEN, false);
                    }
                }
            });
            ledBlueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        allLedStatus.set(LED_BLUE, true);
                    }
                    else{
                        allLedStatus.set(LED_BLUE, false);
                    }
                }
            });ledOrangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        allLedStatus.set(LED_ORANGE, true);
                    }
                    else{
                        allLedStatus.set(LED_ORANGE, false);
                    }
                }
            });
            return firstFragmentView;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        // TODO: 17/3/2017 show error message on this view
        return view;
    }

    @Override
    public void onClick(View v) {
    }

}
