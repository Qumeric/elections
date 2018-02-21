package com.example.qumeric.elections

import android.view.Gravity
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onTouch

class CatcherGameView() : AnkoComponent<CatcherGameActivity> {
    private lateinit var ankoContext: AnkoContext<CatcherGameActivity>

    lateinit var layout: RelativeLayout
    lateinit var scoreText: TextView
    lateinit var cart: ImageButton

    override fun createView(ui: AnkoContext<CatcherGameActivity>) = with(ui) {
        ankoContext = ui

        val cartDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_cart, ctx.theme)

        layout = relativeLayout {
            gravity = Gravity.NO_GRAVITY

            scoreText = textView {
            }

            cart = imageButton {
                backgroundDrawable = cartDrawable
                y = (displayMetrics.heightPixels-cartDrawable.intrinsicHeight).toFloat()
            }

            onTouch { _, e ->
                cart.x = e.x
                true
            }
        }
        layout
    }
}
