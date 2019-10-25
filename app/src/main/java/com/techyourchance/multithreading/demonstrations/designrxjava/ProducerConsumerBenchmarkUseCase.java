package com.techyourchance.multithreading.demonstrations.designrxjava;

import android.util.Log;

import com.techyourchance.multithreading.DefaultConfiguration;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ProducerConsumerBenchmarkUseCase {

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

    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private long mStartTimestamp;


    public Observable<Result> startBenchmark() {
        return Flowable.range(0, NUM_OF_MESSAGES)
                        .flatMap(id -> Flowable
                                .fromCallable(() -> {
                                    try {
                                        Thread.sleep(DefaultConfiguration.DEFAULT_PRODUCER_DELAY_MS);
                                    } catch (InterruptedException e) {
                                        return id;
                                    }
                                    mBlockingQueue.put(id);
                                    return id;
                                }) // <-- generate message
                                .subscribeOn(Schedulers.io())
                        )
                        .parallel(NUM_OF_MESSAGES)
                        .runOn(Schedulers.io())
                        .doOnNext(msg -> { mBlockingQueue.take(); })  // <-- process message
                        .sequential()
                        .count()
                        .doOnSubscribe(s -> { mStartTimestamp = System.currentTimeMillis(); })
                        .map(cnt -> new Result(System.currentTimeMillis() - mStartTimestamp, cnt.intValue()))
                        .toObservable();
    }


}
