package com.example.danyal.bluetoothhc05;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class ledControl extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btnDis, btn5;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private boolean buttonIsDown = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        setContentView(R.layout.activity_led_control);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btnDis = (Button) findViewById(R.id.button5);
        btn5 = (Button) findViewById(R.id.stopButton);

        new ConnectBT().execute();


        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonIsDown = false;
                    Log.i("button is up", "S has been sent");
                    sendSignal("S");
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonIsDown = true;
                    final Handler handler = new Handler();
                    final Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            Log.i("In Forward", "F has been sent");
                            sendSignal("F");
                            if (buttonIsDown) {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    myRunnable.run();
                    return true;
                }
                return false;
            }
        });

        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonIsDown = false;
                    Log.i("button is up", "S has been sent");
                    sendSignal("S");
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonIsDown = true;
                    final Handler handler = new Handler();
                    final Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            sendSignal("B");
                            if (buttonIsDown) {
                                handler.postDelayed(this, 100);  // 1 second delay
                            }
                        }
                    };
                    myRunnable.run();
                    return true;
                }
                return false;
            }
        });

        btn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonIsDown = false;
                    Log.i("button is up", "S has been sent");
                    sendSignal("S");
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonIsDown = true;
                    final Handler handler = new Handler();
                    final Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            sendSignal("L");
                            if (buttonIsDown) {
                                handler.postDelayed(this, 100);  // 1 second delay
                            }
                        }
                    };
                    myRunnable.run();
                    return true;
                }
                return false;
            }
        });

        btn4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonIsDown = false;
                    Log.i("button is up", "S has been sent");
                    sendSignal("S");
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonIsDown = true;
                    final Handler handler = new Handler();
                    final Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            sendSignal("R");
                            if (buttonIsDown) {
                                handler.postDelayed(this, 100);  // 1 second delay
                            }
                        }
                    };
                    myRunnable.run();
                    return true;
                }
                return false;
            }
        });

                                                                                                                                                                                
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("S");
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Disconnect();
            }
        });
    }

    private void sendSignal ( final String number ) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }

        finish();
    }

    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }
}
