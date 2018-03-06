package com.google.chatbotssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device. In this class we are testing
 * some components of UI using the Espresso. Also, we are registering the same BroadcastReceiver
 * as in the MainActivity and checking how it works.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityInstrumentedTest {
    public Context appContext;
    public MainActivity activity;
    private int index = 0;
    private static final String TAG = MyIntentService.class.getSimpleName();


    @Rule
    public final ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp () {
        appContext  = InstrumentationRegistry.getTargetContext();
        activity = activityRule.getActivity();
    }



    @Test
    public void testClickButton() throws TimeoutException {
        // Register the receiver and the intent filter. It is expected to receive the same intent
        //as one of the receivers of MainActivity with the same randomly selected index.
        BroadcastReceiver phraseCodeReceiver = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                index = intent.getIntExtra("INDEX", 0);
                Log.i(TAG, "index: "+ index);
            };

        };

        IntentFilter intentFilter =
                new IntentFilter();
        intentFilter.addAction("START_DIALOG1");

        InstrumentationRegistry.getTargetContext().registerReceiver(phraseCodeReceiver,
                intentFilter);

        //performing the button click and checking the UI behavior

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.phrase2)).check(matches(isDisplayed()));

        //the phrase shown in R.id.phrase2 supposed to be the same as selected from the phrases
        //by the index provided with the intent to the receiver registered above.
        onView(withId(R.id.phrase2)).check(matches(withText(Phrases.prases2[index])));
    }

}
