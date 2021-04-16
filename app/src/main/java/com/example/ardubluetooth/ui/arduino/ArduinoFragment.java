package com.example.ardubluetooth.ui.arduino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ardubluetooth.ConnectionThread;
import com.example.ardubluetooth.MainActivity;
import com.example.ardubluetooth.R;

public class ArduinoFragment extends Fragment {

    private ArduinoViewModel arduinoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        arduinoViewModel = new ViewModelProvider(this).get(ArduinoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_arduino, container, false);

        Spinner spinnerPinRed = root.findViewById(R.id.spinnerPinRed);
        Spinner spinnerPinYellow = root.findViewById(R.id.spinnerPinYellow);
        Spinner spinnerPinGreen = root.findViewById(R.id.spinnerPinGreen);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arduinoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                spinnerPinRed.setAdapter(adapter);
                spinnerPinRed.setSelection(0);
                spinnerPinYellow.setAdapter(adapter);
                spinnerPinYellow.setSelection(1);
                spinnerPinGreen.setAdapter(adapter);
                spinnerPinGreen.setSelection(2);
            }
        });

        ImageButton imageButtonLedRed = root.findViewById(R.id.ledRed);
        imageButtonLedRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionThread.getInstance().write("1".getBytes());
            }
        });

        ImageButton imageButtonLedYellow = root.findViewById(R.id.ledYellow);
        imageButtonLedYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionThread.getInstance().write("2".getBytes());
            }
        });

        ImageButton imageButtonLedGreen = root.findViewById(R.id.ledGreen);
        imageButtonLedGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionThread.getInstance().write("3".getBytes());
            }
        });

        return root;
    }

}