package com.techyourchance.multithreading.demonstrations.designasynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.techyourchance.multithreading.DefaultConfiguration;
import com.techyourchance.multithreading.common.BaseObservable;

import androidx.annotation.UiThread;

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

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

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
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                synchronized (LOCK) {
                    while (mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            return null;
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifySuccess();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // producers init thread
        new Thread(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        }).start();

        // consumers init thread
        new Thread(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        }).start();
    }


    private void startNewProducer(final int index) {
        new Thread(() -> {
            try {
                Thread.sleep(DefaultConfiguration.DEFAULT_PRODUCER_DELAY_MS);
            } catch (InterruptedException e) {
                return;
            }
            mBlockingQueue.put(index);
        }).start();
    }

    private void startNewConsumer() {
        new Thread(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if (message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedConsumers++;
                LOCK.notifyAll();
            }
        }).start();
    }

    @UiThread
    private void notifySuccess() {
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
    }


}
