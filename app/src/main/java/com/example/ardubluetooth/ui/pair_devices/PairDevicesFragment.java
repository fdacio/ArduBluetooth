package com.example.ardubluetooth.ui.pair_devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ardubluetooth.DevicesBluetoothAdapter;
import com.example.ardubluetooth.DevicesFounded;
import com.example.ardubluetooth.MainActivity;
import com.example.ardubluetooth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairDevicesFragment extends Fragment {

    private BluetoothAdapter bluetoothAdapter;
    private PairDevicesViewModel pairDevicesViewModel;
    private final static int REQUEST_ENABLE_BLUETOOTH = 1;
    private List<DevicesFounded> listDevicesFounded = new ArrayList<>();
    private RecyclerView recyclerViewDevices;
    private BroadcastReceiver receiver;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pairDevicesViewModel = new ViewModelProvider(this).get(PairDevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pair_devices, container, false);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    DevicesFounded devicesFounded = new DevicesFounded(deviceName, deviceHardwareAddress);
                    listDevicesFounded.add(devicesFounded);
                }
                int state;
                switch(action)
                {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                        if (state == BluetoothAdapter.STATE_OFF)
                        {
                            Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show();
                            Log.d("BroadcastActions", "Bluetooth is off");
                        }
                        else if (state == BluetoothAdapter.STATE_TURNING_OFF)
                        {
                            Toast.makeText(context, "Bluetooth is turning off", Toast.LENGTH_SHORT).show();
                            Log.d("BroadcastActions", "Bluetooth is turning off");
                        }
                        else if(state == BluetoothAdapter.STATE_ON)
                        {
                            Log.d("BroadcastActions", "Bluetooth is on");
                        }
                        break;

                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        bluetoothAdapter = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Toast.makeText(context, "Connected to "+bluetoothAdapter.getName(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("BroadcastActions", "Connected to "+bluetoothAdapter.getName());
                        break;

                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        bluetoothAdapter = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Toast.makeText(context, "Disconnected from "+bluetoothAdapter.getName(),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        recyclerViewDevices = root.findViewById(R.id.recycleViewDevices);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        habilitaBluetooth();
        recyclerViewDevices.setHasFixedSize(true);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(root.getContext()));

        Button btnSearchDevices  = root.findViewById(R.id.buttonSearchDevices);
        btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                getActivity().registerReceiver(receiver, filter);
                if (bluetoothAdapter.startDiscovery()) {
                    recyclerViewDevices.setAdapter(new DevicesBluetoothAdapter(listDevicesFounded));
                }
            }
        });

        setDevicesDiscovered();

        return root;
    }

    private void habilitaBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

    }

    private void setDevicesDiscovered() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> devices = new ArrayList<>();
        for (BluetoothDevice bt : pairedDevices) {
            DevicesFounded devicesFounded = new DevicesFounded(bt.getName(), bt.getAddress());
            listDevicesFounded.add(devicesFounded);
        }
        DevicesFounded devicesFounded = new DevicesFounded("OUTRO", "0X27");
        listDevicesFounded.add(devicesFounded);
        recyclerViewDevices.setAdapter(new DevicesBluetoothAdapter(listDevicesFounded));
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
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

}