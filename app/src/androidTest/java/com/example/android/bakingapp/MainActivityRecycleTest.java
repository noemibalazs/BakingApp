package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.example.android.bakingapp.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityRecycleTest {


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onClick(){
        onView(withId(R.id.cake_recycle_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    @Test
    public void clickNutella() {
        onView(withId(R.id.cake_recycle_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Nutella Pie")), click()));
    }
}
