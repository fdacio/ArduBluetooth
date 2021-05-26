package com.example.ardubluetooth.ui.servo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ardubluetooth.BluetoothConnection;
import com.example.ardubluetooth.BluetoothInstance;
import com.example.ardubluetooth.R;

public class ServoFragment extends Fragment {

    private Spinner spinnerPinServo;
    private SeekBar seekBarAngulo;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_servo, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        spinnerPinServo = root.findViewById(R.id.spinnerPinServo);
        seekBarAngulo = root.findViewById(R.id.seekBarAngulo);
        TextView textViewAngulo = root.findViewById(R.id.textViewAngulo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPinServo.setAdapter(adapter);

        spinnerPinServo.setSelection(sharedPref.getInt("PIN_SERVO", 0));
        seekBarAngulo.setProgress(sharedPref.getInt("ANGULO_SERVO", 0));
        textViewAngulo.setText(String.valueOf(sharedPref.getInt("ANGULO_SERVO", 0)));

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

                if (!BluetoothInstance.isConnected()) {
                    Toast.makeText(mContext, "Não há dispositivo conectado", Toast.LENGTH_LONG).show();
                    return;
                }
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();

                int pino = Integer.parseInt(spinnerPinServo.getSelectedItem().toString());
                int angulo = seekBarAngulo.getProgress();

                bluetoothConnection.write(String.valueOf((pino * 1000) + angulo).getBytes());

            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PIN_SERVO", spinnerPinServo.getSelectedItemPosition());
        editor.putInt("ANGULO_SERVO", seekBarAngulo.getProgress());
        editor.commit();
        super.onDestroy();
    }
}
