package rocks.che.elections.debate

import android.annotation.SuppressLint
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.toMaybeRussian
import rocks.che.elections.logic.Candidate

class DebateViewEnd(val candidate: Candidate, private val winGroup: String? = "Winner",
                    private val loseGroup: String? = "Loser", private val attackResult: String? = "Opponent") : DefaultView<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>

    @SuppressLint("RtlHardcoded")
    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            space {
            }.lparams(weight = 0.125f, height = 0)

            verticalLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.white

                imageView {
                    imageResource = candidate.resource
                }.lparams(weight = 0.35f, height = 0)

                gameTextView(20) {
                    textResource = R.string.debate
                }.lparams(weight = 0.12f, height = 0)

                imageView {
                    backgroundResource = R.color.blue
                }.lparams(weight = 0.012f, height = 0, width = dip(120))

                verticalLayout {
                    gravity = Gravity.LEFT
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                        imageView {
                            imageResource = R.drawable.ic_add
                        }.lparams { rightPadding = dip(5) }
                        gameTextView(10) {
                            text = if (winGroup != null) {
                                ctx.getString(R.string.debate_good_group_template).format(winGroup.toMaybeRussian(resources.configuration.locale.toString()))
                            } else {
                                ctx.getString(R.string.debate_good_group_template).format("-")
                            }
                        }
                    }.lparams { leftMargin = dip(30); topMargin = dip(10) }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                        imageView {
                            imageResource = R.drawable.ic_substract
                        }.lparams { rightPadding = dip(5) }
                        gameTextView(10) {
                            text = if (loseGroup != null) {
                                ctx.getString(R.string.debate_bad_group_template).format(loseGroup.toMaybeRussian(resources.configuration.locale.toString()))
                            } else {
                                ctx.getString(R.string.debate_bad_group_template).format("-")
                            }
                        }
                    }.lparams { leftMargin = dip(30); topMargin = dip(5) }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                        imageView {
                            imageResource = R.drawable.ic_flash
                        }.lparams { rightPadding = dip(5) }
                        gameTextView(10) {
                            text = if (attackResult != null) {
                                ctx.getString(R.string.debate_opponent_attack_template).format(attackResult.toMaybeRussian(resources.configuration.locale.toString()))
                            } else {
                                ctx.getString(R.string.debate_opponent_attack_fail)
                            }
                        }
                    }.lparams { leftMargin = dip(30); topMargin = dip(5) }
                }.lparams(width = matchParent)

                themedButton(theme = R.style.button) {
                    textResource = R.string.next
                    onClick { ui.owner.nextStage() }
                }.lparams(weight = 0.14f, height = 0, width = dip(180))
            }.lparams(weight = 0.75f, height = 0)

            space {
            }.lparams(weight = 0.125f, height = 0)
        }
    }
}
