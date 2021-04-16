package com.example.ardubluetooth.ui.pair_devices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ardubluetooth.ConnectionThread;
import com.example.ardubluetooth.DevicesBluetoothAdapter;
import com.example.ardubluetooth.DevicesFounded;
import com.example.ardubluetooth.MainActivity;
import com.example.ardubluetooth.R;
import com.example.ardubluetooth.ui.arduino.ArduinoFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairDevicesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_PERMISSION_BLUETOOTH = 2;
    private BluetoothAdapter bluetoothAdapter;
    private PairDevicesViewModel pairDevicesViewModel;
    private final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private ArrayList<BluetoothDevice> listDevicesFounded = new ArrayList<BluetoothDevice>();
    private ListView listViewDevices;
    private BroadcastReceiver receiver;
    private ProgressDialog mProgressDlg;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pairDevicesViewModel = new ViewModelProvider(this).get(PairDevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pair_devices, container, false);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    listDevicesFounded = new ArrayList<>();
                    mProgressDlg.show();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    mProgressDlg.dismiss();
                    DevicesBluetoothAdapter devicesBluetoothAdapter = new DevicesBluetoothAdapter(getContext());
                    devicesBluetoothAdapter.setData(listDevicesFounded);
                    listViewDevices.setAdapter(devicesBluetoothAdapter);
                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listDevicesFounded.add(device);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().getApplicationContext().registerReceiver(receiver, filter);

        listViewDevices = root.findViewById(R.id.listViewDevices);
        listViewDevices.setOnItemClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mProgressDlg = new ProgressDialog(getContext());

        mProgressDlg.setMessage("Aguarde, procurando...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                bluetoothAdapter.cancelDiscovery();
            }
        });

        habilitaBluetooth();
        permCoarse();

        Button btnSearchDevices = root.findViewById(R.id.buttonSearchDevices);
        btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothAdapter.startDiscovery();
            }
        });

        return root;
    }

    private void permCoarse() {
        // Handling permissions.
        if (ContextCompat.checkSelfPermission(getContext(),  Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Not to annoy user.
                Toast.makeText(getContext(), "Permissão de localização deve ser concedida.", Toast.LENGTH_SHORT).show();
            } else {
                // Request permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_BLUETOOTH);
            }
        }
    }

    private void habilitaBluetooth() {

        if (bluetoothAdapter == null) {
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void setDevicesDiscovered() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices) {
            listDevicesFounded.add(bt);
        }
        DevicesBluetoothAdapter devicesBluetoothAdapter = new DevicesBluetoothAdapter(getContext());
        devicesBluetoothAdapter.setData(listDevicesFounded);
        listViewDevices.setAdapter(devicesBluetoothAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_ENABLE_BLUETOOTH) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            String bluetoothDeviceName = bluetoothAdapter.getName();
            String bluetoothDeviceAdrress = bluetoothAdapter.getAddress();
            Toast.makeText(this.getContext(), bluetoothDeviceName + ":" + bluetoothDeviceAdrress, Toast.LENGTH_LONG).show();

            Intent discoverableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivity(discoverableBluetoothIntent);

        }
    }

    @Override
    public void onPause() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        getContext().getApplicationContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    //Pareamento
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listDevicesFounded.get(position).getAddress();
        String deviceAddress = listDevicesFounded.get(position).getAddress();
        Toast.makeText(getContext(), deviceAddress, Toast.LENGTH_SHORT).show();

        ConnectionThread connectionThread =  ConnectionThread.getInstance(deviceAddress);
        connectionThread.start();
        try {
            Thread.sleep(1000);
        } catch (Exception E) {
            E.printStackTrace();
        }

    }

}