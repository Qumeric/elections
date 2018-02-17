package com.example.qumeric.elections

import android.graphics.drawable.Drawable
import android.media.Image
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onTouch

class DucksGameView() : AnkoComponent<DucksGameActivity> {
    private lateinit var ankoContext: AnkoContext<DucksGameActivity>

    public lateinit var layout: RelativeLayout
    public lateinit var scoreText: TextView
    public lateinit var crosshair: ImageView

    override fun createView(ui: AnkoContext<DucksGameActivity>) = with(ui) {
        ankoContext = ui

        ui.ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val crosshairDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_crosshair)
        val bulletDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_bullet)

        linearLayout {
            gravity = Gravity.NO_GRAVITY

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                backgroundResource = R.color.darkBlue
                scoreText = textView { }
            }.lparams(weight = 0.1f, width = matchParent)

            layout = relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.white

                crosshair = imageView {
                    backgroundDrawable = crosshairDrawable
                    elevation = 100f
                }
            }.lparams(weight = 0.2f, width = matchParent)

            relativeLayout {
                backgroundResource = R.color.grass
            }.lparams(weight = 0.05f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.brightBlue

                imageButton {
                    backgroundDrawable = bulletDrawable

                    onClick {
                        (ui.ctx as DucksGameActivity).shoot()
                    }
                }
            }.lparams(weight = 0.55f, width = matchParent)
        }
    }
}
