import React, { PureComponent } from 'react'
import ReactNative, { Platform, DeviceEventEmitter, NativeModules, requireNativeComponent, UIManager, View, ViewPropTypes } from 'react-native'
import PropTypes from 'prop-types'

const NativeGoogleCastV3 = NativeModules.GoogleCastV3

const GoogleCastV3Handler = {
  ...NativeGoogleCastV3,
  send: (a, b) => {
    (b === undefined)
      ? NativeGoogleCastV3.send(NativeGoogleCastV3.namespace, a)
      : NativeGoogleCastV3.send(a, b)
  },
  resetMediaMetadata: () => {
    NativeGoogleCastV3.resetMediaMetadata()
  },
  setMediaMetadata: (title, subtitle, imageUri) => {
    NativeGoogleCastV3.setMediaMetadata(title, subtitle, imageUri)
  },
  loadVideo: (videoUri) => {
    NativeGoogleCastV3.loadVideo(videoUri)
  },
  loadAudio: (audioUri) => {
    NativeGoogleCastV3.loadAudio(audioUri)
  },
  getMediaState: ( callback ) => {
    NativeGoogleCastV3.getMediaState( callback )
  },
  togglePlayState: () => {
    NativeGoogleCastV3.togglePlayState()
  },
  seek: (position) => {
    NativeGoogleCastV3.seek(position)
  },
  resetCasting: () => {
    NativeGoogleCastV3.resetCasting();
  },
  disconnect: () => {
    NativeGoogleCastV3.disconnect();
  },
  addCastStateListener: (fn) => {
    const subscription = DeviceEventEmitter.addListener('googleCastStateChanged', fn)
    NativeGoogleCastV3.triggerStateChange()
    return subscription
  },
  addCastMessageListener: (fn) => DeviceEventEmitter.addListener('googleCastMessage', fn),
  addProgressListener: (fn) => DeviceEventEmitter.addListener('googleCastProgress', fn),
  addCastPlayerStateListener: (fn) => DeviceEventEmitter.addListener('googleCastPlayerState', fn),
}

const stub = {
  NO_DEVICES_AVAILABLE: 1,
  NOT_CONNECTED: 2,
  CONNECTING: 3,
  CONNECTED: 4,
  PLAYER_STATE_UNKNOWN: 0,
  PLAYER_STATE_IDLE: 1,
  PLAYER_STATE_PLAYING: 2,
  PLAYER_STATE_PAUSED: 3,
  PLAYER_STATE_BUFFERING: 4,
  send: () => { },
  addCastStateListener: (fn) => {
    fn(stub.NO_DEVICES_AVAILABLE)
  },
  addCastMessageListener: () => { },
  addProgressListener: () => { },
  addCastPlayerStateListener: () => { },
  getCurrentDevice: () => Promise.resolve(null)
}

export const GoogleCastV3 = (Platform.OS === 'android') ? GoogleCastV3Handler : stub

class CastButton extends PureComponent {
  static propTypes = {
    ...ViewPropTypes,
    color: PropTypes.string,
  }

  click = () => {
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.CastButton.Commands.click,
      [],
    )
  }

  render() {
    return (
      <NativeCastButton {...this.props} />
    );
  }
}

const NativeCastButton = requireNativeComponent('CastButton', CastButton)

export default (Platform.OS === 'android') ? CastButton : View
