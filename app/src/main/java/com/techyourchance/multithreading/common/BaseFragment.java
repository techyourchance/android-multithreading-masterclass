package com.techyourchance.multithreading.common;

import android.os.Bundle;
import android.view.View;

import com.techyourchance.fragmenthelper.HierarchicalFragment;
import com.techyourchance.multithreading.MyApplication;
import com.techyourchance.multithreading.common.dependencyinjection.PresentationCompositionRoot;
import com.techyourchance.multithreading.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements HierarchicalFragment {

    private PresentationCompositionRoot mPresentationCompositionRoot;

    protected final PresentationCompositionRoot getCompositionRoot() {
        if (mPresentationCompositionRoot == null) {
            mPresentationCompositionRoot = new PresentationCompositionRoot(
                    requireActivity(),
                    ((MyApplication)requireActivity().getApplication()).getApplicationCompositionRoot()
            );
        }
        return mPresentationCompositionRoot;
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return HomeFragment.newInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ToolbarManipulator toolbarManipulator = getCompositionRoot().getToolbarManipulator();
        toolbarManipulator.setScreenTitle(getScreenTitle());
        if (getHierarchicalParentFragment() != null) {
            toolbarManipulator.showUpButton();
        } else {
            toolbarManipulator.hideUpButton();
        }
    }

    protected abstract String getScreenTitle();

}
