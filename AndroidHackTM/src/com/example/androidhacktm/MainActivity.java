package com.example.androidhacktm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {


	DrawingView dv ;
	EditText myTextbox;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
    ImageButton ibSend,ibOpen;
//  EditText myTextbox;
	private TextView state;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int PAIRDEVICE = 2;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothService bluetoothService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.activity_main);
	    dv =(DrawingView)findViewById(R.id.dv);
	    dv.mPaint = new Paint();
	    dv.mPaint.setAntiAlias(true);
	    dv.mPaint.setDither(true);
	    dv.mPaint.setColor(Color.GREEN);
	    dv.mPaint.setStyle(Paint.Style.STROKE);
	    dv.mPaint.setStrokeJoin(Paint.Join.ROUND);
	    dv.mPaint.setStrokeCap(Paint.Cap.ROUND);
	    dv.mPaint.setStrokeWidth(12); 
	    
	    state = (TextView)findViewById(R.id.state);
		state.setText("NOT CONNECTED!");
		state.setTextColor(Color.RED);
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothService = new BluetoothService();
	    
	    ImageButton openButton = (ImageButton)findViewById(R.id.ibOpen);
		ImageButton sendButton = (ImageButton)findViewById(R.id.ibSend);
		myTextbox = (EditText)findViewById(R.id.tbText);
		
        openButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub  
				if (!bluetoothAdapter.isEnabled()) {
					enableBluetooth();
				}
				else
				{
					pairDevice();
				}
			}
		});
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              try {
                sendData();
              }
              catch (IOException ex) {
                  showMessage("SEND FAILED");
              }
            }
          });  
        Toast.makeText(this, "\tNOT CONNECTED!\nPlease use connect button!", Toast.LENGTH_LONG).show();
        }
		//Open Button
//	    openButton.setOnClickListener(new View.OnClickListener() {
//	      public void onClick(View v) {
//	        try {
//	          findBT();
//	          openBT();
//	        }
//	        catch (IOException ex) { }
//	      }
//	    });
	    
    	private void pairDevice() {
    		//Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
    		bluetoothService.start();
    		Intent intent = new Intent(this, BluetoothDevices.class);
    		startActivityForResult(intent, PAIRDEVICE);
    	}
        
    	private void enableBluetooth() {
    		Intent enableBtIntent;
    		enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	}
        
    	@Override
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	    // Check which request we're responding to
    	    if (requestCode == REQUEST_ENABLE_BT) {
    	        // Make sure the request was successful
    	        if (resultCode == RESULT_OK) {
    	        	pairDevice();
    	        }
    	    }
    	    else
    	    	if(requestCode == PAIRDEVICE)
    	    	{
    	    		if(resultCode == RESULT_OK)
    	    		{
    	    			Toast.makeText(this,"Connecting to " + data.getDataString(), Toast.LENGTH_LONG).show();
    	    			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(data.getDataString());
    	    	        // Attempt to connect to the device
    	    			bluetoothService.connect(device, true);
    	    			int bluetoothstate = bluetoothService.getState();
    	    			while(bluetoothstate!=bluetoothService.STATE_CONNECTED)
    	    			{
    	    				bluetoothstate=bluetoothService.getState();
    	    				if(bluetoothstate==bluetoothService.STATE_LISTEN)
    	    				{
    	    					break;
    	    				}
    	    			}
    	    			if (bluetoothstate == bluetoothService.STATE_CONNECTED)
    	    			{
    	    				state.setTextColor(Color.GREEN);
    	    				state.setText("CONNECTED!");
    	    			}
    	    			else
    	    			{
    	    				state.setTextColor(Color.RED);
    	    				state.setText("NOT CONNECTED!");
    	    			}
    	    		}
    	    	}
    	
    	
    	}
	   
//      ibSend = (ImageButton)findViewById(R.id.ibSend);
//	    ibOpen = (ImageButton)findViewById(R.id.ibOpen);
//	    myTextbox = (EditText)findViewById(R.id.tbText);

 
	   


//	 void findBT() 
//	 {
//	    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//	    if(mBluetoothAdapter == null) {
//		      myLabel.setText("No bluetooth adapter available");
//	    }
//
//	    if(!mBluetoothAdapter.isEnabled()) {
//	      Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//	      startActivityForResult(enableBluetooth, 0);
//	    }
//
//	    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//	    if(pairedDevices.size() > 0) {
//	      for(BluetoothDevice device : pairedDevices) {
//	        if(device.getName().equals("GT-I9300")) {
//	          mmDevice = device;
//	          break;
//	        }
//	      }
//	    }
//		    myLabel.setText("Bluetooth Device Found");
//	  }
	 
//	 void openBT() throws IOException {
//		    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
//		    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);    
		  // mmSocket.connect();
		  // mmOutputStream = mmSocket.getOutputStream();
		  // mmInputStream = mmSocket.getInputStream();
//		    beginListenForData();
//		    myLabel.setText("Bluetooth Opened");
//		  }
	 
	 void sendData() throws IOException {
		    String msg = myTextbox.getText().toString();
		    msg += "\n";
		    //mmOutputStream.write(msg.getBytes());
		    DrawingView.SendDegrees();
//		    mmOutputStream.write()
//		    myLabel.setText("Data Sent");
				  }	  
	private void showMessage(String theMsg) {
	        Toast msg = Toast.makeText(getBaseContext(),
	                theMsg, (Toast.LENGTH_LONG)/160);
	        msg.show();
	    }	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
