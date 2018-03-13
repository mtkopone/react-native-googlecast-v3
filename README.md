# react-native-googlecast-v3

Google Cast SDK v3 support for React-Native

## CAVEATS!
* Only Android at the moment, iOS to come.
* Only supports talking to your custom receiver thru a custom channel at the moment, so the default use-case of playing some media is currently **NOT** supported. Sorry, but this is what I needed today.

# Installation

    npm install --save react-native-googlecast-v3
    react-native link

Or, you know, the same in yarn if that's what we are all using this week...

## Configuration for Android

Add required dependencies to `./android/app/build.gradle`:

    dependencies {
      compile project(':react-native-googlecast-v3')
      compile "com.android.support:appcompat-v7:23.0.1"
      compile "com.google.android.gms:play-services-cast-framework:10.0.1"
      ... // And so on
    }

Make sure the version of the appcompat and mediarouter dependencies matches your compileSdkVersion.

Add the following inside the `<application>`-element in `./android/app/src/main/AndroidManifest.xml`:

    <meta-data
      android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
      android:value="com.reactnativegooglecastv3.CastOptionsProvider" />
    <meta-data
      android:name="com.reactnativegooglecastv3.castAppId"
      android:value="YOUR_APP_ID" />
    <meta-data
      android:name="com.reactnativegooglecastv3.castNamespace"
      android:value="urn:x-cast:your.own.namespace" />

Make the following changes to `./android/app/src/main/java/your.package/MainActivity.java`:

* Make MainActivity extend `com.facebook.react.ReactFragmentActivity`

* Override onStart, and add CastManager initialization:

      @Override
      protected void onStart() {
        super.onStart();
        CastManager.init(this);
      }

# Usage

#### Import

    import CastButton, { GoogleCastV3 } from 'react-native-googlecastv3'

    GoogleCastV3.appId // is your castAppId
    GoogleCastV3.namespace // is your castNamespace

#### Render the Cast button

The CastButton will appear and disappear depending on cast device availability, and show the current connection status:

    <View>
      <CastButton />
    </View>

#### Send messages to a connected Cast device

Using the namespace declared in you AndroidManifest.xml:

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
