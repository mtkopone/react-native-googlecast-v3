package com.reactnativegooglecastv3;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.SessionManager;

import java.io.IOException;

import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;

public class CastManager {
    static final String TAG = "GoogleCastV3";
    static CastManager instance;

    Context parent;
    CastContext castContext;
    SessionManager sessionManager;
    ReactContext reactContext;


    CastManager(Context parent) {
        this.parent = parent;
        this.castContext = CastContext.getSharedInstance(parent);
        this.sessionManager = castContext.getSessionManager();
        castContext.addCastStateListener(new CastStateListenerImpl());
        sessionManager.addSessionManagerListener(new SessionManagerListenerImpl(), CastSession.class);

    }

    public static void init(Context ctx) {
        instance = new CastManager(ctx);
    }

    public void sendMessage(String namespace, String message) {
        CastSession session = sessionManager.getCurrentCastSession();
        if (session == null) return;
        session.sendMessage(namespace, message);
    }

    private class CastStateListenerImpl implements CastStateListener {
        @Override
        public void onCastStateChanged(int state) {
            Log.e("GoogleCastV3", "onCastStateChanged: " + state);
            if (reactContext != null) reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("googleCastStateChanged", state);
        }
    }

    private class SessionManagerListenerImpl extends SessionManagerListenerBase {
        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            try {
                if (reactContext != null)
                    session.setMessageReceivedCallbacks(
                        GoogleCastPackage.metadata(NAMESPACE, "", reactContext),
                        new CastMessageReceivedCallback());
            } catch (IOException e) {
                Log.e(TAG, "Cast channel creation failed: ", e);
            }
        }
    }

    private class CastMessageReceivedCallback implements Cast.MessageReceivedCallback {
        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
            Log.e(TAG, "onMessageReceived: " + namespace + " / " + message);
            if (reactContext == null) return;
            WritableMap map = Arguments.createMap();
            map.putString("namespace", namespace);
            map.putString("message", message);
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("googleCastMessage", map);

        }
    }

}
