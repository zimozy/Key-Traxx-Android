package com.example.keytraxx;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ActivationActivity extends AppCompatActivity {
    //  CONSTANTS   //
    private static final int REQUEST_ENABLE_BT = 1; // used by startActivityForResult and sent to @Override onActivityResult as requestCode (first parameter)
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private static final long SCAN_PERIOD = 3000;
    private static final String TAG = "BTLEKeyTraxx";

    //  OBJECTS  //
    private BluetoothAdapter bluetoothAdapter;
//    private LeDeviceListAdapter mLeDeviceListAdapter;
    private DatabaseReference mDatabase;


    //  OTHERS  //
    private boolean areScanning;
    private Handler mHandler;
    private TextView tvAmount;
    private ArrayList<String> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanForClosestKey();
            }
        });

//        // The id of the channel.
//        String CHANNEL_ID = "my_channel_01";
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//        // Creates an explicit intent for an Activity in your app
//                Intent resultIntent = new Intent(this, MainActivity.class);
//
//        // The stack builder object will contain an artificial back stack for the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your app to the Home screen.
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        // Adds the back stack for the Intent (but not the Intent itself)
//                stackBuilder.addParentStack(MainActivity.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//                stackBuilder.addNextIntent(resultIntent);
//                PendingIntent resultPendingIntent =
//                        stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                mBuilder.setContentIntent(resultPendingIntent);
//                NotificationManager mNotificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // mNotificationId is a unique integer your app uses to identify the
//        // notification. For example, to cancel the notification, you can pass its ID
//        // number to NotificationManager.cancel().
//                mNotificationManager.notify(1, mBuilder.build());

    }

    private void scanForClosestKey() {
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

                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    for (final String theAddress: addresses) {
                        mDatabase.child("dealerships").child("awoeifjw").child("keys").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(theAddress)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DatabaseReference car = mDatabase.child("dealerships").child("awoeifjw").child("cars");

                                            TextView tvMessage = findViewById(R.id.tvMessage);
                                            tvMessage.setText("Found device!");

                                            TextView tvMake = findViewById(R.id.tvMake);
                                            tvMake.setText(dataSnapshot.getKey());

                                            TextView tvModel = findViewById(R.id.tvModel);
                                            tvModel.setText("");

                                            TextView tvYear = findViewById(R.id.tvYear);
                                            tvYear.setText("");


                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

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

                    if (rssi > -70) {
                        String address = device.getAddress();
                        if (!addresses.contains(address)) {
                            addresses.add(address);
                        }


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
