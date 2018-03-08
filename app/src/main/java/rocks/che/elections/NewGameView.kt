package rocks.che.elections

import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.loadCandidates
import rocks.che.elections.minigames.*

class NewGameView : DefaultView<NewGameActivity> {
    private lateinit var ankoContext: AnkoContext<NewGameActivity>

    override fun createView(ui: AnkoContext<NewGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            linearLayout {
                gravity = Gravity.CENTER
                weightSum = 1f
                verticalLayout {
                    linearLayout {
                        gravity = Gravity.CENTER
                        for (i in 1..5) {
                            imageView {
                                imageResource = R.drawable.red_star
                            }.lparams {
                                width = 0
                                weight = 1f
                            }
                        }
                    }.lparams {
                        width = matchParent
                        height = dip(30)
                    }

                    frameLayout {
                        gameTextView(20, R.color.aqua) {
                            text = ctx.getText(R.string.logo)
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }
                        imageView {
                            imageResource = R.drawable.checkmark
                        }.lparams {
                            gravity = Gravity.END
                            width = dip(25)
                            height = dip(25)
                        }
                    }

                    linearLayout {
                        gravity = Gravity.CENTER
                        for (i in 1..5) {
                            imageView {
                                imageResource = R.drawable.red_star
                            }.lparams {
                                width = 0
                                weight = 1f
                            }
                        }
                    }.lparams {
                        width = matchParent
                        height = dip(30)
                    }
                }.lparams {
                    width = 0
                    weight = 0.5f
                }
            }

            imageView {
                imageResource = R.drawable.play
                onClick {
                    ctx.startActivity<ChooseCandidateActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "catcher"
                onClick {
                    ctx.startActivity<CatcherGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "ducks"
                onClick {
                    ctx.startActivity<DucksGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "hammer"
                onClick {
                    ctx.startActivity<HammerGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "ladder"
                onClick {
                    ctx.startActivity<LadderGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "snake"
                onClick {
                    ctx.startActivity<SnakeGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "runner"
                onClick {
                    ctx.startActivity<RunnerGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "debate"
                val cs = loadCandidates(resources)
                onClick {
                    ctx.startActivity<DebateActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "endGameView"
                onClick {
                    ctx.startActivity<EndGameActivity>()
                }
            }
        }
    }
}
