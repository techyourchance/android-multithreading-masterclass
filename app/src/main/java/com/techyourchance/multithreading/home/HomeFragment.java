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
            case CUSTOM_HANDLER_DEMONSTRATION:
                mScreensNavigator.toCustomHandlerDemonstration();
                break;
            case EXERCISE_3:
                mScreensNavigator.toExercise3Screen();
                break;
            case ATOMICITY_DEMONSTRATION:
                mScreensNavigator.toAtomicityDemonstration();
                break;
            case EXERCISE_4:
                mScreensNavigator.toExercise4Screen();
                break;
            case THREAD_WAIT_DEMONSTRATION:
                mScreensNavigator.toThreadWaitDemonstration();
                break;
            case EXERCISE_5:
                mScreensNavigator.toExercise5Screen();
                break;
            case DESIGN_WITH_THREADS_DEMONSTRATION:
                mScreensNavigator.toDesignWithThreadsDemonstration();
                break;
            case EXERCISE_6:
                mScreensNavigator.toExercise6Screen();
                break;
            case DESIGN_WITH_THREAD_POOL_DEMONSTRATION:
                mScreensNavigator.toDesignWithThreadPoolDemonstration();
                break;
            case EXERCISE_7:
                mScreensNavigator.toExercise7Screen();
                break;
            case DESIGN_WITH_ASYNCTASK_DEMONSTRATION:
                mScreensNavigator.toDesignWithAsyncTaskDemonstration();
                break;
            case DESIGN_WITH_THREAD_POSTER_DEMONSTRATION:
                mScreensNavigator.toThreadPosterDemonstration();
                break;
            case EXERCISE_8:
                mScreensNavigator.toExercise8Screen();
                break;
            case DESIGN_WITH_RX_JAVA_DEMONSTRATION:
                mScreensNavigator.toDesignWithRxJavaDemonstration();
                break;
            case EXERCISE_9:
                mScreensNavigator.toExercise9Screen();
                break;
            case DESIGN_WITH_COROUTINES_DEMONSTRATION:
                mScreensNavigator.toDesignWithCoroutinesDemonstration();
                break;
            case EXERCISE_10:
                mScreensNavigator.toExercise10Screen();
                break;
        }
    }
}
