package com.reactnativegooglecastv3;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.images.WebImage;

import java.io.IOException;

import static com.google.android.gms.cast.framework.CastState.CONNECTED;
import static com.google.android.gms.cast.framework.CastState.CONNECTING;
import static com.reactnativegooglecastv3.GoogleCastPackage.NAMESPACE;
import static com.reactnativegooglecastv3.GoogleCastPackage.TAG;
import static com.reactnativegooglecastv3.GoogleCastPackage.metadata;

public class CastManager {
    static CastManager instance;

    final CastContext castContext;
    final SessionManager sessionManager;
    final CastStateListenerImpl castStateListener;
    final SessionManagerListenerImpl sessionManagerListener;
    ReactContext reactContext;
    CastDevice castDevice;

    RemoteMediaClient mRemoteMediaClient;
    MediaMetadata mediaMetadata;

    CastManager(Context parent) {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(parent) == ConnectionResult.SUCCESS) {
            this.castContext = CastContext.getSharedInstance(parent);
            this.sessionManager = castContext.getSessionManager();
            this.castStateListener = new CastStateListenerImpl();
            this.sessionManagerListener = new SessionManagerListenerImpl();
        }
        else {
            Log.w(TAG, "Google Play services not installed on device. Cannot cast.");
            this.castContext = null;
            this.sessionManager = null;
            this.castStateListener = null;
            this.sessionManagerListener = null;
        }
    }

    public static void init(Context ctx) {
        instance = new CastManager(ctx);
    }

    void addStateListeners() {
        if (this.castContext == null || this.sessionManager == null) { return; }
        castContext.addCastStateListener(this.castStateListener);
        sessionManager.addSessionManagerListener(this.sessionManagerListener, CastSession.class);
    }

    void removeStateListeners() {
        if (this.castContext == null || this.sessionManager == null) { return; }
        castContext.removeCastStateListener(this.castStateListener);
        sessionManager.removeSessionManagerListener(this.sessionManagerListener, CastSession.class);
    }

    public void sendMessage(String namespace, String message) {
        CastSession session = sessionManager.getCurrentCastSession();
        if (session == null) return;
        session.sendMessage(namespace, message);
    }

    public void setMediaMetadata(String title, String subtitle, String imageUri) {
        mediaMetadata = new MediaMetadata();

        mediaMetadata.putString(MediaMetadata.KEY_TITLE, title);
        mediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, subtitle);
        mediaMetadata.addImage(new WebImage(Uri.parse(imageUri)));
    }

    public void resetMediaMetadata() {
        mediaMetadata = null;
    }

    public void loadVideo(String videoUri) {
        CastSession session = sessionManager.getCurrentCastSession();

        MediaInfo mediaInfo = new MediaInfo.Builder(videoUri)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("video/mp4")
                .setMetadata(mediaMetadata)
                .build();

        mRemoteMediaClient = session.getRemoteMediaClient();
        mRemoteMediaClient.addListener(new RemoteMediaClient.Listener()
        {
            @Override
            public void onAdBreakStatusUpdated() { }
            @Override
            public void onMetadataUpdated() { }
            @Override
            public void onPreloadStatusUpdated() { }
            @Override
            public void onQueueStatusUpdated() { }
            @Override
            public void onSendingRemoteMediaRequest() { }

            @Override
            public void onStatusUpdated() {
                if (mRemoteMediaClient != null) {
                    if (reactContext != null)
                        reactContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("googleCastPlayerState", mRemoteMediaClient.getPlayerState());
                }
            }
        });
        mRemoteMediaClient.addProgressListener(new ProgressListenerImpl(), 500);
        mRemoteMediaClient.load(mediaInfo, true, 0);
    }

    public void loadAudio(String audioUri) {
        CastSession session = sessionManager.getCurrentCastSession();

        MediaInfo mediaInfo = new MediaInfo.Builder(audioUri)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/mpeg")
                .setMetadata(mediaMetadata)
                .build();
        mRemoteMediaClient = session.getRemoteMediaClient();
        mRemoteMediaClient.addListener(new RemoteMediaClient.Listener()
        {
            @Override
            public void onAdBreakStatusUpdated() { }
            @Override
            public void onMetadataUpdated() { }
            @Override
            public void onPreloadStatusUpdated() { }
            @Override
            public void onQueueStatusUpdated() { }
            @Override
            public void onSendingRemoteMediaRequest() { }

            @Override
            public void onStatusUpdated() {
                if (mRemoteMediaClient != null) {
                    if (reactContext != null)
                        reactContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("googleCastPlayerState", mRemoteMediaClient.getPlayerState());
                }
            }
        });
        mRemoteMediaClient.addProgressListener(new ProgressListenerImpl(), 500);
        mRemoteMediaClient.load(mediaInfo, true, 0);
    }

    public void getMediaState(Callback callback) {
        if (mRemoteMediaClient == null) {
            callback.invoke( 0 );
            return;
        }

        callback.invoke( mRemoteMediaClient.getPlayerState() );
    }

    public void togglePlayerState() {
        if (mRemoteMediaClient.isPlaying())
            mRemoteMediaClient.pause();
        else if (mRemoteMediaClient.isPaused())
            mRemoteMediaClient.play();
    }

    public void seek(int position) {
        mRemoteMediaClient.seek(position, mRemoteMediaClient.RESUME_STATE_UNCHANGED);
    }

    public void resetCasting() {
        mRemoteMediaClient.stop();
    }

    public void disconnect() {
        try {
            sessionManager.endCurrentSession(true);
        } catch (RuntimeException re) {
            Log.w(TAG, "RuntimeException in CastManager.disconnect.", re);
        }
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

    private class ProgressListenerImpl implements ProgressListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onProgressUpdated(long progressMs, long durationMs) {
            int progress = Math.toIntExact(progressMs / 1000);
            int duration = Math.toIntExact(progressMs / 1000);

            if (reactContext != null) {
                WritableMap map = Arguments.createMap();
                map.putInt("progress", progress);
                map.putInt("duration", duration);

                reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("googleCastProgress", map);
            }
        }
    }

    private class SessionManagerListenerImpl extends SessionManagerListenerBase {
        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            setMessageReceivedCallbacks(session);
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            setMessageReceivedCallbacks(session);
        }

        private void setMessageReceivedCallbacks(CastSession session) {
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
