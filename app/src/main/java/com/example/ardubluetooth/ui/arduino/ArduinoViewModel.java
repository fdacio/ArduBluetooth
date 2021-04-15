package com.example.ardubluetooth.ui.arduino;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArduinoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArduinoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}