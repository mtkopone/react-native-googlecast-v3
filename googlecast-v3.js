import { DeviceEventEmitter, NativeModules, requireNativeComponent, ViewPropTypes } from 'react-native'
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

const CastButton = requireNativeComponent('CastButton', {
  name: 'CastButton',
  propTypes: {
    ...ViewPropTypes,
    color: PropTypes.string,
  },
})

export default CastButton
