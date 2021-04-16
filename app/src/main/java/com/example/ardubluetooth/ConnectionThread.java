package com.example.ardubluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectionThread extends Thread {

    private static ConnectionThread instance;

    private BluetoothSocket btSocket = null;
    private BluetoothServerSocket btServerSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private String btDevAddress = null;
    private String myUUID = "00001101-0000-1000-8000-00805F9B34FB";

    private boolean server;
    private boolean running = false;
    private boolean isConnected = false;


    public static ConnectionThread getInstance() {
        if (instance == null) {
            instance = new ConnectionThread();
        }
        return instance;
    }

    public static ConnectionThread getInstance(String btDevAddress) {
        if (instance == null) {
            instance = new ConnectionThread(btDevAddress);

        }
        return instance;
    }

    private ConnectionThread() {
        this.server = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //myUUID = btAdapter.getAddress();
    }

    private ConnectionThread(String btDevAddress) {
        this.server = false;
        this.btDevAddress = btDevAddress;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //myUUID = btAdapter.getAddress();
    }


    public void run() {
        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.server) {
            try {
                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("LEDS", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();
                if (btSocket != null) {
                    btServerSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));
                btAdapter.cancelDiscovery();
                if (btSocket != null) {
                    btSocket.connect();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (btSocket != null) {
            this.isConnected = true;
            try {
                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();
                while (running) {
                    byte[] buffer = new byte[1024];
                    int bytes;
                    int bytesRead = -1;
                    do {
                        bytes = input.read(buffer, bytesRead + 1, 1);
                        bytesRead += bytes;
                    } while (buffer[bytesRead] != '\n');
                }

            } catch (IOException e) {
                e.printStackTrace();
                this.isConnected = false;
            }
        }

    }

    public void write(byte[] data) {
        if (output != null) {
            try {
                output.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        try {
            running = false;
            this.isConnected = false;
            btServerSocket.close();
            btSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        this.isConnected = false;
    }

    public boolean isConnected() {
        return this.isConnected;
    }
}