package com.techyourchance.multithreading.common;

import com.techyourchance.fragmenthelper.FragmentHelper;
import com.techyourchance.multithreading.exercises.exercise1.Exercise1Fragment;
import com.techyourchance.multithreading.exercises.exercise2.Exercise2Fragment;
import com.techyourchance.multithreading.home.HomeFragment;

public class ScreensNavigator {

    private final FragmentHelper mFragmentHelper;

    public ScreensNavigator(FragmentHelper fragmentHelper) {
        mFragmentHelper = fragmentHelper;
    }

    public void navigateBack() {
        mFragmentHelper.navigateBack();
    }

    public void navigateUp() {
        mFragmentHelper.navigateUp();
    }

    public void toHomeScreen() {
        mFragmentHelper.replaceFragmentAndClearHistory(HomeFragment.newInstance());
    }

    public void toExercise1Screen() {
        mFragmentHelper.replaceFragment(Exercise1Fragment.newInstance());
    }

    public void toExercise2Screen() {
        mFragmentHelper.replaceFragment(Exercise2Fragment.newInstance());
    }
}
