import React from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';

import { DeviceEventEmitter, NativeModules, requireNativeComponent, ViewPropTypes } from 'react-native'
import PropTypes from 'prop-types'

const Cast = NativeModules.GoogleCastV3

const CastButton = requireNativeComponent('CastButton', {
  name: 'CastButton',
  propTypes: {
    ...ViewPropTypes,
  },
})

export default class App extends React.Component {

  componentWillMount() {
    console.log('Mounting!');
    DeviceEventEmitter.addListener('googleCastStateChanged', e => {
      console.log(e)
    })
    DeviceEventEmitter.addListener('googleCastMessage', e => {
      console.log(e)
    })

  }
  render() {
    return (
      <View style={s.container}>
        <Button title="Send!" onPress={() => {
          Cast.send(Cast.namespace, "WHADDUP")
        }} />
        <Text style={s.text}>messages</Text>
        <CastButton style={{ width: 100, height: 30, backgroundColor: 'red' }} />
      </View>
    );
  }
}

const s = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    color: '#333',
    marginBottom: 30,
  }
});
