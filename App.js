import React from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';

import { DeviceEventEmitter, NativeModules, requireNativeComponent, ViewPropTypes } from 'react-native'
import PropTypes from 'prop-types'

import CastButton, { GoogleCastV3 } from './googlecast-v3'

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
          GoogleCastV3.send("WHADDUP")
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
