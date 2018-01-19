package com.example.keytraxx;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity_btle extends AppCompatActivity {

    //  CONSTANTS   //
    private static final int REQUEST_ENABLE_BT = 1; // used by startActivityForResult and sent to @Override onActivityResult as requestCode (first parameter)
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = "TRAX";



    //  OBJECTS  //
    private BluetoothAdapter bluetoothAdapter;
//    private LeDeviceListAdapter mLeDeviceListAdapter;


    //  OTHERS  //
    private boolean areScanning;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    Log.d(TAG, device.getAddress());

                    createToast(device.getAddress());

                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });*/
                }
            };

    public void createToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
