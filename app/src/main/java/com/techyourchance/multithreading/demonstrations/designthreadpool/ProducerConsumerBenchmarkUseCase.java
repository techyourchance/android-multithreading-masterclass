package com.techyourchance.multithreading.demonstrations.designthreadpool;

import android.os.Handler;

import com.techyourchance.multithreading.DefaultConfiguration;
import com.techyourchance.multithreading.common.BaseObservable;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public static interface Listener {
        void onBenchmarkCompleted(Result result);
    }

    public static class Result {
        private final long mExecutionTime;
        private final int mNumOfReceivedMessages;

        public Result(long executionTime, int numOfReceivedMessages) {
            mExecutionTime = executionTime;
            mNumOfReceivedMessages = numOfReceivedMessages;
        }

        public long getExecutionTime() {
            return mExecutionTime;
        }

        public int getNumOfReceivedMessages() {
            return mNumOfReceivedMessages;
        }
    }

    private static final int NUM_OF_MESSAGES = DefaultConfiguration.DEFAULT_NUM_OF_MESSAGES;
    private static final int BLOCKING_QUEUE_CAPACITY = DefaultConfiguration.DEFAULT_BLOCKING_QUEUE_SIZE;

    private final Object LOCK = new Object();

    private final Handler mUiHandler;

    private final AtomicInteger mNumOfThreads = new AtomicInteger(0);

    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private final ThreadPoolExecutor mThreadPool;

    private int mNumOfFinishedConsumers;

    private int mNumOfReceivedMessages;

    private long mStartTimestamp;

    public ProducerConsumerBenchmarkUseCase(Handler uiHandler, ThreadPoolExecutor threadPool) {
        mUiHandler = uiHandler;
        mThreadPool = threadPool;
    }

    public void startBenchmarkAndNotify() {

        synchronized (LOCK) {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;
            mStartTimestamp = System.currentTimeMillis();
            mNumOfThreads.set(0);
        }

        // watcher-reporter thread
        mThreadPool.execute(() -> {
            synchronized (LOCK) {
                while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            notifySuccess();
        });

        // producers init thread
        mThreadPool.execute(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        });

        // consumers init thread
        mThreadPool.execute(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        });
    }


    private void startNewProducer(final int index) {
        mThreadPool.execute(() -> {
            try {
                Thread.sleep(DefaultConfiguration.DEFAULT_PRODUCER_DELAY_MS);
            } catch (InterruptedException e) {
                return;
            }
            mBlockingQueue.put(index);
        });
    }

    private void startNewConsumer() {
        mThreadPool.execute(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if (message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedConsumers++;
                LOCK.notifyAll();
            }
        });
    }

    private void notifySuccess() {
        mUiHandler.post(() -> {
            Result result;
            synchronized (LOCK) {
                 result =
                        new Result(
                                System.currentTimeMillis() - mStartTimestamp,
                                mNumOfReceivedMessages
                        );
            }
            for (Listener listener : getListeners()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }


}
