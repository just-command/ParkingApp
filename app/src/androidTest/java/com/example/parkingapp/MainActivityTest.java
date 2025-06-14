package com.example.parkingapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);

    private MockServer mockServer;

    @Test
    public void testInitialMapLoad() {
        // Проверяем, что SVG карта загружена
        Espresso.onView(ViewMatchers.withId(R.id.MapView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Проверяем, что кнопки управления отображаются
        Espresso.onView(ViewMatchers.withId(R.id.ZoomInButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        
        Espresso.onView(ViewMatchers.withId(R.id.ZoomOutButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        
        Espresso.onView(ViewMatchers.withId(R.id.SelectMapButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testZoomFunctionality() {
        // Проверяем начальное состояние кнопок зума
        Espresso.onView(ViewMatchers.withId(R.id.ZoomInButton))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));
        
        Espresso.onView(ViewMatchers.withId(R.id.ZoomOutButton))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));

        // Тестируем увеличение масштаба
        Espresso.onView(ViewMatchers.withId(R.id.ZoomInButton))
                .perform(ViewActions.click())
                .perform(ViewActions.click());

        // Тестируем уменьшение масштаба
        Espresso.onView(ViewMatchers.withId(R.id.ZoomOutButton))
                .perform(ViewActions.click())
                .perform(ViewActions.click());
    }

    @Test
    public void testMapSelection() {
        // Проверяем, что кнопка выбора карты активна
        Espresso.onView(ViewMatchers.withId(R.id.SelectMapButton))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                .perform(ViewActions.click());

        // Проверяем, что открылся экран выбора карты
        Espresso.onView(ViewMatchers.withId(R.id.RadioGroup))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Выбираем вторую карту
        Espresso.onView(ViewMatchers.withId(R.id.radioButton2))
                .perform(ViewActions.click());

        // Нажимаем кнопку подтверждения
        Espresso.onView(ViewMatchers.withId(R.id.SubmitButton))
                .perform(ViewActions.click());

        // Проверяем, что вернулись в MainActivity и карта отображается
        Espresso.onView(ViewMatchers.withId(R.id.MapView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testParkingSpotStatusUpdate() {
        // Проверяем, что карта загружена
        Espresso.onView(ViewMatchers.withId(R.id.MapView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Ждем обновления статуса (UPDATE_INTERVAL = 1000ms)
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, что карта все еще отображается после обновления
        Espresso.onView(ViewMatchers.withId(R.id.MapView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
} 