package com.google.chatbotssample;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext;


    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();


    @Before
    public void getContext () {
        appContext  = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void useAppContext() throws Exception {

        assertEquals("com.google.chatbotssample", appContext.getPackageName());
    }


    @Test
    public void assertNoDatabase () {
        assertEquals(0, appContext.databaseList().length);

    }

    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(appContext, MainThreadService.class);

        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra("BEGIN", true);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        MainThreadService service = ((MainThreadService.MyBinder) binder).getService();

        // Verify that the service is working correctly.
        assertEquals(service.getIndexes().size(), Phrases.prases1.length);
    }





}
