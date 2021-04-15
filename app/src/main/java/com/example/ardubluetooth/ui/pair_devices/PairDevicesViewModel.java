package com.example.ardubluetooth.ui.pair_devices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PairDevicesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PairDevicesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}