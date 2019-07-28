package rocks.che.elections

import androidx.appcompat.widget.AppCompatTextView
import android.view.Gravity
import android.widget.TextView
import com.pixplicity.easyprefs.library.Prefs
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.*
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange

class GameView(val gs: Gamestate) : DefaultView<GameActivity> {
    lateinit var ankoContext: AnkoContext<GameActivity>
    private lateinit var moneyTextView: AppCompatTextView
    private var groupViews = hashMapOf<String, TextView>()

    override fun createView(ui: AnkoContext<GameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
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
                    backgroundColorResource = if (gs.lastGroup == group) {
                        R.color.silver
                    } else {
                        R.color.white
                    }
                    imageView {
                        imageResource = groupToResource[group]!!
                        onClick {
                            if (gs.lastGroup != group) {
                                gs.lastGroup = group
                                inActivityChange = true
                                ctx.startActivity<QuestionActivity>("question" to gs.questions.get(group),
                                    "group" to group, "gamestate" to gs)
                            } else {
                                this@linearLayout.snackbar(R.string.pick_twice)
                            }
                        }
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.3f
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        onClick {
                            if (gs.lastGroup != group) {
                                gs.lastGroup = group
                                inActivityChange = true
                                ctx.startActivity<QuestionActivity>("question" to gs.questions.get(group),
                                    "group" to group, "gamestate" to gs)
                            } else {
                                this@linearLayout.snackbar(R.string.pick_twice)
                            }
                        }
                        val tv = gameTextView(12) {
                            text = "%s: %d".format(group.toMaybeRussian(resources.configuration.locale.toString()), gs.candidate.opinions[group]!!)
                        }
                        groupViews[group] = tv
                    }.lparams {
                        width = 0
                        height = matchParent
                        weight = 0.5f
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        frameLayout {
                            themedImageButton(theme = R.style.button) {
                                backgroundResource = R.drawable.round_button
                                imageResource = R.drawable.ic_coins
                                onClick {
                                    if (!ui.owner.buyGroupPoints(group)) {
                                        ankoContext.view.snackbar(R.string.not_enough_money)
                                    }
                                    moneyTextView.text = "${gs.money}$"
                                    groupViews[group]!!.text = "%s: %d".format(group.toMaybeRussian(resources.configuration.locale.toString()), gs.candidate.opinions[group]!!)
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
                        text = String.format(ctx.getString(R.string.day_template), gs.step)
                    }
                    space().lparams { width = dip(30) }
                    moneyTextView = gameTextView(14, color = R.color.olive) {
                        text = "%d$".format(gs.money)
                    }
                }.lparams(height = matchParent, width = 0, weight = 0.5f)
                linearLayout {
                    gravity = Gravity.CENTER
                    themedImageButton(theme = R.style.button) {
                        backgroundResource = R.drawable.round_button
                        imageResource = R.drawable.ic_logout
                        onClick { _ ->
                            alert(R.string.end_game_dialog_message, R.string.end_game_dialog_title) {
                                positiveButton(R.string.yes_button) {
                                    Prefs.remove("gamestate")
                                    inActivityChange = true
                                    ctx.startActivity<NewGameActivity>()
                                }
                                negativeButton(R.string.no_button) {
                                    // Do nothing
                                }
                            }.show()
                        }
                    }.lparams(height = dip(50), width = dip(50))
                }.lparams(height = matchParent, width = 0, weight = 0.5f)
            }.lparams(weight = 0.2f, height = 0, width = matchParent)
        }
    }
}