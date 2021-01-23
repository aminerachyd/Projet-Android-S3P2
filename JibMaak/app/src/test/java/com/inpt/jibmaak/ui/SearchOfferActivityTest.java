package com.inpt.jibmaak.ui;

import android.os.Build;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.activities.SearchOfferActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;


@Config(sdk = {Build.VERSION_CODES.P},application = HiltTestApplication.class)
@RunWith(RobolectricTestRunner.class)
@LargeTest
@HiltAndroidTest
public class SearchOfferActivityTest {
    // Note : pour lancer ce test ne pas utiliser le bouton à coté de la classe ou des méthodes
    // dans Android studio mais utiliser le menu Gradle à droite
    // Gradle -> JibMaak -> app -> verification -> testDebugUnitTest
    @Rule
    public ActivityScenarioRule<SearchOfferActivity> activity = new ActivityScenarioRule<>(SearchOfferActivity.class);

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Before
    public void init() {
        hiltRule.inject(); // Injection des dépendances
    }

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
