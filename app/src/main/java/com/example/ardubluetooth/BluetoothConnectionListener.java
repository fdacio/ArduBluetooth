package com.example.ardubluetooth;

import android.bluetooth.BluetoothDevice;

public interface BluetoothConnectionListener {
    void setConnected(BluetoothDevice device);
    void setDesconect();
}
