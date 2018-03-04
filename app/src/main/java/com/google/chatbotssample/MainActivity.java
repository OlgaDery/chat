package com.google.chatbotssample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver phraseCodeReceiver;
    private BroadcastReceiver phraseCodeReceiver2;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView phrase1;
    private TextView phrase2;
    private boolean mBound = false;
    private MainThreadService mService;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phrase1 = (TextView) findViewById(R.id.phrase1);
        phrase2 = (TextView) findViewById(R.id.phrase2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO start service
              //  Intent intent = new Intent(getApplicationContext(), MainThreadService.class);
              //  intent.setAction("START_DIALOG");
              //  startService(intent);
                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    mService.sendIndexes();

                }

            }
        });

        phraseCodeReceiver = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                //On receive should be called very rarely, onle the current location is significantly changed. In our case, it is calling
                //onle once
                Log.d(TAG, "enter onReceive(Context context, Intent intent)");

                int index = intent.getIntExtra("INDEX", 0);
                phrase2.setText(Phrases.prases2[index]);
                phrase2.setTextSize(18);

                Log.d(TAG, "exit onReceive(Context context, Intent intent)");
            };

        };

        phraseCodeReceiver2 = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                //On receive should be called very rarely, onle the current location is significantly changed. In our case, it is calling
                //onle once
                Log.d(TAG, "enter onReceive2(Context context, Intent intent)");

                int index = intent.getIntExtra("INDEX", 0);
                phrase1.setText(Phrases.prases1[index]);
                phrase1.setTextSize(18);

                Log.d(TAG, "exit onReceive2(Context context, Intent intent)");
            };

        };

        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                MainThreadService.MyBinder binder = (MainThreadService.MyBinder) service;
                mService = binder.getService();
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MainThreadService.class);
        intent.putExtra("BEGIN", true);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }


    private void registerReceiver(BroadcastReceiver receiver, String action) {
        Log.d(TAG, "enter registerReceiver()");
        // Create an intent filter for DATA_RECEIVED.
        IntentFilter intentFilter =
                new IntentFilter();
        intentFilter.addAction(action); //"START_DIALOG1"

        // Register the receiver and the intent filter.
        registerReceiver(receiver,
                intentFilter);
        Log.d(TAG, "exit registerReceiver()");
    }


    //TODO register receiver in onResume
    @Override
    protected void onResume() {
        //    Log.d(TAG, "enter onResume()");
        registerReceiver(phraseCodeReceiver, "START_DIALOG1");
        registerReceiver(phraseCodeReceiver2, "START_DIALOG2");
        super.onResume();
        //    Log.d(TAG, "exit onResume()");

    }

    @Override
    protected void onPause () {
        //    Log.d(TAG, "enter onPause ()");
        super.onPause();
        this.unregisterReceiver(this.phraseCodeReceiver);
        this.unregisterReceiver(this.phraseCodeReceiver2);
        //    Log.d(TAG, "exit onPause()");
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
