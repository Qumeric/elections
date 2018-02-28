package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class NewGameView : AnkoComponent<NewGameActivity> {
    private lateinit var ankoContext: AnkoContext<NewGameActivity>

    override fun createView(ui: AnkoContext<NewGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageButton {
                imageResource = R.drawable.play
                onClick {
                    ctx.startActivity(ctx.intentFor<MainActivity>())
                }
            }
        }
    }
}
