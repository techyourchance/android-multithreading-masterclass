package com.techyourchance.multithreading.demonstrations.designrxjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DesignWithRxJavaDemonstrationFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new DesignWithRxJavaDemonstrationFragment();
    }

    private Button mBtnStart;
    private ProgressBar mProgressBar;
    private TextView mTxtReceivedMessagesCount;
    private TextView mTxtExecutionTime;

    private ProducerConsumerBenchmarkUseCase mProducerConsumerBenchmarkUseCase;

    private @Nullable Disposable mDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProducerConsumerBenchmarkUseCase = new ProducerConsumerBenchmarkUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_with_thread_demonstration, container, false);

        mBtnStart = view.findViewById(R.id.btn_start);
        mProgressBar = view.findViewById(R.id.progress);
        mTxtReceivedMessagesCount = view.findViewById(R.id.txt_received_messages_count);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);

        mBtnStart.setOnClickListener(v -> {
            mBtnStart.setEnabled(false);
            mTxtReceivedMessagesCount.setText("");
            mTxtExecutionTime.setText("");
            mProgressBar.setVisibility(View.VISIBLE);

            mDisposable = mProducerConsumerBenchmarkUseCase.startBenchmark()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBenchmarkCompleted);
        });

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "";
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mBtnStart.setEnabled(true);
        mTxtReceivedMessagesCount.setText("Received messages: " + result.getNumOfReceivedMessages());
        mTxtExecutionTime.setText("Execution time: " + result.getExecutionTime() + "ms");
    }
}
