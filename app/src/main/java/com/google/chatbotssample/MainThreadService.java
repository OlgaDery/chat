package com.google.chatbotssample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MainThreadService extends Service {

    private static final String TAG = MainThreadService.class.getSimpleName();
    // Binder given to clients
    private final IBinder mBinder = new MyBinder();
    private ArrayList<Integer> indexes;

    public class MyBinder extends Binder {
        MainThreadService getService() {
            Log.d(TAG, "enter getService()");
            // Return this instance of MainThreadService so clients can call public methods
            Log.d(TAG, "exit getService()");
            return MainThreadService.this;
        }
    }

    public ArrayList<Integer> getIndexes() {
        Log.d(TAG, "enter getIndexes()");

        Log.d(TAG, "exit getIndexes()");
        return indexes;
    }


    public void sendIndexes() {
        //TODO do something useful
        Log.d(TAG, "enter sendIndexes()");
     //   ArrayList<Integer> indexes = generateIndexes();

        //TODO pass the indexes to the IntentService
        Intent intent1 = new Intent(getApplicationContext(), MyIntentService.class);
        intent1.setAction("START_DIALOG");
        intent1.putIntegerArrayListExtra("INDEXES", indexes);
        startService(intent1);

        Log.d(TAG, "exit sendIndexes()");
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        //TODO do something useful
//        Log.d(TAG, "enter onStartCommand(Intent intent, int flags, int startId)");
//
//        if (intent != null) {
//            final String action = intent.getAction();
//            if ("START_DIALOG".equals(action)) {
//                ArrayList<Integer> indexes = generateIndexes();
//
//                //TODO pass the indexes to the IntentService
//                Intent intent1 = new Intent(getApplicationContext(), MyIntentService.class);
//                intent1.setAction("START_DIALOG");
//                intent1.putIntegerArrayListExtra("INDEXES", indexes);
//                startService(intent1);
//
//            }
//        }
//
//
//        Log.d(TAG, "exit onStartCommand(Intent intent, int flags, int startId)");
//        return Service.START_NOT_STICKY;
//    }

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
