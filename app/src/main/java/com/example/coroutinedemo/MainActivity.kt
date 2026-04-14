package com.example.coroutinedemo

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val TAG = "CoroutineDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Activity created")
        setupSeekBar()
        setupButton()
    }

    private fun setupSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val count = if (progress < 1) 1 else progress
                val text = if (count == 1) "$count coroutine" else "$count coroutines"
                binding.countText.text = text
                Log.d(TAG, "SeekBar changed: $count")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupButton() {
        binding.launchButton.setOnClickListener {
            launchCoroutines()
        }
    }

    private fun launchCoroutines() {
        val count = binding.seekBar.progress
        if (count == 0) return

        Log.d(TAG, "Launching $count coroutines")

        binding.launchButton.isEnabled = false
        binding.statusText.text = "Запуск $count корутин..."

        var completed = 0

        for (i in 1..count) {
            scope.launch {
                delay(5000) // 5 секунд задержки
                completed++
                binding.statusText.text = "Завершена корутина $i из $count"

                if (completed == count) {
                    binding.launchButton.isEnabled = true
                    delay(2000)
                    binding.statusText.text = " Готов к работе"
                    Log.d(TAG, "All $count coroutines completed")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d(TAG, "Activity destroyed, coroutines cancelled")
    }
}