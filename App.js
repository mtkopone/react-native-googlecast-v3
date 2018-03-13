import React from 'react'
import { Button, StyleSheet, Text, View } from 'react-native'

import CastButton, { GoogleCastV3 } from './googlecast-v3'

export default class App extends React.Component {
  constructor() {
    super(...arguments)
    this.state = { messages: [] }
  }
  componentWillMount() {
    console.log('Mounting!');
    GoogleCastV3.addCastStateListener(state => {
      console.log('cast state:', state)
    })
    GoogleCastV3.addCastMessageListener(message => {
      console.log('msg: ' , message)
      this.setState({ messages: this.state.messages.concat([message]) })
    })
  }

  render() {
    console.log(this.state.messages);
    return (
      <View style={s.container}>
        {this.state.messages.map((m, i) =>
          <Text style={s.text} key={i}>{m.message}</Text>
        )}
        <Button title="Send!" onPress={() => {
          GoogleCastV3.send("WHADDUP")
        }} />
        <CastButton style={{ color: 'black', marginTop: 20, width: 100, height: 30, backgroundColor: 'red' }} />
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
