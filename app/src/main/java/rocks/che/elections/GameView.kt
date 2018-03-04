package rocks.che.elections

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.cardView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.getGroupResource

class GameView : AnkoComponent<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                gameTextView(dip(20)) {
                    textResource = R.string.choose_category
                }
            }.lparams {
                height = 0
                weight = 0.2f
            }

            /*gameTextView {
                gravity = Gravity.CENTER
                statement = String.format("Day: %d", gamestate.step)
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
                            imageResource = getGroupResource(ctx, group)
                        }.lparams {
                            height = dip(70)
                            width = dip(70)
                        }
                        gameTextView(color = R.color.white) {
                            backgroundResource = R.color.navy
                            text = group
                        }
                    }
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

            linearLayout {
                gravity = Gravity.CENTER
                themedButton(theme = R.style.button) {
                    textResource = R.string.exit_game;
                    onClick {
                        val simpleAlert = AlertDialog.Builder(ctx as GameActivity).create()
                        simpleAlert.setTitle(ctx.getString(R.string.end_game_dialog_title))
                        simpleAlert.setMessage(ctx.getString(R.string.end_game_dialog_message))

                        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.yes_button), { _, _ ->

                            ctx.startActivity(ctx.intentFor<NewGameActivity>())
                        })
                        simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, ctx.getString(R.string.no_button), { _, _ ->
                            // do Nothing
                        });

                        simpleAlert.show()
                    }
                }

                themedButton(theme = R.style.button) {
                    text = "spend money"
                    onClick {
                        ctx.startActivity(ctx.intentFor<SpendMoneyActivity>())
                    }
                }
            }.lparams(weight = 0.2f, height = 0)
        }
    }
}