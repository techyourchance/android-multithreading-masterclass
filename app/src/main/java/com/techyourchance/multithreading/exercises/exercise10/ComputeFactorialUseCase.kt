package com.techyourchance.multithreading.exercises.exercise10

import android.os.Handler
import android.os.Looper

import com.techyourchance.multithreading.common.BaseObservable

import java.math.BigInteger

import androidx.annotation.WorkerThread
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ComputeFactorialUseCase : BaseObservable<ComputeFactorialUseCase.Listener>() {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val uiHandler = Handler(Looper.getMainLooper())

    private var numberOfThreads: Int = 0
    private var threadsComputationRanges: Array<ComputationRange?> = arrayOf()
    @Volatile private var threadsComputationResults: Array<BigInteger?> = arrayOf()
    private var numOfFinishedThreads = 0

    private var computationTimeoutTime: Long = 0

    private var abortComputation: Boolean = false

    interface Listener {
        fun onFactorialComputed(result: BigInteger)
        fun onFactorialComputationTimedOut()
        fun onFactorialComputationAborted()
    }

    override fun onLastListenerUnregistered() {
        super.onLastListenerUnregistered()
        reentrantLock.withLock {
            abortComputation = true
            lockCondition.signalAll()
        }
    }

    fun computeFactorialAndNotify(argument: Int, timeout: Int) {
        Thread {
            initComputationParams(argument, timeout)
            startComputation()
            waitForThreadsResultsOrTimeoutOrAbort()
            processComputationResults()
        }.start()
    }

    private fun initComputationParams(factorialArgument: Int, timeout: Int) {
        numberOfThreads = if (factorialArgument < 20)
            1
        else
            Runtime.getRuntime().availableProcessors()

        synchronized(reentrantLock) {
            numOfFinishedThreads = 0
            abortComputation = false
        }

        threadsComputationResults = arrayOfNulls(numberOfThreads)

        threadsComputationRanges = arrayOfNulls(numberOfThreads)

        initThreadsComputationRanges(factorialArgument)

        computationTimeoutTime = System.currentTimeMillis() + timeout
    }

    private fun initThreadsComputationRanges(factorialArgument: Int) {
        val computationRangeSize = factorialArgument / numberOfThreads

        var nextComputationRangeEnd = factorialArgument.toLong()
        for (i in numberOfThreads - 1 downTo 0) {
            threadsComputationRanges[i] = ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            )
            nextComputationRangeEnd = threadsComputationRanges[i]!!.start - 1
        }

        // add potentially "remaining" values to first thread's range
        threadsComputationRanges[0] = ComputationRange(1, threadsComputationRanges[0]!!.end)
    }

    @WorkerThread
    private fun startComputation() {
        for (i in 0 until numberOfThreads) {

            Thread {
                val rangeStart = threadsComputationRanges[i]!!.start
                val rangeEnd = threadsComputationRanges[i]!!.end
                var product = BigInteger("1")
                for (num in rangeStart..rangeEnd) {
                    if (isTimedOut()) {
                        break
                    }
                    product = product.multiply(BigInteger(num.toString()))
                }
                threadsComputationResults[i] = product

                reentrantLock.withLock {
                    numOfFinishedThreads++
                    lockCondition.signalAll()
                }

            }.start()
        }
    }

    @WorkerThread
    private fun waitForThreadsResultsOrTimeoutOrAbort() {
        reentrantLock.withLock {
            while (numOfFinishedThreads != numberOfThreads && !abortComputation && !isTimedOut()) {
                try {
                    lockCondition.await(remainingMillisToTimeout(), TimeUnit.MILLISECONDS)
                } catch (e: InterruptedException) {
                    return
                }

            }
        }
    }

    @WorkerThread
    private fun processComputationResults() {
        if (abortComputation) {
            notifyAborted()
            return
        }

        val result = computeFinalResult()

        // need to check for timeout after computation of the final result
        if (isTimedOut()) {
            notifyTimeout()
            return
        }

        notifySuccess(result)
    }

    @WorkerThread
    private fun computeFinalResult(): BigInteger {
        var result = BigInteger("1")
        for (i in 0 until numberOfThreads) {
            if (isTimedOut()) {
                break
            }
            result = result.multiply(threadsComputationResults[i])
        }
        return result
    }

    private fun remainingMillisToTimeout(): Long {
        return computationTimeoutTime - System.currentTimeMillis()
    }

    private fun isTimedOut(): Boolean {
        return System.currentTimeMillis() >= computationTimeoutTime
    }

    private fun notifySuccess(result: BigInteger) {
        uiHandler.post {
            for (listener in listeners) {
                listener.onFactorialComputed(result)
            }
        }
    }

    private fun notifyAborted() {
        uiHandler.post {
            for (listener in listeners) {
                listener.onFactorialComputationAborted()
            }
        }
    }

    private fun notifyTimeout() {
        uiHandler.post {
            for (listener in listeners) {
                listener.onFactorialComputationTimedOut()
            }
        }
    }


    private data class ComputationRange(val start: Long, val end: Long)
}
