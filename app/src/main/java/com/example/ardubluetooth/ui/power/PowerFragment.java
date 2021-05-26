package com.example.ardubluetooth.ui.power;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ardubluetooth.BluetoothConnection;
import com.example.ardubluetooth.BluetoothInstance;
import com.example.ardubluetooth.R;


public class PowerFragment extends Fragment {

    private View root;
    private Spinner spinnerPinPower;
    private Switch switchPower;
    private ImageButton imageButtonPower;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_power, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        spinnerPinPower = root.findViewById(R.id.spinnerPinPower);
        switchPower = root.findViewById(R.id.switchPower);
        imageButtonPower = root.findViewById(R.id.imageButtonPower);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPinPower.setAdapter(adapter);

        spinnerPinPower.setSelection(sharedPref.getInt("PIN_POWER", 0));

        imageButtonPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!BluetoothInstance.isConnected()) {
                    Toast.makeText(mContext, "Não há dispositivo conectado", Toast.LENGTH_LONG).show();
                    return;
                }
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();

                switchPower.setChecked(!switchPower.isChecked());
                if (switchPower.isChecked()) {
                    imageButtonPower.setImageResource(R.drawable.light_low);
                } else {
                    imageButtonPower.setImageResource(R.drawable.light_high);
                }

                int pino = Integer.parseInt(spinnerPinPower.getSelectedItem().toString());
                int sinal = (switchPower.isChecked()) ? 1 : 0;

                bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());

            }

        });

        return root;

    }

    public void onDestroy() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PIN_POWER", spinnerPinPower.getSelectedItemPosition());
        editor.putInt("SINAL_POWER", (switchPower.isChecked()) ? 1 : 0);
        editor.commit();
        super.onDestroy();
    }
}