package com.techyourchance.multithreading.demonstrations.bestjavaimplementation;

import com.techyourchance.multithreading.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.math.BigInteger;

import androidx.annotation.WorkerThread;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(BigInteger result);
        void onFactorialComputationTimedOut();
    }

    private final Object LOCK = new Object();

    private final BackgroundThreadPoster mBackgroundThreadPoster;
    private final UiThreadPoster mUiThreadPoster;

    private BigInteger[] mRangesComputationResults;

    private int mNumOfFinishedThreads = 0;

    private long mComputationTimeoutTime;

    private boolean mAbortComputation;

    public ComputeFactorialUseCase(UiThreadPoster uiThreadPoster, BackgroundThreadPoster backgroundThreadPoster) {
        mBackgroundThreadPoster = backgroundThreadPoster;
        mUiThreadPoster = uiThreadPoster;
    }

    @Override
    protected void onLastListenerUnregistered() {
        super.onLastListenerUnregistered();
        synchronized (LOCK) {
            mAbortComputation = true;
            LOCK.notifyAll();
        }
    }

    public void computeFactorialAndNotify(final int argument, final int timeout) {
        mBackgroundThreadPoster.post(() -> {

            synchronized (LOCK) {
                mNumOfFinishedThreads = 0;
                mAbortComputation = false;
                mComputationTimeoutTime = System.currentTimeMillis() + timeout;
            }

            ComputationRange[] computationRanges = getComputationRanges(argument);

            startComputation(computationRanges);

            waitForResultsOrTimeoutOrAbort();

            processComputationResults();

        });
    }

    private ComputationRange[] getComputationRanges(int factorialArgument) {

        int numberOfThreads = factorialArgument < 20
                ? 1 : Runtime.getRuntime().availableProcessors();

        ComputationRange[] computationRanges = new ComputationRange[numberOfThreads];

        int computationRangeSize = factorialArgument / numberOfThreads;

        long nextComputationRangeEnd = factorialArgument;
        for (int i = numberOfThreads - 1; i >= 0; i--) {
            computationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = computationRanges[i].start - 1;
        }

        // add potentially "remaining" values to first thread's range
        computationRanges[0] = new ComputationRange(1, computationRanges[0].end);

        return computationRanges;
    }

    @WorkerThread
    private void startComputation(ComputationRange[] computationRanges) {
        mRangesComputationResults = new BigInteger[computationRanges.length];

        for (int i = 0; i < computationRanges.length; i++) {
            startRangeComputation(computationRanges[i], i);
        }
    }

    private void startRangeComputation(ComputationRange computationRange, int rangeIndex) {
        mBackgroundThreadPoster.post(() -> {
            long rangeStart = computationRange.start;
            long rangeEnd = computationRange.end;

            BigInteger product = new BigInteger("1");
            for (long num = rangeStart; num <= rangeEnd; num++) {
                if (isTimedOut()) {
                    break;
                }
                product = product.multiply(new BigInteger(String.valueOf(num)));
            }

            synchronized (LOCK) {
                mRangesComputationResults[rangeIndex] = product;
                mNumOfFinishedThreads++;
                LOCK.notifyAll();
            }
        });
    }

    @WorkerThread
    private void waitForResultsOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while (!isCompleted()
                    && !isAborted()
                    && !isTimedOut()) {
                try {
                    LOCK.wait(getRemainingMillisToTimeout());
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private boolean isCompleted() {
        synchronized (LOCK) {
            return mNumOfFinishedThreads == mRangesComputationResults.length;
        }
    }

    @WorkerThread
    private void processComputationResults() {
        if (isAborted()) {
            return;
        }

        BigInteger result = computeFinalResult();

        // need to check for timeout after computation of the final result
        if (isTimedOut()) {
            notifyTimeout();
            return;
        }

        notifySuccess(result);
    }

    @WorkerThread
    private BigInteger computeFinalResult() {
        synchronized (LOCK) {
            BigInteger result = new BigInteger("1");
            for (BigInteger partialResult : mRangesComputationResults) {
                if (isTimedOut()) {
                    break;
                }
                result = result.multiply(partialResult);
            }
            return result;
        }
    }

    private long getRemainingMillisToTimeout() {
        return mComputationTimeoutTime - System.currentTimeMillis();
    }

    private boolean isTimedOut() {
        return System.currentTimeMillis() >= mComputationTimeoutTime;
    }

    private boolean isAborted() {
        synchronized (LOCK) {
            return mAbortComputation;
        }
    }

    private void notifySuccess(final BigInteger result) {
        mUiThreadPoster.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    private void notifyTimeout() {
        mUiThreadPoster.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onFactorialComputationTimedOut();
            }
        });
    }

    private static class ComputationRange {
        final long start;
        final long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
