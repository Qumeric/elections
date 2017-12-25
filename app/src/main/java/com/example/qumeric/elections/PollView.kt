package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class PollView(): AnkoComponent<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                gravity = Gravity.CENTER
                text = String.format("Poll results on day %d", gamestate.step)
            }

            for (canditate in gamestate.candidates) {
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("%s: %d", canditate.name, canditate.generalOpinion)
                }
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
                val lostCandidateName = gamestate.expel()
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("Candidate %s has been expelled from elections", lostCandidateName)
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