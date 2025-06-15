import React, {useCallback, useRef, useState} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
} from 'react-native';
import {GestureHandlerRootView} from 'react-native-gesture-handler';
import ChatIndex from '../../components/ChatIndex';

const TITLES = [
  'Record the dismissible tutorial 🎥',
  'Leave 👍🏼 to the video',
  'Check YouTube comments',
  'Subscribe to the channel 🚀',
  'Leave a ⭐️ on the GitHub Repo',
];

const TASKS = TITLES.map((title, index) => ({title, index}));

const BACKGROUND_COLOR = '#FAFBFF';

const Home = ({navigation}) => {
  const [tasks, setTasks] = useState(TASKS);
  const scrollRef = useRef(null);

  const onDismiss = useCallback(task => {
    setTasks(prevTasks => prevTasks.filter(item => item.index !== task.index));
  }, []);

  return (
    <GestureHandlerRootView style={{flex: 1}}>
      <SafeAreaView style={styles.container}>
        <StatusBar barStyle="dark-content" />
        <Text style={styles.title}>Chats</Text>
        <ScrollView ref={scrollRef} style={styles.scrollView}>
          {tasks.map(task => (
            <ChatIndex
              key={task.index}
              simultaneousHandlers={scrollRef}
              task={task}
              onDismiss={onDismiss}
              onTap={() => navigation.navigate('Chat', {task})}
            />
          ))}
        </ScrollView>
      </SafeAreaView>
    </GestureHandlerRootView>
  );
};

export default Home;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: BACKGROUND_COLOR,
  },
  scrollView: {
    flex: 1,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginVertical: 20,
    marginLeft: 20,
  },
});
