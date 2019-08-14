package com.techyourchance.multithreading.home;

public enum ScreenReachableFromHome {
    EXERCISE_1("Exercise 1");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
