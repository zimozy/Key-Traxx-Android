package com.example.keytraxx;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivationActivity extends AppCompatActivity {
    //  CONSTANTS   //
    private static final int REQUEST_ENABLE_BT = 1; // used by startActivityForResult and sent to @Override onActivityResult as requestCode (first parameter)
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private static final long SCAN_PERIOD = 2000;
    private static final String TAG = "BTLEKeyTraxx";

    //  OBJECTS  //
    private BluetoothAdapter bluetoothAdapter;
//    private LeDeviceListAdapter mLeDeviceListAdapter;

    //  OTHERS  //
    private boolean areScanning;
    private Handler mHandler;
    private TextView tvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        tvAmount = findViewById(R.id.tvAmount);

        //initialie and get bluetooth adapter
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        //check bt is enabled
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //check for location permission in API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //perm granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

                    mHandler = new Handler();

                    scanLeDevice(true);

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    //perm denied
                }
                break;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    areScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            areScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);

        } else {
            areScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }



    // Device scan callback.
    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

                    if (rssi > -50) {
                        Log.d(TAG, device.getAddress() + " at strength: " + String.valueOf(rssi));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAmount.append(String.valueOf(rssi).concat("\n"));
                            }
                        });
                    }
                }
            };

    public void createToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String var_dump(Object o) {
        Method[] fields= o.getClass().getMethods();
//        Field[] fields = o.getClass().getDeclaredFields();
        String result = "";

        for (int i=0; i<fields.length; i++)
        {

                result += fields[i].getName() + ": " + fields[i].getReturnType() + "\n";

        }

        return result;
    }
}
