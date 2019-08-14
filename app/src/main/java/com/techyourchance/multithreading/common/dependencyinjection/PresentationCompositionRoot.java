package com.techyourchance.multithreading.common.dependencyinjection;

import com.techyourchance.fragmenthelper.FragmentContainerWrapper;
import com.techyourchance.fragmenthelper.FragmentHelper;
import com.techyourchance.multithreading.common.ToolbarManipulator;
import com.techyourchance.multithreading.common.ScreensNavigator;

import androidx.fragment.app.FragmentActivity;

public class PresentationCompositionRoot {

    private final FragmentActivity mActivity;

    public PresentationCompositionRoot(FragmentActivity activity) {
        mActivity = activity;
    }

    public ScreensNavigator getScreensNavigator() {
        return new ScreensNavigator(getFragmentHelper());
    }

    private FragmentHelper getFragmentHelper() {
        return new FragmentHelper(mActivity, getFragmentContainerWrapper(), mActivity.getSupportFragmentManager());
    }

    private FragmentContainerWrapper getFragmentContainerWrapper() {
        return (FragmentContainerWrapper) mActivity;
    }

    public ToolbarManipulator getToolbarManipulator() {
        return (ToolbarManipulator) mActivity;
    }

}
