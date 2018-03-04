package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.getGroupResource
import java.util.*

class ChangeView(private val oldOpinions: Map<String, Int>) : AnkoComponent<ChangeActivity> {
    private lateinit var ankoContext: AnkoContext<ChangeActivity>

    override fun createView(ui: AnkoContext<ChangeActivity>) = with(ui) {
        ankoContext = ui

        var pos = 0

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(dip(15)) {
                textResource = R.string.support
            }

            space().lparams(height = dip(40), width = matchParent)

            for ((group, opinion) in gamestate.opinions) {
                linearLayout {
                    gravity = Gravity.CENTER
                    if (pos % 2 == 0) {
                        backgroundResource = R.color.silver
                    }
                    imageView {
                        imageResource = getGroupResource(group)
                    }.lparams {
                        width = dip(50)
                        height = dip(50)
                    }

                    space().lparams(width = dip(15), height = matchParent)

                    gameTextView(dip(10)) {
                        text = String.format("%s  %d(%s)", group, opinion.value,
                                Formatter().format(Locale.US, "%+d", opinion.value - oldOpinions[group]!!))
                    }
                }.lparams(height = dip(70), width = matchParent)
                pos++
            }
            space().lparams(height = dip(40), width = matchParent)
            button {
                textResource = R.string.next
                backgroundResource = R.color.blue
                onClick {
                    if (gamestate.isPollTime()) {
                        ctx.startActivity(ctx.intentFor<DebateActivity>())
                    } else {
                        ctx.startActivity(ctx.intentFor<PollActivity>())
                    }
                }
            }.lparams(width = dip(150), height = dip(60))
        }
    }
}