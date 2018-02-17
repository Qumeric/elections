package com.example.qumeric.elections

import android.graphics.drawable.Drawable
import android.media.Image
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onTouch

class CatcherGameView() : AnkoComponent<CatcherGameActivity> {
    private lateinit var ankoContext: AnkoContext<CatcherGameActivity>

    public lateinit var layout: RelativeLayout
    public lateinit var scoreText: TextView
    public lateinit var cart: ImageButton


    override fun createView(ui: AnkoContext<CatcherGameActivity>) = with(ui) {
        ankoContext = ui

        val cartDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_cart)
        val strawberryDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_strawberry)

        layout = relativeLayout {
            gravity = Gravity.NO_GRAVITY

            scoreText = textView {
            }

            cart = imageButton {
                backgroundDrawable = cartDrawable
                y = (displayMetrics.heightPixels-cartDrawable.intrinsicHeight).toFloat()
            }

            onTouch { view, e ->
                cart.x = e.getX()

                true
            }
        }
        layout
    }

    fun setScore(score: Int) {
    }
}
