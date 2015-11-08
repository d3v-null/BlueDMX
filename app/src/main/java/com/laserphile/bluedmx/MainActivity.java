package com.laserphile.bluedmx;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.base.Function;

import java.util.concurrent.Callable;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    public BluetoothDMXWrapper btw;
    private Function<String, Void> messageWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context applicationContext = getApplicationContext();
        final Activity applicationActivity = this;
        messageWriter = new Function<String, Void>() {
            @Override
            public Void apply(String input) {
                Toast.makeText(
                        applicationContext,
                        "@string/bluetooth_not_available",
                        Toast.LENGTH_SHORT
                ).show();
                return null;
            }
        };

        btw = new BluetoothDMXWrapper(this, messageWriter);

        if(!btw.isBluetoothAvailable()) {
            this.messageWriter.apply("@string/bluetooth_not_available");
//            finish();
        }

        Button btnNewConnection = (Button)findViewById(R.id.btnNewConnection);
        btnNewConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btw.isServiceStateConnected()){
                    btw.disconnect();
                }
                btw.startBluetoothRequestActivity(applicationActivity, applicationContext);
            }
        });



//        Intent intent = new Intent(applicationContext, DeviceList.class);
//        intent.putExtra("bluetooth_devices", "Bluetooth devices");
//        intent.putExtra("no_devices_found", "No devices");
//        intent.putExtra("scanning", "Scanning");
//        intent.putExtra("scan_for_devices", "Search");
//        intent.putExtra("select_device", "Select");
//        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public void onDestroy() {
        super.onDestroy();
        btw.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!btw.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!btw.isServiceAvailable()) {
                btw.setupService();
                btw.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            }
        }

        //TODO: complete the rest of this from copy
    }

    public void setup() {
        Button btnSetChannel = (Button)findViewById(R.id.btnSetChannel);
        btnSetChannel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //TODO: get value and channel from form
                int value = 0;
                int channel = 0;
                btw.setDMXChannel(channel, value);
            }
        });
        //TODO: set onClick listeners for btnIncrementChannel and btnDecrementChannel
        //TODO: set onClick listeners for btnNewConnection
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
