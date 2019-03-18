package com.reactnativegooglecastv3;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Map;

import javax.annotation.Nullable;

import static com.reactnativegooglecastv3.GoogleCastPackage.TAG;

class CastButtonManager extends SimpleViewManager<CustomMediaRouteButton> {
    @Override
    public String getName() {
        return "CastButton";
    }

    @Override
    protected CustomMediaRouteButton createViewInstance(ThemedReactContext ctx) {
        CustomMediaRouteButton button = new CustomMediaRouteButton(ctx);
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS) {
            try {
                CastButtonFactory.setUpMediaRouteButton(ctx.getApplicationContext(), button);
            } catch (RuntimeException re) {
                Log.w(TAG, "RuntimeException in CastButtonManager.createViewInstance. Cannot create button.", re);
            }
        }
        return button;
    }

    @ReactProp(name = "color", customType = "Color")
    public void setColor(CustomMediaRouteButton view, int color) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(view.getContext(), android.support.v7.mediarouter.R.style.Theme_MediaRouter);
        TypedArray a = ctw.obtainStyledAttributes(null,
                android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = a.getDrawable(
                android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);
        a.recycle();
        DrawableCompat.setTint(drawable, color);
        view.setRemoteIndicatorDrawable(drawable);
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("click", 1);
    }

    @Override
    public void receiveCommand(CustomMediaRouteButton view, int commandId, @Nullable ReadableArray args) {
        Activity activity = view.context.getCurrentActivity();
        if (activity == null) return;
        if (activity instanceof FragmentActivity) {
            boolean isTooLate = ((FragmentActivity)activity).getSupportFragmentManager().isStateSaved();
            if (isTooLate) return;
        }
        if (commandId == 1) view.performClick();
    }
}
