package com.techyourchance.multithreading.common;

import com.techyourchance.fragmenthelper.FragmentHelper;
import com.techyourchance.multithreading.home.HomeFragment;

public class ScreensNavigator {

    private final FragmentHelper mFragmentHelper;

    public ScreensNavigator(FragmentHelper fragmentHelper) {
        mFragmentHelper = fragmentHelper;
    }

    public void toHomeScreen() {
        mFragmentHelper.replaceFragmentAndClearHistory(HomeFragment.newInstance());
    }

    public void navigateBack() {
        mFragmentHelper.navigateBack();
    }

    public void navigateUp() {
        mFragmentHelper.navigateUp();
    }
}
