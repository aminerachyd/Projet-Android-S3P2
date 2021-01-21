package com.inpt.jibmaak;

import android.os.Build;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.inpt.jibmaak.activities.SearchOfferActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@Config(sdk = {Build.VERSION_CODES.P})
@RunWith(RobolectricTestRunner.class)
@LargeTest
public class SearchOfferActivityTest {
    @Rule
    public ActivityScenarioRule<SearchOfferActivity> activity = new ActivityScenarioRule<>(SearchOfferActivity.class);

    @Test
    public void isDateDialogShownCorrectly(){
        // Lorsqu'on clique sur le TextView pour choisir la date, un dialogue doit s'ouvrir
        Espresso.onView(ViewMatchers.withId(R.id.date_depart_avant)).perform(ViewActions.scrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).inRoot(RootMatchers.isDialog()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void isDateDialogNotShownByDefault(){
        // Lorsqu'on a pas encore cliqué sur le bouton, le dialogue ne doit pas être là
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).check(ViewAssertions.doesNotExist());
    }
}
