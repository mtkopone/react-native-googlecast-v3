/**
 * @providesModule react-native-googlecast-v3
 */
import { DeviceEventEmitter, NativeModules, requireNativeComponent, ViewPropTypes } from 'react-native'

const NativeGoogleCastV3 = NativeModules.GoogleCastV3

export const GoogleCastV3 = {
  ...NativeGoogleCastV3,

  send: (a, b) => {
    (b === undefined)
      ? NativeGoogleCastV3.send(NativeGoogleCastV3.namespace, a)
      : NativeGoogleCastV3.send(a, b)
  },
  addCastStateListener: (fn) => DeviceEventEmitter.addListener('googleCastStateChanged', fn),
  addCastMessageListener: (fn) => DeviceEventEmitter.addListener('googleCastMessage', fn),
}

const CastButton = requireNativeComponent('CastButton', {
  name: 'CastButton',
  propTypes: {
    ...ViewPropTypes,
  },
})

export default CastButton
