package com.techyourchance.multithreading.demonstrations.designcoroutines

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.techyourchance.multithreading.R
import com.techyourchance.multithreading.common.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DesignWithCoroutinesDemonstrationFragment : BaseFragment() {

    private lateinit var btnStart: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var txtReceivedMessagesCount: TextView
    private lateinit var txtExecutionTime: TextView
    private lateinit var viewUiNonBlockedIndicator : View

    private lateinit var producerConsumerBenchmarkUseCase: ProducerConsumerBenchmarkUseCase

    private var showUiNonBlockedIndication : Boolean = false

    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        producerConsumerBenchmarkUseCase = ProducerConsumerBenchmarkUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_design_with_coroutines_demonstration, container, false)

        view.apply {
            btnStart = findViewById(R.id.btn_start)
            progressBar = findViewById(R.id.progress)
            txtReceivedMessagesCount = findViewById(R.id.txt_received_messages_count)
            txtExecutionTime = findViewById(R.id.txt_execution_time)
            viewUiNonBlockedIndicator = findViewById(R.id.view_ui_non_blocked_indicator)
        }

        btnStart.setOnClickListener { _ ->
            btnStart.isEnabled = false
            txtReceivedMessagesCount.text = ""
            txtExecutionTime.text = ""
            progressBar.visibility = VISIBLE

            job = CoroutineScope(Dispatchers.Main).launch {
                val result = producerConsumerBenchmarkUseCase.startBenchmark()
                onBenchmarkCompleted(result)
            }
        }

        return view
    }

    override fun getScreenTitle(): String {
        return ""
    }

    override fun onStart() {
        super.onStart()
        showUiNonBlockedIndication = true
        postUiNonBlockedIndication()
    }
    override fun onStop() {
        Log.d("FragmentCoroutinesDemo", "onStop() called")
        super.onStop()
        showUiNonBlockedIndication = false
        job?.apply { cancel() }
    }

    private fun postUiNonBlockedIndication() {
        Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (showUiNonBlockedIndication) {
                        val indicatorVisible = viewUiNonBlockedIndicator.visibility == VISIBLE
                        viewUiNonBlockedIndicator.visibility = if (indicatorVisible) INVISIBLE else VISIBLE
                        postUiNonBlockedIndication()
                    }
                },
                500
        )
    }


    fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        Log.d("FragmentCoroutinesDemo", "onBenchmarkCompleted() called")
        progressBar.visibility = INVISIBLE
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
