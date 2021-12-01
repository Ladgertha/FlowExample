package ru.ladgertha.testflowapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var oldValueButton: Button
    private lateinit var newValueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        oldValueButton = findViewById(R.id.oldFlowButton)
        newValueButton = findViewById(R.id.newFlowButton)
        oldValueButton.setOnClickListener {
            mainViewModel.eventFromOldFlow()
        }
        newValueButton.setOnClickListener {
            mainViewModel.eventFromNewFlow()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(State.STARTED) {
                launch {
                    mainViewModel
                        .getOldFLow()
                        .filterNotNull()
                        .collect {
                            changeTextAndColor(it, oldValueButton)
                        }
                }

                launch {
                    mainViewModel
                        .getNewFlow()
                        .filterNotNull()
                        .collect {
                            changeTextAndColor(it, newValueButton)
                        }
                }
            }
        }
    }

    private fun changeTextAndColor(flowValue: Boolean, button: Button) {
        button.text = Random.nextInt().toString()
        if (flowValue) {
            button.setBackgroundColor(Color.BLACK)
        } else {
            button.setBackgroundColor(Color.GRAY)
        }
    }
}