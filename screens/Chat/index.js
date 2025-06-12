import {StyleSheet, Text, View} from 'react-native';
import React from 'react';

const Chat = ({route}) => {
  const {task} = route.params;

  console.log(task.title);

  return (
    <View>
      <Text>Chat</Text>
    </View>
  );
};

export default Chat;

const styles = StyleSheet.create({});
