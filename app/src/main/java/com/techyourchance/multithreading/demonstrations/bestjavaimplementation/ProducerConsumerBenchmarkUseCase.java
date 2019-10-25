package com.techyourchance.multithreading.demonstrations.bestjavaimplementation;

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

    public void startBenchmarkAndNotify() {
        mBackgroundThreadPoster.post(() -> {

            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;
            long startTimestamp = System.currentTimeMillis();

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

            waitForAllConsumersToFinish();

            Result result;
            synchronized (LOCK) {
                result = new Result(
                        System.currentTimeMillis() - startTimestamp,
                        mNumOfReceivedMessages
                );
            }

            notifySuccess(result);
            
        });

    }

    private void waitForAllConsumersToFinish() {
        synchronized (LOCK) {
            while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
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

    private void notifySuccess(Result result) {
        mUiThreadPoster.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }


}
