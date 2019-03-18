package com.reactnativegooglecastv3;

import android.support.v7.app.MediaRouteButton;

import com.facebook.react.uimanager.ThemedReactContext;

public class CustomMediaRouteButton extends MediaRouteButton {
  protected final ThemedReactContext context;

  public CustomMediaRouteButton(ThemedReactContext ctx) {
    super(ctx);
    this.context = ctx;
  }
}
