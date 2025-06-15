import React from 'react';
import {Dimensions, StyleSheet, Text, View} from 'react-native';
import {Gesture, GestureDetector} from 'react-native-gesture-handler';
import Animated, {
  runOnJS,
  useAnimatedStyle,
  useSharedValue,
  withTiming,
} from 'react-native-reanimated';
import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';

const LIST_ITEM_HEIGHT = 70;
const {width: SCREEN_WIDTH} = Dimensions.get('window');
const TRANSLATE_X_THRESHOLD = SCREEN_WIDTH * 0.15;

const ChatIndex = ({task, onDismiss, simultaneousHandlers, onTap}) => {
  const translateX = useSharedValue(0);
  const itemHeight = useSharedValue(LIST_ITEM_HEIGHT);
  const marginVertical = useSharedValue(10);
  const opacity = useSharedValue(1);

  const panGesture = Gesture.Pan()
    .minDistance(10)
    .simultaneousWithExternalGesture(simultaneousHandlers)
    .onUpdate(event => {
      translateX.value = event.translationX;
    })
    .onEnd(() => {
      const shouldBeDismissed = translateX.value < -TRANSLATE_X_THRESHOLD; // Sola kaydırma
      if (shouldBeDismissed) {
        translateX.value = withTiming(-SCREEN_WIDTH, {duration: 300});
        itemHeight.value = withTiming(0, {duration: 300});
        marginVertical.value = withTiming(0, {duration: 300});
        opacity.value = withTiming(0, {duration: 300}, finished => {
          if (finished && onDismiss) {
            runOnJS(onDismiss)(task);
          }
        });
      } else {
        translateX.value = withTiming(0, {duration: 300});
      }
    });

  const tapGesture = Gesture.Tap()
    .maxDistance(10)
    .maxDuration(250)
    .simultaneousWithExternalGesture(simultaneousHandlers)
    .onEnd(() => {
      if (onTap) {
        runOnJS(onTap)();
      }
    });

  const rStyle = useAnimatedStyle(() => ({
    transform: [{translateX: translateX.value}],
  }));

  const rIconContainerStyle = useAnimatedStyle(() => ({
    opacity: translateX.value < -TRANSLATE_X_THRESHOLD / 2 ? 1 : 0,
  }));

  const rTaskContainerStyle = useAnimatedStyle(() => ({
    height: itemHeight.value,
    marginVertical: marginVertical.value,
    opacity: opacity.value,
  }));

  return (
    <Animated.View style={[styles.taskContainer, rTaskContainerStyle]}>
      <View style={styles.iconContainerWrapper}>
        <Animated.View style={[styles.iconContainer, rIconContainerStyle]}>
          <FontAwesome5
            name="trash-alt"
            size={LIST_ITEM_HEIGHT * 0.4}
            color="red"
          />
        </Animated.View>
      </View>
      <GestureDetector gesture={Gesture.Simultaneous(tapGesture, panGesture)}>
        <Animated.View style={[styles.task, rStyle]}>
          <Text style={styles.taskTitle}>{task.title}</Text>
        </Animated.View>
      </GestureDetector>
    </Animated.View>
  );
};

export default ChatIndex;

const styles = StyleSheet.create({
  taskContainer: {
    width: '100%',
    alignItems: 'center',
  },
  task: {
    width: '90%',
    height: LIST_ITEM_HEIGHT,
    justifyContent: 'center',
    paddingLeft: 20,
    backgroundColor: 'white',
    borderRadius: 10,
    shadowOpacity: 0.08,
    shadowOffset: {width: 0, height: 20},
    shadowRadius: 10,
    elevation: 5,
    zIndex: 1,
  },
  taskTitle: {
    fontSize: 16,
  },
  iconContainerWrapper: {
    position: 'absolute',
    width: '100%',
    height: LIST_ITEM_HEIGHT,
    zIndex: 0,
  },
  iconContainer: {
    height: LIST_ITEM_HEIGHT,
    width: LIST_ITEM_HEIGHT,
    position: 'absolute',
    right: '2%',
    justifyContent: 'center',
    alignItems: 'center',
  },
});
