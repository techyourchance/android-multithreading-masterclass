package com.techyourchance.multithreading.demonstrations.designcoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

import com.techyourchance.multithreading.R
import com.techyourchance.multithreading.common.BaseFragment
import androidx.fragment.app.Fragment

class DesignWithCoroutinesDemonstrationFragment : BaseFragment(), ProducerConsumerBenchmarkUseCase.Listener {

    private lateinit var btnStart: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var txtReceivedMessagesCount: TextView
    private lateinit var txtExecutionTime: TextView

    private lateinit var producerConsumerBenchmarkUseCase: ProducerConsumerBenchmarkUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        producerConsumerBenchmarkUseCase = ProducerConsumerBenchmarkUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_design_with_coroutines_demonstration, container, false)

        btnStart = view.findViewById(R.id.btn_start)
        progressBar = view.findViewById(R.id.progress)
        txtReceivedMessagesCount = view.findViewById(R.id.txt_received_messages_count)
        txtExecutionTime = view.findViewById(R.id.txt_execution_time)

        btnStart.setOnClickListener { _ ->
            btnStart.isEnabled = false
            txtReceivedMessagesCount.text = ""
            txtExecutionTime.text = ""
            progressBar.visibility = View.VISIBLE

            producerConsumerBenchmarkUseCase.startBenchmarkAndNotify()
        }

        return view
    }

    override fun getScreenTitle(): String {
        return ""
    }

    override fun onStart() {
        super.onStart()
        producerConsumerBenchmarkUseCase.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        producerConsumerBenchmarkUseCase.unregisterListener(this)
    }

    override fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        progressBar.visibility = View.INVISIBLE
        btnStart.isEnabled = true
        txtReceivedMessagesCount.text = "Received messages: ${result.numOfReceivedMessages}"
        txtExecutionTime.text = "Execution time: ${result.executionTime} ms"
    }

    companion object {
        fun newInstance(): Fragment {
            return DesignWithCoroutinesDemonstrationFragment()
        }
    }
}
