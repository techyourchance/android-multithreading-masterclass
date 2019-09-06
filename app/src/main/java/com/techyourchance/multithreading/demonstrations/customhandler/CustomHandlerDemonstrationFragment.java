package com.techyourchance.multithreading.demonstrations.customhandler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


@SuppressLint("SetTextI18n")
public class CustomHandlerDemonstrationFragment extends BaseFragment {

    private static final int SECONDS_TO_COUNT = 5;

    public static Fragment newInstance() {
        return new CustomHandlerDemonstrationFragment();
    }

    private Button mBtnSendJob;

    private CustomHandler mCustomHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_looper_demonstration, container, false);

        mBtnSendJob = view.findViewById(R.id.btn_send_job);
        mBtnSendJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendJob();
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
        mCustomHandler = new CustomHandler();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCustomHandler.stop();
    }

    private void sendJob() {
        mCustomHandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i < SECONDS_TO_COUNT; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    Log.d("CustomHandler", "iteration: " + i);
                }
            }
        });
    }

    private class CustomHandler {

        private final Runnable POISON = new Runnable() {
            @Override
            public void run() {}
        };

        private final BlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

        public CustomHandler() {
            initWorkerThread();
        }

        private void initWorkerThread() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("CustomHandler", "worker (looper) thread initialized");
                    while (true) {
                        Runnable runnable;
                        try {
                            runnable = mQueue.take();
                        } catch (InterruptedException e) {
                            return;
                        }
                        if (runnable == POISON) {
                            Log.d("CustomHandler", "poison data detected; stopping working thread");
                            return;
                        }
                        runnable.run();
                    }
                }
            }).start();
        }

        public void stop() {
            Log.d("CustomHandler", "injecting poison data into the queue");
            mQueue.clear();
            mQueue.add(POISON);
        }

        public void post(Runnable job) {
            mQueue.add(job);
        }
    }
}
