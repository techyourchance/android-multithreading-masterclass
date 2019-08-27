package com.techyourchance.multithreading.demonstrations.uithread;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UiThreadDemonstrationFragment extends BaseFragment {

    private static final String TAG = "UiThreadDemonstration";

    public static Fragment newInstance() {
        return new UiThreadDemonstrationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logThreadInfo("onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_thread_demonstration, container, false);

        Button mBtnCallbackCheck = view.findViewById(R.id.btn_callback_check);
        mBtnCallbackCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logThreadInfo("button callback");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logThreadInfo("onViewCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        logThreadInfo("onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        logThreadInfo("onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        logThreadInfo("onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        logThreadInfo("onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logThreadInfo("onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logThreadInfo("onDestroy()");
    }

    @Override
    protected String getScreenTitle() {
        return "";
    }

    private void logThreadInfo(String eventName) {
        Log.d(TAG, "event\n"
                + eventName
                + "; thread name: " + Thread.currentThread().getName()
                + "; thread ID: " + Thread.currentThread().getId());

    }

}
