package com.techyourchance.multithreading.demonstrations.designcoroutines

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.techyourchance.multithreading.DefaultConfiguration
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

class ProducerConsumerBenchmarkUseCase {

    class Result(val executionTime: Long, val numOfReceivedMessages: Int)

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private val numOfReceivedMessages: AtomicInteger = AtomicInteger(0)
    private val numOfProducers: AtomicInteger = AtomicInteger(0)
    private val numOfConsumers: AtomicInteger = AtomicInteger(0)

    suspend fun startBenchmark() : Result {

        return withContext(Dispatchers.IO) {

            numOfReceivedMessages.set(0)
            numOfProducers.set(0)
            numOfConsumers.set(0)

            val startTimestamp = System.currentTimeMillis()

            // producers init coroutine
            val deferredProducers = async(Dispatchers.IO + NonCancellable) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }

            // consumers init coroutine
            val deferredConsumers = async(Dispatchers.IO + NonCancellable) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }

            awaitAll(deferredConsumers, deferredProducers)

            Result(
                    System.currentTimeMillis() - startTimestamp,
                    numOfReceivedMessages.get()
            )
        }

    }

    private fun CoroutineScope.startNewProducer(index: Int) = launch(Dispatchers.IO) {
        Log.d("Producer", "producer ${numOfProducers.incrementAndGet()} started; " +
                "on thread ${Thread.currentThread().name}");
        Thread.sleep(DefaultConfiguration.DEFAULT_PRODUCER_DELAY_MS.toLong())
        blockingQueue.put(index)
    }

    private fun CoroutineScope.startNewConsumer() = launch(Dispatchers.IO) {
        Log.d("Consumer", "consumer ${numOfConsumers.incrementAndGet()} started; " +
                "on thread ${Thread.currentThread().name}");
        val message = blockingQueue.take()
        if (message != -1) {
            numOfReceivedMessages.incrementAndGet()
        }
    }

    companion object {
        private const val NUM_OF_MESSAGES = DefaultConfiguration.DEFAULT_NUM_OF_MESSAGES
        private const val BLOCKING_QUEUE_CAPACITY = DefaultConfiguration.DEFAULT_BLOCKING_QUEUE_SIZE
    }

}
