package com.techyourchance.multithreading.demonstrations.designcoroutines

import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Simplified implementation of blocking queue.
 */
internal class MyBlockingQueue(private val capacity: Int) {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val queue = LinkedList<Int>()

    private var currentSize = 0

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param number the element to add
     */
    fun put(number: Int) {
        reentrantLock.withLock {
            while (currentSize >= capacity) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return
                }
            }
            queue.offer(number)
            currentSize++
            lockCondition.signalAll()
        }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     */
    fun take(): Int {
        reentrantLock.withLock {
            while (currentSize <= 0) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return 0
                }
            }
            currentSize--
            lockCondition.signalAll()
            return queue.poll()
        }
    }
}
