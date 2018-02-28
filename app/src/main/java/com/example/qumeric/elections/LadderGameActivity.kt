package com.example.qumeric.elections

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.setContentView

class LadderGameActivity : DefaultActivity() {
    private lateinit var view: LadderGameView

    private var score = 0

    private val length = 30 // in seconds

    private val handler = Handler()

    fun tap() {
        score++
    }

    private fun update() {
        view.scoreText.text = score.toString()
        handler.postDelayed({ update() }, 1000 / 50)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = LadderGameView()
        view.setContentView(this)

        handler.postDelayed({ update() }, 1)
        handler.postDelayed({ lose() }, 1000 * length.toLong())
    }

    fun lose() {
        Log.d("LadderGameActivity", "LOSE")
        handler.removeCallbacksAndMessages(null)
    }

}
