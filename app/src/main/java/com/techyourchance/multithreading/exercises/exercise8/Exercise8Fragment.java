package com.techyourchance.multithreading.exercises.exercise8;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techyourchance.multithreading.DefaultConfiguration;
import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import java.math.BigInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Exercise8Fragment extends BaseFragment implements ComputeFactorialUseCase.Listener {

    public static Fragment newInstance() {
        return new Exercise8Fragment();
    }

    private static int MAX_TIMEOUT_MS = DefaultConfiguration.DEFAULT_FACTORIAL_TIMEOUT_MS;

    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private Button mBtnStartWork;
    private TextView mTxtResult;

    private ComputeFactorialUseCase mComputeFactorialUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComputeFactorialUseCase = new ComputeFactorialUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_8, container, false);

        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mTxtResult = view.findViewById(R.id.txt_result);

        mBtnStartWork.setOnClickListener(v -> {
            if (mEdtArgument.getText().toString().isEmpty()) {
                return;
            }

            mTxtResult.setText("");
            mBtnStartWork.setEnabled(false);


            InputMethodManager imm =
                    (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);

            int argument = Integer.valueOf(mEdtArgument.getText().toString());

            mComputeFactorialUseCase.computeFactorialAndNotify(argument, getTimeout());
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mComputeFactorialUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mComputeFactorialUseCase.unregisterListener(this);

    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 8";
    }

    private int getTimeout() {
        int timeout;
        if (mEdtTimeout.getText().toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.valueOf(mEdtTimeout.getText().toString());
            if (timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS;
            }
        }
        return timeout;
    }

    @Override
    public void onFactorialComputed(BigInteger result) {
        mTxtResult.setText(result.toString());
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationTimedOut() {
        mTxtResult.setText("Computation timed out");
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationAborted() {
        mTxtResult.setText("Computation aborted");
        mBtnStartWork.setEnabled(true);
    }
}
