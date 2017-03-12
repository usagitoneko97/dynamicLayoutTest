package usagitoneko.volleytest;

import android.view.View;

import json2view.DynamicViewId;



public class ViewIds {
    static public class SampleViewHolder {
        @DynamicViewId(id = "led2")
        public View clickableView;
        @DynamicViewId(id="button")

        public SampleViewHolder() {
        }
    }
}
