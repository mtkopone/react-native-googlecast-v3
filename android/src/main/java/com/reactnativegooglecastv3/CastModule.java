package com.reactnativegooglecastv3;

import android.os.Handler;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.cast.framework.CastState;

import java.util.HashMap;
import java.util.Map;

import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;

public class CastModule extends ReactContextBaseJavaModule {
    final ReactApplicationContext reactContext;
    final Handler handler;

    public CastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        handler = new Handler(reactContext.getMainLooper());
        CastManager.instance.reactContext = reactContext;
    }

    @ReactMethod @SuppressWarnings("unused")
    public void send(final String namespace, final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.sendMessage(namespace, message);
            }
        });
    }

    @Override
    public String getName() {
        return "GoogleCastV3";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("namespace", GoogleCastPackage.metadata(NAMESPACE, "", reactContext));
        constants.put("NO_DEVICES_AVAILABLE", CastState.NO_DEVICES_AVAILABLE);
        constants.put("NOT_CONNECTED", CastState.NOT_CONNECTED);
        constants.put("CONNECTING", CastState.CONNECTING);
        constants.put("CONNECTED", CastState.CONNECTED);
        return constants;
    }

    @Override
    public void onCatalystInstanceDestroy() {
        CastManager.instance.reactContext = null;
    }
}
