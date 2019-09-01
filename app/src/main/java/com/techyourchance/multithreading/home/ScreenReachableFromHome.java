package com.techyourchance.multithreading.home;

public enum ScreenReachableFromHome {
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    UI_THREAD_DEMONSTRATION("UI Thread Demonstration"),
    UI_HANDLER_DEMONSTRATION("UI Handler Demonstration")
    ;

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
