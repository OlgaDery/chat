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
    //This is the only component responsible for UI elements representation. It shows the dialog
    //of 2 pseudo bots, which looks like the exchange of 8 randomly selected phrases (they will be
    //taken from the static string arrays in Phrases class. The dialog starts after the button
    //FloatingActionButton fab gets clicked. It starts the sendIndexes() method in MainThreadService
    // class (this sevice supposed to be already bound in onStart() method. SendIndexes() sends
    // the array of previously generated indexes to MyIntentService, which is being executed in the
    // separate thread. From the IntentService two types of intents will be broadcasted - with
    // the actions START_DIALOG2 and START_DIALOG1 (also intents will contain the same index for
    // phrases. After this, broadcast receivers being registered in this Activity will receive
    // these intents. It triggers that the phrases are selected by indexes and showed to user.)


    private BroadcastReceiver phraseCodeReceiver;
    private BroadcastReceiver phraseCodeReceiver2;
    private static final String TAG = MainActivity.class.getSimpleName();

    //TextViews to show bot`s phrases
    private TextView phrase1;
    private TextView phrase2;

    //boolean to indicate in MainThreadService is bound
    private boolean mBound = false;
    private MainThreadService mService;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection;
    //button to trigger the process
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "enter onCreate(Bundle savedInstanceState) ");
        super.onCreate(savedInstanceState);

        //instantiating the UI elements
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phrase1 = (TextView) findViewById(R.id.phrase1);
        phrase2 = (TextView) findViewById(R.id.phrase2);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        //setting the button listener to call sendIndexes() method in the MainThreadService
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBound) {
                    mService.sendIndexes();

                }

            }
        });

        phraseCodeReceiver = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                //BroadcastReceiver1 waiting for intents with the action START_DIALOG1.
                // If received, it selects the phrase by the index provided with the intent and shows
                //to user.

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
                //BroadcastReceiver2 waiting for intents with the action START_DIALOG2.
                // If received, it selects the phrase by the index provided with the intent and shows
                //to user.
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
                Log.d(TAG, "enter onServiceConnected(ComponentName arg0)");
                // We've bound to MainThreadService, cast the IBinder and get MainThreadService instance
                MainThreadService.MyBinder binder = (MainThreadService.MyBinder) service;
                mService = binder.getService();
                mBound = true;
                Log.d(TAG, "exit onServiceConnected(ComponentName arg0)");
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                Log.d(TAG, "enter onServiceDisconnected(ComponentName arg0)");

                mBound = false;
                Log.d(TAG, "exit onServiceDisconnected(ComponentName arg0)");
            }
        };

        Log.d(TAG, "exit onCreate(Bundle savedInstanceState) ");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "enter onStart()");
        super.onStart();
        // Binding to MainThreadService and putting the extra data. "BEGIN" means
        //that the bound service has to start to generate the array of indexes to select
        // phrases for bots.
        Intent intent = new Intent(this, MainThreadService.class);
        intent.putExtra("BEGIN", true);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "exit onStart()");
    }

    @Override
    protected void onStop() {
        // Unbinding MainThreadService
        Log.d(TAG, "enter onStop()");
        super.onStop();
        unbindService(mConnection);
        mBound = false;
        Log.d(TAG, "exit onStop()");
    }


    private void registerReceiver(BroadcastReceiver receiver, String action) {
        Log.d(TAG, "enter registerReceiver()");
        // Creating an intent filter from provided parameter (action) and registering it.
        IntentFilter intentFilter =
                new IntentFilter();
        intentFilter.addAction(action);

        registerReceiver(receiver,
                intentFilter);
        Log.d(TAG, "exit registerReceiver()");
    }


    @Override
    protected void onResume() {
        //registering both receivers if the activity resumes
        Log.d(TAG, "enter onResume()");
        registerReceiver(phraseCodeReceiver, "START_DIALOG1");
        registerReceiver(phraseCodeReceiver2, "START_DIALOG2");
        super.onResume();
        Log.d(TAG, "exit onResume()");

    }

    @Override
    protected void onPause () {
        //unregistering both receivers if the activity pauses to avoid the memory overuse
        Log.d(TAG, "enter onPause ()");
        super.onPause();
        this.unregisterReceiver(this.phraseCodeReceiver);
        this.unregisterReceiver(this.phraseCodeReceiver2);
        Log.d(TAG, "exit onPause()");
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
