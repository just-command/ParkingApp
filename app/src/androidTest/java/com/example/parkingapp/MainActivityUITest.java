package com.example.parkingapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testInitialElementsAreDisplayed() {
        // Проверяем, что карта загружена
        onView(withId(R.id.MapView))
                .check(matches(isDisplayed()));

        // Проверяем наличие кнопок управления
        onView(withId(R.id.ZoomInButton)).check(matches(isDisplayed()));
        onView(withId(R.id.ZoomOutButton)).check(matches(isDisplayed()));
        onView(withId(R.id.SelectMapButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testZoomButtonsFunctionality() {
        // Проверяем, что кнопки зума активны
        onView(withId(R.id.ZoomInButton)).check(matches(isEnabled()));
        onView(withId(R.id.ZoomOutButton)).check(matches(isEnabled()));

        // Увеличиваем масштаб дважды
        onView(withId(R.id.ZoomInButton)).perform(click()).perform(click());

        // Уменьшаем масштаб дважды
        onView(withId(R.id.ZoomOutButton)).perform(click()).perform(click());
    }

    @Test
    public void testChangeMapSelection() {
        // Открываем выбор карты
        onView(withId(R.id.SelectMapButton)).perform(click());

        // Проверяем появление элементов выбора
        onView(withId(R.id.RadioGroup)).check(matches(isDisplayed()));

        // Выбираем второй вариант карты
        onView(withId(R.id.radioButton2)).perform(click());

        // Нажимаем "Подтвердить"
        onView(withId(R.id.SubmitButton)).perform(click());

        // Проверяем возврат на главный экран и отображение карты
        onView(withId(R.id.MapView)).check(matches(isDisplayed()));
    }

    @Test
    public void testAutoStatusUpdateAfterDelay() throws InterruptedException {
        // Карта загружена
        onView(withId(R.id.MapView)).check(matches(isDisplayed()));

        // Ждём обновления данных (например, каждые 1000 мс)
        Thread.sleep(1100);

        // Проверяем, что карта всё ещё отображается после обновления
        onView(withId(R.id.MapView)).check(matches(isDisplayed()));
    }
}