package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick


class GameView : AnkoComponent<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            for ((group, opinion) in gamestate.opinions) {
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("%s: %d", group, opinion.value)
                }
            }
            for ((group, qGroup) in gamestate.questions) {
                val b = button {
                    onClick {
                        val q = qGroup.getQuestion()
                        val ctx = ankoContext.ctx
                        ctx.startActivity(ctx.intentFor<QuestionActivity>("question" to q))
                    }
                }.lparams(width= matchParent, height = wrapContent)
                b.setText(group)
            }
        }
    }
}