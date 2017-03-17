package usagitoneko.volleytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import json2view.DynamicView;


public class RemoteDisplayLayout extends Fragment implements View.OnClickListener  {

    private View led2;
    private View ledBlue;
    private View ledGreen;
    private View ledOrange;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /*@Override
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
    }*/

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
            led2 = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).led2;
            led2 = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).led2;
            led2 = ((ViewIds.SampleViewHolder)firstFragmentView.getTag()).led2;
            led2.setOnClickListener(this);
            Object led2Object = 1;
            led2.setTag(1);
            return firstFragmentView;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == (Object)1){
            Toast.makeText(getActivity(), "you have succeed again!!", Toast.LENGTH_SHORT).show();
        }
    }
}
