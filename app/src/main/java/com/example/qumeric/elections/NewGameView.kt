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
                    ctx.startActivity(ctx.intentFor<ChooseCandidateActivity>())
                }
            }
              button {
                text = "c"
                onClick {
                    ctx.startActivity(ctx.intentFor<CatcherGameActivity>())
                }
            }
            button {
                text = "d"
                onClick {
                    ctx.startActivity(ctx.intentFor<DucksGameActivity>())
                }
            }
            button {
                text = "h"
                onClick {
                    ctx.startActivity(ctx.intentFor<HammerGameActivity>())
                }
            }
            button {
                text = "l"
                onClick {
                    ctx.startActivity(ctx.intentFor<LadderGameActivity>())
                }
            }
            button {
                text = "s"
                onClick {
                    ctx.startActivity(ctx.intentFor<SnakeGameActivity>())
                }
            }
        }
    }
}
