package com.techyourchance.multithreading.solutions.exercise9;

import android.os.Handler;
import android.os.Looper;

import com.techyourchance.multithreading.common.BaseObservable;

import java.math.BigInteger;

import androidx.annotation.WorkerThread;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ComputeFactorialUseCase {

    public static class Result {
        private final boolean mIsAborted;
        private final boolean mIsTimedOut;
        private final BigInteger mResult;

        public Result(boolean isAborted, boolean isTimedOut, BigInteger result) {
            mIsAborted = isAborted;
            mIsTimedOut = isTimedOut;
            mResult = result;
        }

        public boolean isAborted() {
            return mIsAborted;
        }

        public boolean isTimedOut() {
            return mIsTimedOut;
        }

        public BigInteger getResult() {
            return mResult;
        }
    }

    private int mNumberOfThreads;
    private ComputationRange[] mThreadsComputationRanges;

    private long mComputationTimeoutTime;

    public Observable<Result> computeFactorial(final int argument, final int timeout) {

        initComputationParams(argument, timeout);

        return Flowable
                .range(0, mNumberOfThreads)
                .parallel(mNumberOfThreads)
                .runOn(Schedulers.io())
                .map(this::computePart)
                .sequential()
                .scan((bigInteger, bigInteger2) -> {
                    if (isTimedOut()) {
                        return bigInteger;
                    } else {
                        return bigInteger.multiply(bigInteger2);
                    }
                })
                .last(new BigInteger("0"))
                .map(result -> {
                    if (isTimedOut()) {
                        return new Result(false, true, result);
                    }
                    return new Result(false, false, result);
                })
                .onErrorReturnItem(new Result(true, false, new BigInteger("0")))
                .toObservable();
    }

    @WorkerThread
    private BigInteger computePart(int id) {
        long rangeStart = mThreadsComputationRanges[id].start;
        long rangeEnd = mThreadsComputationRanges[id].end;
        BigInteger product = new BigInteger("1");
        for (long num = rangeStart; num <= rangeEnd; num++) {
            if (isTimedOut()) {
                break;
            }
            product = product.multiply(new BigInteger(String.valueOf(num)));
        }
        return product;
    }

    private void initComputationParams(int factorialArgument, int timeout) {
        mNumberOfThreads = factorialArgument < 20
                ? 1 : Runtime.getRuntime().availableProcessors();

        mThreadsComputationRanges = new ComputationRange[mNumberOfThreads];

        initThreadsComputationRanges(factorialArgument);

        mComputationTimeoutTime = System.currentTimeMillis() + timeout;
    }

    private void initThreadsComputationRanges(int factorialArgument) {
        int computationRangeSize = factorialArgument / mNumberOfThreads;

        long nextComputationRangeEnd = factorialArgument;
        for (int i = mNumberOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = mThreadsComputationRanges[i].start - 1;
        }

        // add potentially "remaining" values to first thread's range
        mThreadsComputationRanges[0].start = 1;
    }

    private long getRemainingMillisToTimeout() {
        return mComputationTimeoutTime - System.currentTimeMillis();
    }

    private boolean isTimedOut() {
        return System.currentTimeMillis() >= mComputationTimeoutTime;
    }

    private static class ComputationRange {
        private long start;
        private long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
