# react-native-googlecast-v3

Google Cast SDK v3 support for react-Native

Only Android at the moment, iOS to come...

# Installation

    npm install --save <TODO:my-repo>
    npm link

## Configuration for Android

Add the following inside the `<application>`-element in `./android/app/src/main/AndroidManifest.xml`:

    <meta-data
      android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
      android:value="com.reactnativegooglecastv3.CastOptionsProvider"
    />

Add your Google Cast app id and the namespace whose messages you want to listen to into `.android/app/src/main/res/values/strings.xml`:

    <string name="castAppId">YOUR_APP_ID</string>
    <string name="castNamespace">urn:x-cast:your.own.namespace</string>

Make the following changes to `./android/app/src/main/java/your.package/MainActivity.java`:

* Make MainActivity extend `com.facebook.react.ReactFragmentActivity`

* Override onStart, and add cast initialization:
      @Override
      protected void onStart() {
        super.onStart();
        CastManager.init(this);
      }

# Usage

**TODO:** write me!
