import {
  NativeModules,
  Button,
  View,
  Text,
  Alert,
  DeviceEventEmitter,
} from 'react-native';
import {useEffect, useState} from 'react';

const {Ads} = NativeModules;

if (!Ads) {
  Alert.alert(
    'Hata',
    'Ads modülü yüklenemedi. Native modül doğru şekilde bağlanmış mı?',
  );
  console.error('Ads modülü undefined');
}

const initializeAdMob = async () => {
  if (!Ads) return;
  try {
    const result = await Ads.initializeAdMob();
    console.log(result);
  } catch (error) {
    console.error('AdMob başlatma hatası:', error);
  }
};

const loadInterstitialAd = async setAdLoaded => {
  if (!Ads) return;
  try {
    const result = await Ads.loadInterstitialAd(
      'ca-app-pub-3940256099942544/1033173712',
    );
    console.log(result);
    setAdLoaded(true);
  } catch (error) {
    console.error('Geçiş reklamı yükleme hatası:', error.stack);
    Alert.alert('Hata', 'Geçiş reklamı yüklenemedi: ' + error.message);
    setAdLoaded(false);
  }
};

const showInterstitialAd = async () => {
  if (!Ads) return;
  try {
    const result = await Ads.showInterstitialAd();
    console.log(result);
  } catch (error) {
    console.error('Geçiş reklamı gösterme hatası:', error.stack);
    Alert.alert('Hata', error.message);
  }
};

const loadRewardedAd = async setAdLoaded => {
  if (!Ads) return;
  try {
    const result = await Ads.loadRewardedAd(
      'ca-app-pub-3940256099942544/5224354917',
    );
    console.log(result);
    setAdLoaded(true);
  } catch (error) {
    console.error('Ödüllü reklam yükleme hatası:', error);
    Alert.alert('Hata', 'Ödüllü reklam yüklenemedi: ' + error.message);
    setAdLoaded(false);
  }
};

const showRewardedAd = async () => {
  if (!Ads) return;
  try {
    const result = await Ads.showRewardedAd();
    console.log(result);
    console.log('first');
    loadRewardedAd();
    set;
  } catch (error) {
    console.error('Ödüllü reklam gösterme hatası:', error);
    Alert.alert('Hata', error.message);
  }
};

export default function AdMob() {
  const [interstitialAdLoaded, setInterstitialAdLoaded] = useState(false);
  const [rewardedAdLoaded, setRewardedAdLoaded] = useState(false);

  useEffect(() => {
    initializeAdMob();
    loadInterstitialAd(setInterstitialAdLoaded);
    loadRewardedAd(setRewardedAdLoaded);

    const subscription = DeviceEventEmitter.addListener(
      'onInterstitialClosed',
      () => {
        setInterstitialAdLoaded(false);
        loadInterstitialAd(setInterstitialAdLoaded); // Yeni reklam yükle
      },
    );
    return () => subscription.remove();
  }, []);

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Text>AdMob Test Uygulaması</Text>
      <Button
        title="Geçiş Reklamını Göster"
        onPress={showInterstitialAd}
        disabled={!interstitialAdLoaded}
      />
      <Button
        title="Ödüllü Reklamı Göster"
        onPress={showRewardedAd}
        disabled={!rewardedAdLoaded}
      />
    </View>
  );
}
