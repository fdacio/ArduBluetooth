package com.example.ardubluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnection extends Thread {

    private static BluetoothConnection instance;
    private BluetoothSocket mmSocket;
    private OutputStream mmOutStream;
    private BluetoothAdapter mAdapter;
    private boolean connected = false;

    public static BluetoothConnection getInstance() {
        if (instance == null) {
            instance = new BluetoothConnection();
        }
        return instance;
    }

    public static BluetoothConnection getInstance(BluetoothDevice device) {
        if (instance == null) {
            instance = new BluetoothConnection(device);

        }
        return instance;
    }

    private BluetoothConnection(BluetoothDevice device) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
        OutputStream tmpOut = null;
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmOutStream = tmpOut;
    }

    private BluetoothConnection() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        OutputStream tmpOut = null;
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmOutStream = tmpOut;
    }

    public void run() {
        mAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
            try {
                mmSocket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

}

