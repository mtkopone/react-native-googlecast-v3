package com.reactnativegooglecastv3;

import com.facebook.react.ReactFragmentActivity;

/*

- Activity must extend ReactFragmentActivity


 */


public class MainActivity extends ReactFragmentActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "reactnativegooglecastv3";
    }

    @Override
    protected void onStart() {
        super.onStart();
        CastManager.init(this);
    }
}
