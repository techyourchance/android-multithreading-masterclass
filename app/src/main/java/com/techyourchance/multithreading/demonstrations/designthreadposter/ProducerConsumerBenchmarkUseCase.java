package com.techyourchance.multithreading.demonstrations.designthreadposter;

import com.techyourchance.multithreading.DefaultConfiguration;
import com.techyourchance.multithreading.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

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

    private final UiThreadPoster mUiThreadPoster = new UiThreadPoster();
    private final BackgroundThreadPoster mBackgroundThreadPoster = new BackgroundThreadPoster();

    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private int mNumOfFinishedConsumers;

    private int mNumOfReceivedMessages;

    private long mStartTimestamp;


    public void startBenchmarkAndNotify() {

        synchronized (LOCK) {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;
            mStartTimestamp = System.currentTimeMillis();
        }

        // watcher-reporter thread
        mBackgroundThreadPoster.post(() -> {
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
        mBackgroundThreadPoster.post(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        });

        // consumers init thread
        mBackgroundThreadPoster.post(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        });
    }


    private void startNewProducer(final int index) {
        mBackgroundThreadPoster.post(() -> {
            try {
                Thread.sleep(DefaultConfiguration.DEFAULT_PRODUCER_DELAY_MS);
            } catch (InterruptedException e) {
                return;
            }
            mBlockingQueue.put(index);
        });
    }

    private void startNewConsumer() {
        mBackgroundThreadPoster.post(() -> {
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
        mUiThreadPoster.post(() -> {
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
