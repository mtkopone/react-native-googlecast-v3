# react-native-googlecast-v3

Google Cast SDK v3 support for react-Native

## CAVEATS!
* Only Android at the moment, iOS to come...
* Only supports talking to your custom receiver thru a custom channel at the moment, so the default use-case of playing some media is currently **NOT** supported. Sorry, but this is what I needed today...

# Installation

    npm install --save <TODO:i-haven't-published-in-npm-yet>
    npm link

Or, you know, the same in yarn if that's what we are all using this week...

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

#### Import

    import CastButton, { GoogleCastV3 } from 'react-native-googlecastv3'

#### Render the Cast button

The CastButton will appear and disappear depending on cast device availability, and show the current connection status:

    <View>
      <CastButton />
    </View>

#### Send messages to a connected Cast device

Using the namespace declared in strings.xml:

    GoogleCastV3.send('WHADDUP')

Using some other namespace:

    GoogleCastV3.send('urn:x-cast:some.other.namespace', 'WHADDUP')

#### Listen to things

    GoogleCastV3.addCastStateListener(state => {
      // state is one of: GoogleCastV3.NO_DEVICES_AVAILABLE, GoogleCastV3.NOT_CONNECTED, GoogleCastV3.CONNECTING, GoogleCastV3.CONNECTED
    })

    GoogleCastV3.addCastMessageListener(message => {
      // message is: { namespace: 'urn:x-cast:your.own.namespace', message: String }
    })
