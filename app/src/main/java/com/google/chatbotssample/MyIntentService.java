package com.google.chatbotssample;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {

        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    private void sendBroadcast2(Integer index) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", index);
        intent.setAction("START_DIALOG2");
        getApplicationContext().sendBroadcast(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    private void sendBroadcast1(Integer index) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", index);
        intent.setAction("START_DIALOG1");
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
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
