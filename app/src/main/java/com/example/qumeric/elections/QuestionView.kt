package com.example.qumeric.elections

import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class QuestionView(val question: Question) : AnkoComponent<QuestionActivity> {
    private lateinit var ankoContext: AnkoContext<QuestionActivity>

    override fun createView(ui: AnkoContext<QuestionActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                gravity = Gravity.CENTER
                text = question.text
            }

            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                for (answer in question.answers) {
                     val b = button {
                        onClick {
                            ctx.startActivity(ctx.intentFor<ChangeActivity>("answer" to answer))
                        }
                    }.lparams(width = wrapContent, height = wrapContent)
                    b.setText(answer.statement)
                }
            }
        }
    }
}
