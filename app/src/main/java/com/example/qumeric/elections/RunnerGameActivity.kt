package com.example.qumeric.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView

class RunnerGameActivity : DefaultActivity() {
    private lateinit var view: RunnerGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = RunnerGameView()
        view.setContentView(this)
    }
}