package com.example.qumeric.elections

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.setContentView

class LadderGameActivity : MiniGameActivity() {
    private lateinit var view: LadderGameView

    private val length = 30 // in seconds

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


        drawInformationDialog(getString(R.string.ladder_info_title), getString(R.string.ladder_info_message),
                {
                    handler.postDelayed({update()}, 1)
                    handler.postDelayed({lose()}, 1000 * length.toLong())
                }, view.ankoContext)

    }
}
