package com.example.qumeric.elections

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.setContentView

class LadderGameActivity : AppCompatActivity() {
    private lateinit var view: LadderGameView

    private var score = 0

    private val length = 30 // in seconds

    private val handler = Handler()

    fun tap() {
        score++
    }

    private fun update() {
        view.scoreText.text = score.toString()
        handler.postDelayed(Runnable { update() }, 1000/50)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = LadderGameView()
        view.setContentView(this)

        handler.postDelayed(Runnable { update() }, 1)
        handler.postDelayed(Runnable { lose() }, 1000*length.toLong())
    }

    fun lose() {
        Log.d("LadderGameActivity", "LOSE")
    }

}
