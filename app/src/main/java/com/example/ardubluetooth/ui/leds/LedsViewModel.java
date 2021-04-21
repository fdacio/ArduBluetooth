package com.example.ardubluetooth.ui.leds;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LedsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LedsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Leds fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}