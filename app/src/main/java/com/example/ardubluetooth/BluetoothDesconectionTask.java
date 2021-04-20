package com.example.ardubluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public class BluetoothDesconectionTask extends AsyncTask<Void, Void, Void> {

    private BluetoothConnectionTask mmTask;
    private BluetoothConnectionListener mmListener;
    private Context mmContext;
    private ProgressDialog progressDialog;


    public BluetoothDesconectionTask(BluetoothConnectionTask task, BluetoothConnectionListener listener, Context context) {
        mmTask = task;
        mmListener = listener;
        mmContext = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            mmTask.desconnect();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mmContext, "Bluetooth", "Aguarde, desconectando ...");
    }

    @Override
    protected void onPostExecute(Void v) {

        mmListener.setDesconect();
        progressDialog.dismiss();
    }
}
