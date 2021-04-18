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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ardubluetooth.BluetoothConnection;
import com.example.ardubluetooth.ConnectionThread;
import com.example.ardubluetooth.R;

public class LedsFragment extends Fragment {

    private LedsViewModel arduinoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        arduinoViewModel = new ViewModelProvider(this).get(LedsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_leds, container, false);

        Spinner spinnerPinRed = root.findViewById(R.id.spinnerPinRed);
        Spinner spinnerPinYellow = root.findViewById(R.id.spinnerPinYellow);
        Spinner spinnerPinGreen = root.findViewById(R.id.spinnerPinGreen);
        Switch switchRed = root.findViewById(R.id.switchRed);
        Switch switchYellow = root.findViewById(R.id.switchYellow);
        Switch switchGreen = root.findViewById(R.id.switchGreen);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arduinoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        spinnerPinRed.setAdapter(adapter);
        spinnerPinRed.setSelection(0);
        spinnerPinYellow.setAdapter(adapter);
        spinnerPinYellow.setSelection(1);
        spinnerPinGreen.setAdapter(adapter);
        spinnerPinGreen.setSelection(2);


        ImageButton imageButtonLedRed = root.findViewById(R.id.ledRed);
        imageButtonLedRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRed.setChecked(!switchRed.isChecked());
                if(switchRed.isChecked()) {
                    imageButtonLedRed.setImageResource(R.drawable.led_red);
                } else {
                    imageButtonLedRed.setImageResource(R.drawable.led_red_low);
                }

                String pino = spinnerPinRed.getSelectedItem().toString();
                boolean sinal = switchRed.isChecked();
                BluetoothConnection.getInstance().write("1".getBytes());
            }
        });

        ImageButton imageButtonLedYellow = root.findViewById(R.id.ledYellow);
        imageButtonLedYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchYellow.setChecked(!switchYellow.isChecked());
                if(switchYellow.isChecked()) {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow);
                } else {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow_low);
                }

                String pino = spinnerPinYellow.getSelectedItem().toString();
                boolean sinal = switchYellow.isChecked();
                BluetoothConnection.getInstance().write("2".getBytes());
            }
        });

        ImageButton imageButtonLedGreen = root.findViewById(R.id.ledGreen);
        imageButtonLedGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGreen.setChecked(!switchGreen.isChecked());
                if(switchGreen.isChecked()) {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green);
                } else {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green_low);
                }

                String pino = spinnerPinGreen.getSelectedItem().toString();
                boolean sinal = switchGreen.isChecked();
                BluetoothConnection.getInstance().write("3".getBytes());
            }
        });

        return root;
    }

}