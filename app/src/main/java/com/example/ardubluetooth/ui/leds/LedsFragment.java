package com.example.ardubluetooth.ui.leds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ardubluetooth.BluetoothConnection;
import com.example.ardubluetooth.BluetoothInstance;
import com.example.ardubluetooth.R;

public class LedsFragment extends Fragment {

    private View root;
    private Spinner spinnerPinRed;
    private Spinner spinnerPinYellow;
    private Spinner spinnerPinGreen;
    private Switch switchRed;
    private Switch switchYellow;
    private Switch switchGreen;
    private ImageButton imageButtonLedRed;
    private ImageButton imageButtonLedYellow;
    private ImageButton imageButtonLedGreen;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.getBoolean("STATE_SAVE")) {
            return root;
        }

        root = inflater.inflate(R.layout.fragment_leds, container, false);

        spinnerPinRed = root.findViewById(R.id.spinnerPinRed);
        spinnerPinYellow = root.findViewById(R.id.spinnerPinYellow);
        spinnerPinGreen = root.findViewById(R.id.spinnerPinGreen);
        switchRed = root.findViewById(R.id.switchRed);
        switchYellow = root.findViewById(R.id.switchYellow);
        switchGreen = root.findViewById(R.id.switchGreen);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPinRed.setAdapter(adapter);
        spinnerPinYellow.setAdapter(adapter);
        spinnerPinGreen.setAdapter(adapter);

        spinnerPinRed.setSelection(0);
        spinnerPinYellow.setSelection(1);
        spinnerPinGreen.setSelection(2);

        imageButtonLedRed = root.findViewById(R.id.servoMoto);
        imageButtonLedRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRed.setChecked(!switchRed.isChecked());
                if (!switchRed.isChecked()) {
                    imageButtonLedRed.setImageResource(R.drawable.led_red);
                } else {
                    imageButtonLedRed.setImageResource(R.drawable.led_red_low);
                }

                int pino = Integer.parseInt(spinnerPinRed.getSelectedItem().toString());
                int sinal = (switchRed.isChecked()) ? 0 : 1;

                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();
                if (bluetoothConnection != null) {
                    bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());
                }
            }
        });

        imageButtonLedYellow = root.findViewById(R.id.ledYellow);
        imageButtonLedYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchYellow.setChecked(!switchYellow.isChecked());
                if (!switchYellow.isChecked()) {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow);
                } else {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow_low);
                }

                int pino = Integer.parseInt(spinnerPinYellow.getSelectedItem().toString());
                int sinal = (switchYellow.isChecked()) ? 0 : 1;

                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();
                if (bluetoothConnection != null) {
                    bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());
                }
            }
        });

        imageButtonLedGreen = root.findViewById(R.id.ledGreen);
        imageButtonLedGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGreen.setChecked(!switchGreen.isChecked());
                if (!switchGreen.isChecked()) {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green);
                } else {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green_low);
                }

                int pino = Integer.parseInt(spinnerPinGreen.getSelectedItem().toString());
                int sinal = (switchGreen.isChecked()) ? 0 : 1;

                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();
                if (bluetoothConnection != null) {
                    bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());
                }
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("STATE_SAVE", true);
        super.onSaveInstanceState(outState);
    }


}