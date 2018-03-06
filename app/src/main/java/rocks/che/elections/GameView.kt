package rocks.che.elections

import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.widget.ImageView
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Bus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.BuyGroupPointsEvent
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.*

class GameView(val step: Int = 0, val questions: Map<String, QuestionGroup> = fakeQuestions,
               val opinions: Opinions = fakeOpinions, var money: Int = 0,
               val bus: Bus = Bus()) : DefaultView<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>
    private lateinit var moneyTextView: AppCompatTextView
    private val moneyToUp = 5

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            verticalLayout {
                backgroundColorResource = R.color.black
                background.alpha = 33
                gravity = Gravity.CENTER
                gameTextView {
                    textResource = R.string.choose_category
                }.lparams(height = dip(50), width = matchParent)
            }.lparams {
                height = dip(50)
                width = matchParent
            }

            for ((group, qGroup) in questions) {
                linearLayout {
                    gravity = Gravity.START
                    backgroundColorResource = R.color.white
                    imageView {
                        imageResource = getGroupResource(group)
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.3f
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        gameTextView(12) {
                            text = if (!isInEditMode) {
                                "%s: %d".format(group, opinions[group]!!.value.toString())
                            } else {
                                "Group: val"
                            }
                            onClick {
                                val q = qGroup.getQuestion()
                                ctx.startActivity<QuestionActivity>("question" to q, "group" to group)
                            }
                        }
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.5f
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        frameLayout {
                            // TODO draw a circle or use round button theme
                            themedImageButton(theme = R.style.button) {
                                backgroundResource = R.drawable.round_button
                                imageResource = R.drawable.ic_coins
                                onClick {
                                    if (money >= moneyToUp) {
                                        bus.post(BuyGroupPointsEvent(group, 1, moneyToUp))
                                        money -= moneyToUp
                                        moneyTextView.text = money.toString()
                                    } else {
                                        snackbar(ankoContext.view, R.string.not_enough_money)
                                    }
                                }
                            }
                        }.lparams(width = matchParent, height = matchParent)
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.2f
                        padding = dip(10)
                    }
                }.lparams {
                    height = 0
                    weight = 1f / 5
                    width = matchParent
                    //padding = dip(5)
                    //margin = dip(5)
                }
                view {
                    backgroundColor = R.color.gray
                }.lparams(width = matchParent, height = dip(1))
            }

            linearLayout {
                gravity = Gravity.CENTER
                linearLayout {
                    gravity = Gravity.CENTER
                    gameTextView(14) {
                        text = String.format("Day: %d", step)
                    }
                    space().lparams { width = dip(30) }
                    moneyTextView = gameTextView(14, color = R.color.navy) {
                        text = money.toString() + "$"
                    }
                }.lparams(height = matchParent, width = 0, weight = 0.5f)
                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.ic_logout
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        onClick {
                            alert(R.string.end_game_dialog_title, R.string.end_game_dialog_message) {
                                yesButton {
                                    Prefs.remove("gamestate")
                                    ctx.startActivity<NewGameActivity>()
                                }
                                noButton {
                                    // Do nothing
                                }
                            }.show()
                        }
                    }.lparams(height = dip(20), width = dip(20))
                }.lparams(height = matchParent, width = 0, weight = 0.5f)

                /*imageView {
                    imageResource = if (isSpending) R.drawable.ic_close else R.drawable.ic_coins
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(height= matchParent, width = 0, weight = 0.5f)*/
            }.lparams(weight = 0.2f, height = 0, width = matchParent)
        }
    }
}