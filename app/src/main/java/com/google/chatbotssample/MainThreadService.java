package com.google.chatbotssample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

//This service is supposed to bind to MainActivity. It is executed in the main thread, so
//no long running operations have to be here. The main task for this service is to generate the
//array of index for the phrases to get selected for the dialog of bots, to put this array to the
//intent and to pass this intent to IntentService (which is executed in the separate thread).

public class MainThreadService extends Service {

    private static final String TAG = MainThreadService.class.getSimpleName();

    // Binder given to clients
    private final IBinder mBinder = new MyBinder();
    private ArrayList<Integer> indexes;

    //the inner class providing the instance of this service to bind
    public class MyBinder extends Binder {
        MainThreadService getService() {
            Log.d(TAG, "enter getService()");
            // Return this instance of MainThreadService so clients can call public methods
            Log.d(TAG, "exit getService()");
            return MainThreadService.this;
        }
    }

    //getter for the array of indexes
    public ArrayList<Integer> getIndexes() {
        Log.d(TAG, "enter getIndexes()");

        Log.d(TAG, "exit getIndexes()");
        return indexes;
    }


    //The method to be called from MainActivity. The goal is to create an intent, to put
    //the array of indexes in it and to send to IntentService.
    public void sendIndexes() {

        Log.d(TAG, "enter sendIndexes()");


        //to pass the indexes to the IntentService
        Intent intent1 = new Intent(getApplicationContext(), MyIntentService.class);
        intent1.setAction("START_DIALOG");
        intent1.putIntegerArrayListExtra("INDEXES", indexes);
        startService(intent1);

        Log.d(TAG, "exit sendIndexes()");
    }


    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        Log.d(TAG, "enter onBind(Intent intent)");
        if (intent.hasExtra("BEGIN")) {
            this.generateIndexes();
        }
        Log.d(TAG, "exit onBind(Intent intent)");
        return mBinder;
    }

    private ArrayList<Integer> generateIndexes(){
        //this method is to generate indexes for phrases to select
        Log.d(TAG, "enter generateIndexes()");
        indexes = new ArrayList<>(Phrases.prases1.length);
        Random mGenerator = new Random();
        int count = 0;
        while (count<Phrases.prases1.length) {
            indexes.add(mGenerator.nextInt(8));
            count++;
        }
        Log.d(TAG, "exit generateIndexes()");
        return indexes;

    }

}
