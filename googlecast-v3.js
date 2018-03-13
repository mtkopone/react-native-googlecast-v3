/*
 * @providesModule react-native-googlecast-v3
 */
import { DeviceEventEmitter, NativeModules, requireNativeComponent, ViewPropTypes } from 'react-native'

export const GoogleCastV3 = NativeModules.GoogleCastV3

// TODO: wrap GoogleCastV3+DeviceEventEmitter into a more usable API.

const originalSend = GoogleCastV3.send

GoogleCastV3.send = (a, b) => {
  (b === undefined)
    ? originalSend(GoogleCastV3.namespace, a)
    : originalSend(a, b)
}

const CastButton = requireNativeComponent('CastButton', {
  name: 'CastButton',
  propTypes: {
    ...ViewPropTypes,
  },
})

export default CastButton
