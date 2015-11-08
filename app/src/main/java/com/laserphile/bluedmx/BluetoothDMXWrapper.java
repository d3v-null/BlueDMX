package com.laserphile.bluedmx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Function;

import java.util.concurrent.Callable;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by Derwent on 8/11/15.
 */
public class BluetoothDMXWrapper extends BluetoothSPP{
//    Function<String, Void> messageWriter;

    public BluetoothDMXWrapper(Context context, final Function<String, Void> messageWriter){
        super(context);
        final Function<String, Void> _messageWriter = messageWriter;

        setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //TODO: onDataReceived(byte[] data, String message)
                _messageWriter.apply(message);
            }
        });

        setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                String message = "Connected to " + name + "\n" + address;
                _messageWriter.apply(message);
            }

            public void onDeviceDisconnected() {
                String message = "@string/connection_lost";
                _messageWriter.apply(message);
            }

            public void onDeviceConnectionFailed() {
                String message = "@strong/cant_connect";
                _messageWriter.apply(message);
            }
        });
    }

    public boolean isServiceStateConnected(){
        return this.getServiceState() == BluetoothState.STATE_CONNECTED;
    }

    public void startBluetoothRequestActivity(Activity activity, Context applicationContext){
        Intent intent = new Intent(applicationContext, DeviceList.class);
        activity.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public boolean setDMXChannel(int channel, int value){
        //TODO: complete this method
        return false;
    }

    public int getDMXChannelValue(int channel){
        //TODO: complete this method
        return 0;
    }
}
