package com.reactnativegooglecastv3;

import android.os.Handler;
import android.util.Log;

import com.facebook.react.bridge.*;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.framework.CastState;

import java.util.HashMap;
import java.util.Map;

import static com.reactnativegooglecastv3.GoogleCastPackage.APP_ID;
import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;
import static com.reactnativegooglecastv3.GoogleCastPackage.TAG;

public class CastModule extends ReactContextBaseJavaModule {
    final ReactApplicationContext reactContext;
    final Handler handler;

    public CastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        handler = new Handler(reactContext.getMainLooper());
    }

    @Override
    public void initialize() {
        super.initialize();
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

    @ReactMethod @SuppressWarnings("unused")
    public void disconnect() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.disconnect();
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void getCurrentDevice(Promise promise) {
        CastDevice d = CastManager.instance.castDevice;
        Log.d(TAG, "getCurrentDevice: " + d);
        if (d == null) {
            promise.resolve(null);
        } else {
            WritableMap map = Arguments.createMap();
            map.putString("id", d.getDeviceId());
            map.putString("version", d.getDeviceVersion());
            map.putString("name", d.getFriendlyName());
            map.putString("model", d.getModelName());
            promise.resolve(map);
        }
    }

    @ReactMethod @SuppressWarnings("unused")
    public void triggerStateChange() {
        if (CastManager.instance.castContext == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.triggerStateChange();
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
        constants.put("appId", GoogleCastPackage.metadata(APP_ID, "", reactContext));
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
