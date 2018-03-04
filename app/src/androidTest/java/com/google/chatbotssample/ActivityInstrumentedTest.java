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
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityInstrumentedTest {
    Context appContext;
    MainActivity activity;
    int index = 0;


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
        // check the state of the view
        BroadcastReceiver phraseCodeReceiver = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                //On receive should be called very rarely, onle the current location is significantly changed. In our case, it is calling
                //onle once

                index = intent.getIntExtra("INDEX", 0);

            };

        };

        IntentFilter intentFilter =
                new IntentFilter();
        intentFilter.addAction("START_DIALOG1"); //

        // Register the receiver and the intent filter.
        InstrumentationRegistry.getTargetContext().registerReceiver(phraseCodeReceiver,
                intentFilter);

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.phrase2)).check(matches(isDisplayed()));
        onView(withId(R.id.phrase2)).check(matches(withText(Phrases.prases2[index])));
    }

}
