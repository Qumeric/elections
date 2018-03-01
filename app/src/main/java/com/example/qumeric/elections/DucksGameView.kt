package com.example.qumeric.elections

import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class DucksGameView() : AnkoComponent<DucksGameActivity> {
    lateinit var ankoContext: AnkoContext<DucksGameActivity>

    lateinit var layout: RelativeLayout
    lateinit var scoreText: TextView
    lateinit var crosshair: ImageView

    override fun createView(ui: AnkoContext<DucksGameActivity>) = with(ui) {
        ankoContext = ui

        val crosshairDrawable = ContextCompat.getDrawable(ctx, R.drawable.ic_crosshair)
        val bulletDrawable = ContextCompat.getDrawable(ctx, R.drawable.ic_bullet)

        linearLayout {
            gravity = Gravity.NO_GRAVITY

            orientation = LinearLayout.VERTICAL

            linearLayout {
                scoreText = textView {
                    textResource = R.string.ducks_welcome
                    textColor = ContextCompat.getColor(ctx, R.color.white)
                }
                gravity = Gravity.CENTER
                backgroundResource = R.color.navy
            }.lparams(weight = 0.1f, width = matchParent, height = 0)

            layout = relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.white

                crosshair = imageView {
                    backgroundDrawable = crosshairDrawable
                    elevation = 100f
                }
            }.lparams(weight = 0.2f, width = matchParent, height = 0)

            linearLayout {
                backgroundResource = R.color.green
            }.lparams(weight = 0.05f, width = matchParent, height = 0)

            linearLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.blue

                imageButton {
                    backgroundDrawable = bulletDrawable

                    onClick {
                        (ctx as DucksGameActivity).shoot()
                    }
                }
            }.lparams(weight = 0.55f, width = matchParent, height = 0)
        }
    }
}
