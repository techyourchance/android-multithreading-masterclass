package com.techyourchance.multithreading.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.techyourchance.multithreading.common.BaseFragment;
import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.ScreensNavigator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends BaseFragment implements HomeArrayAdapter.Listener {

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private ScreensNavigator mScreensNavigator;

    private ListView mListScreensReachableFromHome;
    private HomeArrayAdapter mAdapterScreensReachableFromHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mScreensNavigator = getCompositionRoot().getScreensNavigator();

        mAdapterScreensReachableFromHome = new HomeArrayAdapter(requireContext(), this);
        mListScreensReachableFromHome = view.findViewById(R.id.list_screens);
        mListScreensReachableFromHome.setAdapter(mAdapterScreensReachableFromHome);

        mAdapterScreensReachableFromHome.addAll(ScreenReachableFromHome.values());
        mAdapterScreensReachableFromHome.notifyDataSetChanged();

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "Home Screen";
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return null;
    }

    @Override
    public void onScreenClicked(ScreenReachableFromHome screenReachableFromHome) {
        switch (screenReachableFromHome) {
            case EXERCISE_1:
                mScreensNavigator.toExercise1Screen();
                break;
            case EXERCISE_2:
                mScreensNavigator.toExercise2Screen();
                break;
            case UI_THREAD_DEMONSTRATION:
                mScreensNavigator.toUiThreadDemonstration();
                break;
            case UI_HANDLER_DEMONSTRATION:
                mScreensNavigator.toUiHandlerDemonstration();
                break;
        }
    }
}
