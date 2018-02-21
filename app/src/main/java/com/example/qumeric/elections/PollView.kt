package com.example.qumeric.elections

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class PollView(): AnkoComponent<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>
    lateinit var plotView: LinearLayout

    lateinit var expelledTV: TextView

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                gravity = Gravity.CENTER
                text = String.format("Poll results on day %d", gamestate.step)
            }.lparams(weight = 1f)

            plotView = include<LinearLayout>(R.layout.rating_plot) {

            }.lparams(weight = 1f)

            for (canditate in gamestate.candidates) {
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("%s: %d", canditate.name, Math.round(canditate.generalOpinion))
                }.lparams(weight = 1f)
            }

            if (gamestate.isWon()) {
                textView {
                    gravity = Gravity.CENTER
                    textResource = R.string.win_message
                }
                button {
                    textResource = R.string.try_again
                    onClick {
                        // FIXME check is this working properly
                        ctx.startActivity(ctx.intentFor<MainActivity>())
                    }
                }
            }
            if (gamestate.isLost()) {
                textView {
                    gravity = Gravity.CENTER
                    textResource = R.string.lost_message
                }
                button {
                    textResource = R.string.try_again
                    onClick {
                        // FIXME check is this working properly
                        ctx.startActivity(ctx.intentFor<MainActivity>())
                    }
                }
            } else {
                expelledTV = textView {
                    gravity = Gravity.CENTER
                }
                button {
                    textResource = R.string.next
                    onClick {
                        ctx.startActivity(ctx.intentFor<GameActivity>())
                    }
                }
            }
        }
    }
}