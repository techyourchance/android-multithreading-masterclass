package com.techyourchance.multithreading.demonstrations.designcoroutines

import android.os.Handler
import android.os.Looper

import com.techyourchance.multithreading.common.BaseObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ProducerConsumerBenchmarkUseCase {

    class Result(val executionTime: Long, val numOfReceivedMessages: Int)

    private val uiHandler = Handler(Looper.getMainLooper())

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: AtomicInteger = AtomicInteger(0)

    @Volatile private var startTimestamp: Long = 0

    suspend fun startBenchmark() : Result {

        withContext(Dispatchers.IO) {

            numOfReceivedMessages.set(0)
            startTimestamp = System.currentTimeMillis()
            
            // producers init coroutine
            launch(Dispatchers.IO) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }

            // consumers init coroutine
            launch(Dispatchers.IO) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }

        }

        return Result(
                System.currentTimeMillis() - startTimestamp,
                numOfReceivedMessages.get()
        )

    }


    private fun CoroutineScope.startNewProducer(index: Int) = launch(Dispatchers.IO) {
        blockingQueue.put(index)
    }

    private fun CoroutineScope.startNewConsumer() = launch(Dispatchers.IO) {
        val message = blockingQueue.take()
        if (message != -1) {
            numOfReceivedMessages.incrementAndGet()
        }
    }


    companion object {
        private const val NUM_OF_MESSAGES = 1000
        private const val BLOCKING_QUEUE_CAPACITY = 5
    }

}
