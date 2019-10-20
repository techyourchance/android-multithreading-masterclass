package com.techyourchance.multithreading.demonstrations.designcoroutines

import android.os.Handler
import android.os.Looper

import com.techyourchance.multithreading.common.BaseObservable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ProducerConsumerBenchmarkUseCase {

    class Result(val executionTime: Long, val numOfReceivedMessages: Int)

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val uiHandler = Handler(Looper.getMainLooper())

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfFinishedConsumers: Int = 0

    private var numOfReceivedMessages: Int = 0

    private var startTimestamp: Long = 0

    suspend fun startBenchmark() : Result {

        withContext(Dispatchers.IO) {

            reentrantLock.withLock {
                numOfReceivedMessages = 0
                numOfFinishedConsumers = 0
                startTimestamp = System.currentTimeMillis()
            }

            // producers init thread
            Thread {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }.start()

            // consumers init thread
            Thread {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }.start()


            reentrantLock.withLock {
                while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        lockCondition.await()
                    } catch (e: InterruptedException) {
                        return@withLock
                    }
                }
            }
        }

        reentrantLock.withLock {
            return Result(
                    System.currentTimeMillis() - startTimestamp,
                    numOfReceivedMessages
            )
        }
    }


    private fun startNewProducer(index: Int) {
        Thread { blockingQueue.put(index) }.start()
    }

    private fun startNewConsumer() {
        Thread {
            val message = blockingQueue.take()
            reentrantLock.withLock {
                if (message != -1) {
                    numOfReceivedMessages++
                }
                numOfFinishedConsumers++
                lockCondition.signalAll()
            }
        }.start()
    }


    companion object {
        private const val NUM_OF_MESSAGES = 1000
        private const val BLOCKING_QUEUE_CAPACITY = 5
    }

}
