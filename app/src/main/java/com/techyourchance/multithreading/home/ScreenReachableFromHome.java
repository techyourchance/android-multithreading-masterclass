package com.techyourchance.multithreading.home;

public enum ScreenReachableFromHome {
    TEMP("Temp");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
