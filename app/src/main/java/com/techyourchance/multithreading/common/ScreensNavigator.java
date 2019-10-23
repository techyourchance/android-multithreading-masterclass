package com.techyourchance.multithreading.common;

import com.techyourchance.fragmenthelper.FragmentHelper;
import com.techyourchance.multithreading.demonstrations.atomicity.AtomicityDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.customhandler.CustomHandlerDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designasynctask.DesignWithAsyncTaskDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designcoroutines.DesignWithCoroutinesDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designrxjava.DesignWithRxJavaDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designthread.DesignWithThreadsDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designthreadpool.DesignWithThreadPoolDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.designthreadposter.DesignWithThreadPosterDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.threadwait.ThreadWaitDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.uihandler.UiHandlerDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.uithread.UiThreadDemonstrationFragment;
import com.techyourchance.multithreading.exercises.exercise1.Exercise1Fragment;
import com.techyourchance.multithreading.exercises.exercise10.Exercise10Fragment;
import com.techyourchance.multithreading.exercises.exercise2.Exercise2Fragment;
import com.techyourchance.multithreading.exercises.exercise3.Exercise3Fragment;
import com.techyourchance.multithreading.exercises.exercise4.Exercise4Fragment;
import com.techyourchance.multithreading.exercises.exercise5.Exercise5Fragment;
import com.techyourchance.multithreading.exercises.exercise6.Exercise6Fragment;
import com.techyourchance.multithreading.exercises.exercise7.Exercise7Fragment;
import com.techyourchance.multithreading.exercises.exercise8.Exercise8Fragment;
import com.techyourchance.multithreading.exercises.exercise9.Exercise9Fragment;
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

    public void toUiThreadDemonstration() {
        mFragmentHelper.replaceFragment(UiThreadDemonstrationFragment.newInstance());
    }

    public void toUiHandlerDemonstration() {
        mFragmentHelper.replaceFragment(UiHandlerDemonstrationFragment.newInstance());
    }

    public void toCustomHandlerDemonstration() {
        mFragmentHelper.replaceFragment(CustomHandlerDemonstrationFragment.newInstance());
    }

    public void toExercise3Screen() {
        mFragmentHelper.replaceFragment(Exercise3Fragment.newInstance());
    }

    public void toAtomicityDemonstration() {
        mFragmentHelper.replaceFragment(AtomicityDemonstrationFragment.newInstance());
    }

    public void toExercise4Screen() {
        mFragmentHelper.replaceFragment(Exercise4Fragment.newInstance());
    }

    public void toThreadWaitDemonstration() {
        mFragmentHelper.replaceFragment(ThreadWaitDemonstrationFragment.newInstance());
    }

    public void toExercise5Screen() {
        mFragmentHelper.replaceFragment(Exercise5Fragment.newInstance());
    }

    public void toDesignWithThreadsDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithThreadsDemonstrationFragment.newInstance());
    }

    public void toExercise6Screen() {
        mFragmentHelper.replaceFragment(Exercise6Fragment.newInstance());
    }

    public void toDesignWithThreadPoolDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithThreadPoolDemonstrationFragment.newInstance());
    }

    public void toExercise7Screen() {
        mFragmentHelper.replaceFragment(Exercise7Fragment.newInstance());
    }

    public void toDesignWithAsyncTaskDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithAsyncTaskDemonstrationFragment.newInstance());
    }

    public void toThreadPosterDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithThreadPosterDemonstrationFragment.newInstance());
    }

    public void toExercise8Screen() {
        mFragmentHelper.replaceFragment(Exercise8Fragment.newInstance());
    }

    public void toDesignWithRxJavaDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithRxJavaDemonstrationFragment.newInstance());
    }

    public void toExercise9Screen() {
        mFragmentHelper.replaceFragment(Exercise9Fragment.newInstance());
    }

    public void toDesignWithCoroutinesDemonstration() {
        mFragmentHelper.replaceFragment(DesignWithCoroutinesDemonstrationFragment.Companion.newInstance());
    }

    public void toExercise10Screen() {
        mFragmentHelper.replaceFragment(Exercise10Fragment.Companion.newInstance());
    }
}
