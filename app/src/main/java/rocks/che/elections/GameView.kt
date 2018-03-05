package rocks.che.elections

import android.support.v7.app.AlertDialog
import android.view.Gravity
import com.pixplicity.easyprefs.library.Prefs
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.cardView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.QuestionGroup
import rocks.che.elections.logic.fakeQuestions
import rocks.che.elections.logic.getGroupResource

class GameView(val step: Int = 0, val questions: Map<String, QuestionGroup> = fakeQuestions) : DefaultView<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                gameTextView(20) {
                    textResource = R.string.choose_category
                }
            }.lparams {
                height = 0
                weight = 0.2f
            }

            gameTextView {
                text = String.format("Day: %d", step)
            } // FIXME lparams

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

            for ((group, qGroup) in questions) {
                val v = cardView {
                    onClick {
                        val q = qGroup.getQuestion()
                        ctx.startActivity<QuestionActivity>("question" to q, "group" to group)
                    }
                    verticalLayout {
                        imageView {
                            imageResource = getGroupResource(group)
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
                    textResource = R.string.exit_game
                    onClick {
                        val simpleAlert = AlertDialog.Builder(ctx as GameActivity).create()
                        simpleAlert.setTitle(ctx.getString(R.string.end_game_dialog_title))
                        simpleAlert.setMessage(ctx.getString(R.string.end_game_dialog_message))

                        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.yes_button), { _, _ ->
                            Prefs.remove("gamestate")
                            ctx.startActivity<NewGameActivity>()
                        })
                        simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, ctx.getString(R.string.no_button), { _, _ ->
                            // do Nothing
                        })

                        simpleAlert.show()
                    }
                }

                themedButton(theme = R.style.button) {
                    text = "spend money"
                    onClick {
                        ctx.startActivity<SpendMoneyActivity>()
                    }
                }
            }.lparams(weight = 0.2f, height = 0)
        }
    }
}