package com.reactnativegooglecastv3;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.framework.*;

import java.io.IOException;

import static com.google.android.gms.cast.framework.CastState.CONNECTED;
import static com.google.android.gms.cast.framework.CastState.CONNECTING;
import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;
import static com.reactnativegooglecastv3.GoogleCastPackage.TAG;
import static com.reactnativegooglecastv3.GoogleCastPackage.metadata;

public class CastManager {
    static CastManager instance;

    final Context parent;
    final CastContext castContext;
    final SessionManager sessionManager;
    final CastStateListenerImpl castStateListener;
    ReactContext reactContext;
    CastDevice castDevice;

    CastManager(Context parent) {
        this.parent = parent;
        this.castContext = CastContext.getSharedInstance(parent);
        this.sessionManager = castContext.getSessionManager();
        this.castStateListener = new CastStateListenerImpl();
        castContext.addCastStateListener(this.castStateListener);
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

    public void triggerStateChange() {
        this.castStateListener.onCastStateChanged(castContext.getCastState());
    }

    private class CastStateListenerImpl implements CastStateListener {
        @Override
        public void onCastStateChanged(int state) {
            Log.d(TAG, "onCastStateChanged: " + state);
            if (state == CONNECTING || state == CONNECTED) {
                castDevice = sessionManager.getCurrentCastSession().getCastDevice();
            } else {
                castDevice = null;
            }
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
                        metadata(NAMESPACE, "", reactContext),
                        new CastMessageReceivedCallback());
            } catch (IOException e) {
                Log.e(TAG, "Cast channel creation failed: ", e);
            }
        }
    }

    private class CastMessageReceivedCallback implements Cast.MessageReceivedCallback {
        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
            Log.d(TAG, "onMessageReceived: " + namespace + " / " + message);
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
