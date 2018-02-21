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

class HammerGameActivity : AppCompatActivity() {
    private lateinit var view: HammerGameView

    private var score = 0

    fun kill(imgResource: Int) {
        score += 1
    }

    public val enemies: MutableSet<ImageView> = mutableSetOf()

    val handler = Handler();

    fun createEnemy(elem: ImageButton) : Runnable {
        Log.d("HammerGameActivity", "run createEnemy")
        return object : Runnable {
            override fun run() {
                val r = view.pickRandomEnemyResource();
                elem.setImageResource(r);
                elem.visibility = View.VISIBLE
                elem.onClick {
                    elem.visibility = View.INVISIBLE
                    kill(r);
                }
            }
        }
    }


    val update = object : Runnable {
        override fun run() {
            view.scoreText.setText(score.toString())

            for (row in view.field) {
                for (elem in row) {
                    if (elem.visibility == View.INVISIBLE) {
                        handler.postDelayed(createEnemy(elem), (1000/(10+score)).toLong());
                    }
                }
            }

            handler.postDelayed(this, (1000/50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        view = HammerGameView()
        view.setContentView(this)

        handler.postDelayed(update, 1)
    }

    fun lose() {
        Log.d("HammerGameActivity", "LOSE")
    }

}
