package com.example.androidhacktm;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.support.v7.app.ActionBarActivity;

public class BluetoothDevices extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> devicesAdapter;
	private ListView devicesList;
	private BroadcastReceiver mReceiver;
	private ArrayList<String> addresses = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_devices);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		devicesList = (ListView)findViewById(R.id.devices);
		devicesList.setAdapter(devicesAdapter);
		
		devicesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Intent data = new Intent();
			     
			    //---set the data to pass back---
			    data.setData(Uri.parse(addresses.get(position)));
			    setResult(RESULT_OK, data);
			    
			    unregisterReceiver(mReceiver);
			    
			    //---close the activity---
			    finish();
			    //Toast.makeText(getBaseContext(), "Connecting to " + bluetoothDevice, Toast.LENGTH_LONG).show();
			}
		});
		
		
		setDiscoveredDevices();
		getDevicesList();
	}
	
	private void setPairedDevices()
	{
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        devicesAdapter.add(device.getName() + "\n" + device.getAddress());
		        addresses.add(device.getAddress());
		    }
		}
	}
	
	private void setDiscoveredDevices()
	{
		mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            devicesAdapter.add(device.getName() + "\n" + device.getAddress());
		            addresses.add(device.getAddress());
		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth_devices, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			getDevicesList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getDevicesList() {
		devicesAdapter.clear();
		addresses.clear();
		setPairedDevices();
		mBluetoothAdapter.startDiscovery();
	}
}