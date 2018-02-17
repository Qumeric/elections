package com.example.qumeric.elections

import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import org.jetbrains.anko.ctx
import org.jetbrains.anko.setContentView
import java.lang.Math.random
import java.lang.Math.sqrt

class CatcherGameActivity : AppCompatActivity() {
    private lateinit var view: CatcherGameView

    private var score = 0

    public val strawberries: MutableSet<ImageView> = mutableSetOf()

    val handler = Handler();
    val createStrawberry = object : Runnable {
        override fun run() {

            val strawberryView = ImageView(ctx)

            val x: Float = (random()*(view.layout.width-strawberryView.width*2)).toFloat()

            strawberryView.setImageResource(R.drawable.ic_strawberry)
            strawberryView.setX(x)

            view.layout.addView(strawberryView)
            strawberries.add(strawberryView)

            Log.d("CatcherGameActivity", "added strawberry, size now is " + strawberries.size.toString())

            //handler.postDelayed(this, (1000/(score+1)).toLong())
            handler.postDelayed(this, (1000).toLong())
        }
    }

    val update = object : Runnable {
        override fun run() {

            val cart_rc = Rect()
            view.cart.getHitRect(cart_rc)

            val toRemove: MutableList<ImageView> = mutableListOf()

            for (s in strawberries) {
                s.y += Math.sqrt(10.toDouble()+score).toFloat()
                s.invalidate()

                val s_rc = Rect()
                s.getHitRect(s_rc)

                if (cart_rc.intersect(s_rc)) {
                    toRemove.add(s)
                }

                if (s.y+s.height >= view.layout.height) {
                    lose()
                }
            }

            for (s in toRemove) {
                strawberries.remove(s)
                view.layout.removeView(s)
                score += 1
            }

            view.scoreText.setText(score.toString())

            handler.postDelayed(this, (1000/50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        view = CatcherGameView()
        view.setContentView(this)

        handler.postDelayed(createStrawberry, 1)
        handler.postDelayed(update, 1)
    }

    fun lose() {
        Log.d("CatcherGameActivity", "LOSE")
    }

}
