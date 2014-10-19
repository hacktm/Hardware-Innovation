//package com.example.androidhacktm;

//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;

//public class SendData extends Activity {
	
//	  EditText myTextbox;
//	  BluetoothAdapter mBluetoothAdapter;
//	  BluetoothSocket mmSocket;
//	  BluetoothDevice mmDevice;
//	  OutputStream mmOutputStream;
//	  InputStream mmInputStream;
//	  Thread workerThread;
//	  byte[] readBuffer;
//	  int readBufferPosition;
//	  int counter;
//	  volatile boolean stopWorker;
	 
//	  Button closeButton = (Button)findViewById(R.id.close);
//	  myTextbox = (EditText)findViewById(R.id.tbText);
	
	
//	@Override
//		protected void onCreate(Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_send_data);
//			
//			Button openButton = (Button)findViewById(R.id.ibOpen);
//			Button sendButton = (Button)findViewById(R.id.ibSend);
//		    myTextbox = (EditText)findViewById(R.id.tbText);
//		    
//		  //Open Button
//		    openButton.setOnClickListener(new View.OnClickListener() {
//		      public void onClick(View v) {
//		        try {
//		          findBT();
//		          openBT();
//		        }
//		        catch (IOException ex) { }
//		      }
//		    });
//
//		    //Send Button
//		    sendButton.setOnClickListener(new View.OnClickListener() {
//		      public void onClick(View v) {
//		        try {
//		          sendData();
//		        }
//		        catch (IOException ex) {
////		            showMessage("SEND FAILED");
//		        }
//		      }
//		    });
//		}
//
//	 void findBT() {
//		    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		    if(mBluetoothAdapter == null) {
////		      myLabel.setText("No bluetooth adapter available");
//		    }
//
//		    if(!mBluetoothAdapter.isEnabled()) {
//		      Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		      startActivityForResult(enableBluetooth, 0);
//		    }
//
//		    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//		    if(pairedDevices.size() > 0) {
//		      for(BluetoothDevice device : pairedDevices) {
//		        if(device.getName().equals("HC-05")) {
//		          mmDevice = device;
//		          break;
//		        }
//		      }
//		    }
////		    myLabel.setText("Bluetooth Device Found");
//		  }
//
//	 void openBT() throws IOException {
//		    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
//		    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);    
//		    mmSocket.connect();
//		    mmOutputStream = mmSocket.getOutputStream();
//		    mmInputStream = mmSocket.getInputStream();
////		    beginListenForData();
////		    myLabel.setText("Bluetooth Opened");
//		  }
//		  
//	 void beginListenForData() {
//			    final Handler handler = new Handler(); 
//			    final byte delimiter = 10; //This is the ASCII code for a newline character
//
//			    stopWorker = false;
//			    readBufferPosition = 0;
//			    readBuffer = new byte[1024];
//			    workerThread = new Thread(new Runnable() {
//			      public void run() {
//			         while(!Thread.currentThread().isInterrupted() && !stopWorker) {
//			          try {
//			            int bytesAvailable = mmInputStream.available();            
//			            if(bytesAvailable > 0) {
//			              byte[] packetBytes = new byte[bytesAvailable];
//			              mmInputStream.read(packetBytes);
//			              for(int i=0;i<bytesAvailable;i++) {
//			                byte b = packetBytes[i];
//			                if(b == delimiter) {
//			                  byte[] encodedBytes = new byte[readBufferPosition];
//			                  System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//			                  final String data = new String(encodedBytes, "US-ASCII");
//			                  readBufferPosition = 0;
//
//			                  handler.post(new Runnable() {
//			                    public void run() {
////			                      myLabel.setText(data);
//			                    }
//			                  });
//			                }
//			                else {
//			                  readBuffer[readBufferPosition++] = b;
//			                }
//			              }
//			            }
//			          } 
//			          catch (IOException ex) {
//			            stopWorker = true;
//			          }
//			         }
//			      }
//			    });
//
//			    workerThread.start();
//			  }	  
//	
//	 void sendData() throws IOException {
//	    String msg = myTextbox.getText().toString();
//	    msg += "\n";
//	    //mmOutputStream.write(msg.getBytes());
//	    mmOutputStream.write('A');
////	    myLabel.setText("Data Sent");
//			  }	  
//		  
//	private void showMessage(String theMsg) {
//        Toast msg = Toast.makeText(getBaseContext(),
//                theMsg, (Toast.LENGTH_LONG)/160);
//        msg.show();
//    }
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.send_data, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
