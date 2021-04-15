package com.example.ardubluetooth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DevicesBluetoothAdapter extends RecyclerView.Adapter<DevicesBluetoothAdapter.ViewHolder> {

    private List<DevicesFounded> deviceList;

    public DevicesBluetoothAdapter(List<DevicesFounded> deviceList) {
        this.deviceList = deviceList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewDeviceName;
        private final TextView textViewDeviceAddress;

        public ViewHolder(View view) {
            super(view);
            textViewDeviceName = (TextView) view.findViewById(R.id.textViewDeviceName);
            textViewDeviceAddress = (TextView) view.findViewById(R.id.textViewDeviceAddress);
        }

        public TextView getTextViewDeviceName() {
            return textViewDeviceName;
        }

        public TextView getTextViewDeviceAddress() {
            return textViewDeviceAddress;
        }
    }

    @NonNull
    @Override
    public DevicesBluetoothAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesBluetoothAdapter.ViewHolder holder, int position) {
        holder.getTextViewDeviceName().setText(deviceList.get(position).getName());
        holder.getTextViewDeviceAddress().setText(deviceList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
