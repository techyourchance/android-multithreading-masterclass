package com.techyourchance.multithreading.demonstrations.designthreadpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simplified implementation of blocking queue.
 */
class MyBlockingQueue {

    private final Object QUEUE_LOCK = new Object();

    private final int mCapacity;
    private final Queue<Integer> mQueue = new LinkedList<>();

    private int mCurrentSize = 0;

    MyBlockingQueue(int capacity) {
        mCapacity = capacity;
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param number the element to add
     */
    public void put(int number) {
        synchronized (QUEUE_LOCK) {
            while (mCurrentSize >= mCapacity) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            mQueue.offer(number);
            mCurrentSize++;
            QUEUE_LOCK.notifyAll();
        }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     */
    public int take() {
        synchronized (QUEUE_LOCK) {
            while (mCurrentSize <= 0) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return 0;
                }
            }
            mCurrentSize--;
            QUEUE_LOCK.notifyAll();
            return mQueue.poll();
        }
    }
}
