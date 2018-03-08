package rocks.che.elections

import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.widget.ImageView
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Subscribe
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.groupToResource
import rocks.che.elections.logic.ChangeMoneyEvent
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.bus

class GameView(val gs: Gamestate) : DefaultView<GameActivity> {
    private lateinit var ankoContext: AnkoContext<GameActivity>
    private lateinit var moneyTextView: AppCompatTextView

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        bus.register(this@GameView)
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

            for (group in gs.questions.all.keys) {
                linearLayout {
                    gravity = Gravity.START
                    backgroundColorResource = R.color.white
                    imageView {
                        imageResource = groupToResource[group]!!
                        onClick {
                            gs.lastGroup = group
                            ctx.startActivity<QuestionActivity>("question" to gs.questions.get(group), "group" to group)
                        }
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.3f
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        gameTextView(12) {
                            text = "%s: %d".format(group, gs.candidate.opinions[group]!!)
                            onClick {
                                gs.lastGroup = group
                                ctx.startActivity<QuestionActivity>("question" to gs.questions.get(group),
                                        "group" to group, "gamestate" to gs)
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
                                    if (!ui.owner.buyGroupPoints(group)) {
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
                        text = String.format("Day: %d", gs.step)
                    }
                    space().lparams { width = dip(30) }
                    moneyTextView = gameTextView(14, color = R.color.olive) {
                        text = gs.money.toString() + "$"
                    }
                }.lparams(height = matchParent, width = 0, weight = 0.5f)
                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.ic_logout
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        onClick {
                            alert( R.string.end_game_dialog_message, R.string.end_game_dialog_title) {
                                positiveButton(R.string.yes_button) {
                                    Prefs.remove("gamestate")
                                    ctx.startActivity<NewGameActivity>()
                                }
                                negativeButton(R.string.no_button) {
                                    // Do nothing
                                }
                            }.show()
                        }
                    }.lparams(height = dip(20), width = dip(20))
                }.lparams(height = matchParent, width = 0, weight = 0.5f)
            }.lparams(weight = 0.2f, height = 0, width = matchParent)
        }
    }

    @Subscribe fun updateMoney(e: ChangeMoneyEvent) {
        moneyTextView.text = e.money.toString()
    }
}