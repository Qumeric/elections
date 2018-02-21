package com.example.qumeric.elections

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.ctx
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.setContentView

class DucksGameActivity : AppCompatActivity() {
    private lateinit var view: DucksGameView

    private var score = 0

    public val ducks: MutableSet<ImageView> = mutableSetOf()

    val handler = Handler();
    val createDuck = object : Runnable {
        override fun run() {
            val duckView = ImageView(ctx)

            duckView.setX(displayMetrics.widthPixels.toFloat())

            duckView.setImageResource(R.drawable.ic_duck)

            view.layout.addView(duckView)
            ducks.add(duckView)

            handler.postDelayed(this, (1000).toLong())
        }
    }

    val update = object : Runnable {
        override fun run() {

            val toRemove: MutableList<ImageView> = mutableListOf()

            for (s in ducks) {
                s.x -= Math.sqrt(10.toDouble()+score).toFloat()
                s.invalidate()

                if (s.x <= 0) {
                    lose()
                }
            }

            for (s in toRemove) {
                ducks.remove(s)
                view.layout.removeView(s)
                score += 1
            }

            view.scoreText.setText(score.toString())

            handler.postDelayed(this, (1000/50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        view = DucksGameView()
        view.setContentView(this)

        handler.postDelayed(createDuck, 1)
        handler.postDelayed(update, 1)
    }

    fun shoot() {
        val ch_rc = Rect()
        view.crosshair.getHitRect(ch_rc)

        for (duck in ducks) {
            val duck_rc = Rect()
            duck.getHitRect(duck_rc)

            if (ch_rc.intersect(duck_rc)) {
                ducks.remove(duck)
                view.layout.removeView(duck)
                score += 1
                break
            }
        }
    }

    fun lose() {
        Log.d("DuckGameActivity", "LOSE")
    }

}
