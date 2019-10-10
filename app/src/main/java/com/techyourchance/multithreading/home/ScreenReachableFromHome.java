package com.techyourchance.multithreading.home;

public enum ScreenReachableFromHome {
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    UI_THREAD_DEMONSTRATION("UI Thread Demo"),
    UI_HANDLER_DEMONSTRATION("UI Handler Demo"),
    CUSTOM_HANDLER_DEMONSTRATION("Custom Handler Demo"),
    EXERCISE_3("Exercise 3"),
    ATOMICITY_DEMONSTRATION("Atomicity Demo"),
    EXERCISE_4("Exercise 4"),
    THREAD_WAIT_DEMONSTRATION("Thread Wait Demo"),
    EXERCISE_5("Exercise 5")
    ;

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
