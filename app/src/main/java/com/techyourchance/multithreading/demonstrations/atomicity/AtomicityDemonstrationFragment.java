package com.techyourchance.multithreading.demonstrations.atomicity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


@SuppressLint("SetTextI18n")
public class AtomicityDemonstrationFragment extends BaseFragment {

    private static final int COUNT_UP_TO = 1000;
    private static final int NUM_OF_COUNTER_THREADS = 100;

    public static Fragment newInstance() {
        return new AtomicityDemonstrationFragment();
    }

    private Button mBtnStartCount;
    private TextView mTxtFinalCount;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    private volatile int mCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atomicity_demonstration, container, false);

        mTxtFinalCount = view.findViewById(R.id.txt_final_count);

        mBtnStartCount = view.findViewById(R.id.btn_start_count);
        mBtnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCount();
            }
        });

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void startCount() {
        mCount = 0;
        mTxtFinalCount.setText("");
        mBtnStartCount.setEnabled(false);

        for (int i = 0; i < NUM_OF_COUNTER_THREADS; i++) {
            startCountThread();
        }

        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTxtFinalCount.setText(String.valueOf(mCount));
                mBtnStartCount.setEnabled(true);
            }
        }, NUM_OF_COUNTER_THREADS * 20);
    }

    private void startCountThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < COUNT_UP_TO; i++) {
                    mCount++;
                }
            }
        }).start();
    }

}
