package usagitoneko.volleytest;

import android.view.View;

import json2view.DynamicViewId;



public class ViewIds {
    static public class SampleViewHolder {
        @DynamicViewId(id = "led2")
        public View led2;
        @DynamicViewId(id = "ledGreen")
        public View ledGreen;
        @DynamicViewId(id = "ledBlue")
        public View ledBlue;
        @DynamicViewId(id = "ledOrange")
        public View ledOrange;
        public SampleViewHolder() {
        }
    }
}
