package com.reactnativegooglecastv3;

import android.os.Handler;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.framework.CastState;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.reactnativegooglecastv3.GoogleCastPackage.APP_ID;
import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;
import static com.reactnativegooglecastv3.GoogleCastPackage.TAG;

public class CastModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private final ReactApplicationContext reactContext;
    private final Handler handler;

    CastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        handler = new Handler(reactContext.getMainLooper());
        reactContext.addLifecycleEventListener(this);
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
    public void setMediaMetadata(final String title, final String subtitle, final String imageUri) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.setMediaMetadata(title, subtitle, imageUri);
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void resetMediaMetadata() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.resetMediaMetadata();
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void loadVideo(final String videoUri) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.loadVideo(videoUri);
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void loadAudio(final String audioUri) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.loadAudio(audioUri);
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void getMediaState(final Callback callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.getMediaState( callback );
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void togglePlayerState() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.togglePlayerState();
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void seek(final int position) {
        if (CastManager.instance.castContext == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.seek(position);
            }
        });
    }

    @ReactMethod @SuppressWarnings("unused")
    public void resetCasting() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.resetCasting();
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

    @Nonnull
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
        constants.put("PLAYER_STATE_UNKNOWN", 0);
        constants.put("PLAYER_STATE_IDLE", 1);
        constants.put("PLAYER_STATE_PLAYING", 2);
        constants.put("PLAYER_STATE_PAUSED", 3);
        constants.put("PLAYER_STATE_BUFFERING", 4);
        return constants;
    }

    @Override
    public void onCatalystInstanceDestroy() {
        CastManager.instance.reactContext = null;
    }

    @Override
    public void onHostResume() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.addStateListeners();
            }
        });

    }

    @Override
    public void onHostPause() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.removeStateListeners();
            }
        });
    }

    @Override
    public void onHostDestroy() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CastManager.instance.removeStateListeners();
            }
        });
    }
}
