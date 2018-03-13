package com.reactnativegooglecastv3;

import android.support.v7.app.MediaRouteButton;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.cast.framework.CastButtonFactory;

class CastButtonManager extends SimpleViewManager<MediaRouteButton> {
    @Override
    public String getName() {
        return "CastButton";
    }

    @Override
    protected MediaRouteButton createViewInstance(ThemedReactContext ctx) {
        MediaRouteButton button = new MediaRouteButton(ctx);
        CastButtonFactory.setUpMediaRouteButton(ctx.getApplicationContext(), button);
        return button;
    }
}
