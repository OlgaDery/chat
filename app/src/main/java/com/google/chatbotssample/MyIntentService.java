package com.google.chatbotssample;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread. It receives the intent with the array of indexes to
 * select the phrases and broadcasting two types of intents with different actions. They supposed to
 * be received by Broadcast receivers in MainActivity, in which the phrases will be selected
 * by given indexed and shown to user.
 */
public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {

        super("MyIntentService");
    }


    // method to send broadcasts with the action START_DIALOG2
    private void sendBroadcast2(Integer index) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", index);
        intent.setAction("START_DIALOG2");
        getApplicationContext().sendBroadcast(intent);
    }


    // method to send broadcasts with the action START_DIALOG1
    private void sendBroadcast1(Integer index) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", index);
        intent.setAction("START_DIALOG1");
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    //main method of the class, which handles the intent from MainThreadService, creates the
    //intents and calls the broadcast methods. To make sure that there is a reasonable gap between
    //2 next phrases are shown, we are calling Thread.sleep() method after each breadcast.
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "enter onHandleIntent(Intent intent)");
        if (intent != null) {
            final String action = intent.getAction();
            if ("START_DIALOG".equals(action)) {
                ArrayList<Integer> indexes = intent.getIntegerArrayListExtra("INDEXES");
                for (Integer i : indexes) {
                    sendBroadcast1(i);
                    try{
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    sendBroadcast2(i);
                    try{
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                }

            }
        }
        Log.d(TAG, "enter onHandleIntent(Intent intent)");
    }

}
