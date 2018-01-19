package com.example.keytraxx;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class MainActivity extends AppCompatActivity {

    private static final UUID SERVICE_UUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
    String testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();

// We want to receive a list of found devices every second
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .build();

// We only want to scan for devices advertising our custom service
        ScanFilter scanFilter = new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(SERVICE_UUID)).build();
        scanner.startScan(Arrays.asList(scanFilter), settings, mScanCallback);
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // We scan with report delay > 0. This will never be called.
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Toast.makeText(getApplicationContext(), "Hi there!", Toast.LENGTH_LONG);
            if (!results.isEmpty()) {
                ScanResult result = results.get(0);
                BluetoothDevice device = result.getDevice();
                String deviceAddress = device.getAddress();
                // Device detected, we can automatically connect to it and stop the scan
                Toast.makeText(getParent().getApplicationContext(), deviceAddress, Toast.LENGTH_LONG);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            // Scan error
        }
    };
}
