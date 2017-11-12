package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class ChangeView(val oldOpinions: Map<String, Int>): AnkoComponent<ChangeActivity> {
    private lateinit var ankoContext: AnkoContext<ChangeActivity>

    override fun createView(ui: AnkoContext<ChangeActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            for ((group, opinion) in gamestate.opinions) {
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("%s: %d -> %d", group, oldOpinions[group], opinion.value)
                }
            }
            button("OK") {
                onClick {
                    ctx.startActivity(ctx.intentFor<GameActivity>())
                }
            }
        }
    }
}