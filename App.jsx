import {NativeModules, Button, View, Text} from 'react-native';
import {useEffect} from 'react';

const {Ads} = NativeModules;

const initializeAdMob = async () => {
  try {
    const result = await Ads.initializeAdMob();
    console.log(result);
  } catch (error) {
    console.error('AdMob başlatma hatası:', error);
  }
};

const loadInterstitialAd = async () => {
  try {
    const result = await Ads.loadInterstitialAd(
      'ca-app-pub-3940256099942544/1033173712',
    );
    console.log(result);
  } catch (error) {
    console.error('Geçiş reklamı yükleme hatası:', error);
  }
};

const showInterstitialAd = async () => {
  try {
    const result = await Ads.showInterstitialAd();
    console.log(result);
  } catch (error) {
    console.error('Geçiş reklamı gösterme hatası:', error);
  }
};

const loadRewardedAd = async () => {
  try {
    const result = await Ads.loadRewardedAd(
      'ca-app-pub-3940256099942544/5224354917',
    );
    console.log(result);
  } catch (error) {
    console.error('Ödüllü reklam yükleme hatası:', error);
  }
};

const showRewardedAd = async () => {
  try {
    const result = await Ads.showRewardedAd();
    console.log(result);
  } catch (error) {
    console.error('Ödüllü reklam gösterme hatası:', error);
  }
};

export default function App() {
  useEffect(() => {
    initializeAdMob();
    loadInterstitialAd();
    loadRewardedAd();
  }, []);

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Text>AdMob Test Uygulaması</Text>
      <Button title="Geçiş Reklamını Göster" onPress={showInterstitialAd} />
      <Button title="Ödüllü Reklamı Göster" onPress={showRewardedAd} />
    </View>
  );
}
