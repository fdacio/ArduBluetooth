package br.com.daciosoftware.ardubluetooth.ui.leds;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import br.com.daciosoftware.ardubluetooth.BluetoothConnection;
import br.com.daciosoftware.ardubluetooth.BluetoothInstance;
import com.daciosoftware.ardubluetooth.R;

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
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_leds, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        spinnerPinRed = root.findViewById(R.id.spinnerPinRed);
        spinnerPinYellow = root.findViewById(R.id.spinnerPinYellow);
        spinnerPinGreen = root.findViewById(R.id.spinnerPinGreen);
        switchRed = root.findViewById(R.id.switchRed);
        switchYellow = root.findViewById(R.id.switchYellow);
        switchGreen = root.findViewById(R.id.switchGreen);
        imageButtonLedRed = root.findViewById(R.id.ledRed);
        imageButtonLedYellow = root.findViewById(R.id.ledYellow);
        imageButtonLedGreen = root.findViewById(R.id.ledGreen);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.pins_arduino, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPinRed.setAdapter(adapter);
        spinnerPinYellow.setAdapter(adapter);
        spinnerPinGreen.setAdapter(adapter);

        spinnerPinRed.setSelection(sharedPref.getInt("PIN_LED_RED", 0));
        spinnerPinYellow.setSelection(sharedPref.getInt("PIN_LED_YELLOW", 1));
        spinnerPinGreen.setSelection(sharedPref.getInt("PIN_LED_GREEN", 2));

        switchRed.setChecked(sharedPref.getInt("SINAL_LED_RED", 0) == 1);
        switchYellow.setChecked(sharedPref.getInt("SINAL_LED_YELLOW", 0) == 1);
        switchGreen.setChecked(sharedPref.getInt("SINAL_LED_GREEN", 0) == 1);

        if (!switchRed.isChecked()) {
            imageButtonLedRed.setImageResource(R.drawable.led_red);
        } else {
            imageButtonLedRed.setImageResource(R.drawable.led_red_low);
        }
        if (!switchYellow.isChecked()) {
            imageButtonLedYellow.setImageResource(R.drawable.led_yellow);
        } else {
            imageButtonLedYellow.setImageResource(R.drawable.led_yellow_low);
        }
        if (!switchGreen.isChecked()) {
            imageButtonLedGreen.setImageResource(R.drawable.led_green);
        } else {
            imageButtonLedGreen.setImageResource(R.drawable.led_green_low);
        }

        imageButtonLedRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!BluetoothInstance.isConnected()) {
                    Toast.makeText(mContext, "Não há dispositivo conectado", Toast.LENGTH_LONG).show();
                    return;
                }
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();

                switchRed.setChecked(!switchRed.isChecked());
                if (!switchRed.isChecked()) {
                    imageButtonLedRed.setImageResource(R.drawable.led_red);
                } else {
                    imageButtonLedRed.setImageResource(R.drawable.led_red_low);
                }

                int pino = Integer.parseInt(spinnerPinRed.getSelectedItem().toString());
                int sinal = (switchRed.isChecked()) ? 0 : 1;

                bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());

            }
        });

        imageButtonLedYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!BluetoothInstance.isConnected()) {
                    Toast.makeText(mContext, "Não há dispositivo conectado", Toast.LENGTH_LONG).show();
                    return;
                }
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();

                switchYellow.setChecked(!switchYellow.isChecked());
                if (!switchYellow.isChecked()) {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow);
                } else {
                    imageButtonLedYellow.setImageResource(R.drawable.led_yellow_low);
                }

                int pino = Integer.parseInt(spinnerPinYellow.getSelectedItem().toString());
                int sinal = (switchYellow.isChecked()) ? 0 : 1;

                bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());

            }
        });

        imageButtonLedGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!BluetoothInstance.isConnected()) {
                    Toast.makeText(mContext, "Não há dispositivo conectado", Toast.LENGTH_LONG).show();
                    return;
                }
                BluetoothConnection bluetoothConnection = BluetoothInstance.getInstance();

                switchGreen.setChecked(!switchGreen.isChecked());
                if (!switchGreen.isChecked()) {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green);
                } else {
                    imageButtonLedGreen.setImageResource(R.drawable.led_green_low);
                }

                int pino = Integer.parseInt(spinnerPinGreen.getSelectedItem().toString());
                int sinal = (switchGreen.isChecked()) ? 0 : 1;

                bluetoothConnection.write(String.valueOf((pino * 10) + sinal).getBytes());

            }
        });


        return root;
    }

    @Override
    public void onDestroy() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PIN_LED_RED", spinnerPinRed.getSelectedItemPosition());
        editor.putInt("PIN_LED_YELLOW", spinnerPinYellow.getSelectedItemPosition());
        editor.putInt("PIN_LED_GREEN", spinnerPinGreen.getSelectedItemPosition());
        editor.putInt("SINAL_LED_RED", (switchRed.isChecked()) ? 1 : 0);
        editor.putInt("SINAL_LED_YELLOW", (switchYellow.isChecked()) ? 1 : 0);
        editor.putInt("SINAL_LED_GREEN", (switchGreen.isChecked()) ? 1 : 0);
        editor.commit();
        super.onDestroy();
    }

}