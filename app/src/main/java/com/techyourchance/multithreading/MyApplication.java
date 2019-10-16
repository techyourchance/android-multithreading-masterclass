package com.techyourchance.multithreading;

import android.app.Application;

import com.techyourchance.multithreading.common.dependencyinjection.ApplicationCompositionRoot;

public class MyApplication extends Application {

    private final ApplicationCompositionRoot mApplicationCompositionRoot =
            new ApplicationCompositionRoot();

    public ApplicationCompositionRoot getApplicationCompositionRoot() {
        return mApplicationCompositionRoot;
    }
}
