package com.example.qumeric.elections

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class GameView : AnkoComponent<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            weightSum = 1f
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                textView {
                    textResource = R.string.choose_category
                    typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                    textSize = dip(20).toFloat()
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
            }.lparams {
                height = 0
                weight = 0.2f
            }

            /*textView {
                gravity = Gravity.CENTER
                text = String.format("Day: %d", gamestate.step)
            }*/

            val row1 = linearLayout {
                space().lparams(width = dip(20), height = matchParent)
            }.lparams {
                height = 0
                weight = 0.3f
            }

            val row2 = linearLayout {
                space().lparams(width = dip(20), height = matchParent)
            }.lparams {
                height = 0
                weight = 0.3f
            }

            verticalLayout {

                button {
                    textResource = R.string.exit_game;
                    onClick {
                        val simpleAlert = AlertDialog.Builder(ctx as GameActivity).create()
                        simpleAlert.setTitle(ctx.getString(R.string.end_game_dialog_title))
                        simpleAlert.setMessage(ctx.getString(R.string.end_game_dialog_message))

                        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.yes_button), {
                            _, _ -> ctx.startActivity(ctx.intentFor<NewGameActivity>())
                       })
                        simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, ctx.getString(R.string.no_button), {
                            _, _ -> // do Nothing
                        });

                        simpleAlert.show()
                    }
                }

                button {
                    text = "spend money"
                    onClick {
                        ctx.startActivity(ctx.intentFor<SpendMoneyActivity>())
                    }
                }
            }

            var leftInRow = 3

            for ((group, qGroup) in gamestate.questions) {
                val v = cardView {
                    onClick {
                        val q = qGroup.getQuestion()
                        val ctx = ankoContext.ctx
                        ctx.startActivity(ctx.intentFor<QuestionActivity>("question" to q, "group" to group))
                    }
                    verticalLayout {
                        imageView {
                            imageResource = resources.getIdentifier(group, "drawable", "com.example.qumeric.elections")
                        }.lparams {
                            height = dip(70)
                            width = dip(70)
                        }
                        textView {
                            backgroundResource = R.color.navy
                            text = group
                            textColor = R.color.white
                            typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                        }
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
                removeView(v)
                if (leftInRow > 0) {
                    row1.addView(v)
                    val s = space().lparams(width = dip(20), height = matchParent)
                    removeView(s)
                    row1.addView(s)
                } else {
                    row2.addView(v)
                    val s = space().lparams(width = dip(20), height = matchParent)
                    removeView(s)
                    row2.addView(s)
                }
                leftInRow--
            }
        }
    }
}