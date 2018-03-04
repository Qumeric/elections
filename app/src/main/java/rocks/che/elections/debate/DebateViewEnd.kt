package rocks.che.elections.debate

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate

class DebateViewEnd : AnkoComponent<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            space {
            }.lparams(weight = 0.125f, height = 0)

            verticalLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.white

                imageView {
                    imageResource = gamestate.candidate.resource
                }.lparams(weight = 0.35f, height = 0)

                gameTextView(dip(20)) {
                    textResource = R.string.debate
                }.lparams(weight = 0.15f, height = 0)

                imageView {
                    backgroundResource = R.color.blue
                }.lparams(weight = 0.012f, height = 0, width = dip(120))

                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.ic_add
                    }
                    gameTextView(dip(10)) {
                        text= ctx.getString(R.string.debate_good_group_template).format((ctx as DebateActivity).winGroup())
                    }
                }
                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.ic_substract
                    }
                    gameTextView(dip(10)) {
                        text= ctx.getString(R.string.debate_bad_group_template).format((ctx as DebateActivity).loseGroup())
                    }
                }.lparams(weight = 0.1f, height = 0, width = matchParent)
                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.ic_flash
                    }
                    gameTextView(dip(10)) {
                        textResource = if ((ctx as DebateActivity).attackResult()) {
                            R.string.debate_opponent_attack_success
                        } else {
                            R.string.debate_opponent_attack_fail
                        }
                    }
                }.lparams(weight = 0.1f, height = 0, width = matchParent)

                themedButton(theme = R.style.button) {
                    textResource = R.string.next
                    onClick {(ctx as DebateActivity).nextStage() }
                }.lparams(weight = 0.14f, height = 0, width = dip(180))
            }.lparams(weight = 0.75f, height = 0)

            space {
            }.lparams(weight = 0.125f, height = 0)
        }
    }
}
