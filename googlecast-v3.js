import React, { PureComponent } from 'react'
import ReactNative, { DeviceEventEmitter, NativeModules, requireNativeComponent, UIManager, ViewPropTypes } from 'react-native'
import PropTypes from 'prop-types'

const NativeGoogleCastV3 = NativeModules.GoogleCastV3

export const GoogleCastV3 = {
  ...NativeGoogleCastV3,

  send: (a, b) => {
    (b === undefined)
      ? NativeGoogleCastV3.send(NativeGoogleCastV3.namespace, a)
      : NativeGoogleCastV3.send(a, b)
  },
  addCastStateListener: (fn) => {
    const subscription = DeviceEventEmitter.addListener('googleCastStateChanged', fn)
    NativeGoogleCastV3.triggerStateChange()
    return subscription
  },
  addCastMessageListener: (fn) => DeviceEventEmitter.addListener('googleCastMessage', fn),
}

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

export default CastButton
