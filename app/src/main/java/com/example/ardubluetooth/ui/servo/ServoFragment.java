package com.example.ardubluetooth.ui.servo;

import android.media.RemoteController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ardubluetooth.BluetoothConnection;
import com.example.ardubluetooth.BluetoothInstance;
import com.example.ardubluetooth.R;

public class ServoFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_servo, container, false);

        Spinner spinnerPinServo = root.findViewById(R.id.spinnerPinServo);
        SeekBar seekBarAngulo = root.findViewById(R.id.seekBarAngulo);
        TextView textViewAngulo = root.findViewById(R.id.textViewAngulo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPinServo.setAdapter(adapter);
        spinnerPinServo.setSelection(3);

        seekBarAngulo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAngulo.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        ImageButton imageButtonServo = root.findViewById(R.id.imageButtonServo);
        imageButtonServo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pino = Integer.parseInt(spinnerPinServo.getSelectedItem().toString());
                int angulo = seekBarAngulo.getProgress();
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();
                if (bluetoothConnection != null) {
                    bluetoothConnection.write(String.valueOf((pino * 1000) + angulo).getBytes());
                }
            }
        });

        return root;
    }
}
