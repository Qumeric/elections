package rocks.che.elections

import android.content.Intent
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.groupToResource
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.minigames.*
import java.util.*


class ChangeView(private val oldOpinions: Map<String, Int> = mapOf(), val gs: Gamestate) : DefaultView<ChangeActivity> {
    private lateinit var ankoContext: AnkoContext<ChangeActivity>

    override fun createView(ui: AnkoContext<ChangeActivity>) = with(ui) {
        ankoContext = ui

        var pos = 0

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(20) {
                textResource = R.string.support
            }

            space().lparams(height = dip(30), width = matchParent)

            for ((group, opinion) in gs.candidate.opinions) {
                linearLayout {
                    leftPadding = dip(25)
                    gravity = Gravity.CENTER_VERTICAL
                    if (pos % 2 == 0) {
                        backgroundResource = R.color.silver
                    }
                    imageView {
                        imageResource = groupToResource[group]!!
                    }.lparams {
                        width = dip(50)
                        height = dip(50)
                    }

                    space().lparams(width = dip(15), height = matchParent)

                    // FIXME strings of different length?
                    gameTextView(10) {
                        text = if (!isInEditMode) {
                            "%s: %d(%s)".format(group, opinion,
                                    Formatter().format(Locale.US, "%+d", opinion- oldOpinions[group]!!))
                        } else {
                            "Group: val(+c)"
                        }
                    }.lparams {
                        horizontalMargin = dip(15)
                    }
                    var currentValue = opinion
                    linearLayout {
                        while (currentValue >= 10) {
                            imageView {
                                imageResource = if (currentValue >= 20) R.drawable.blue_star else R.drawable.blue_star_half
                            }.lparams {
                                height = dip(18)
                                width = dip(18)
                                marginEnd = dip(5)
                            }
                            currentValue = (currentValue - 1) / 20 * 20 // largest divisor of 20 which is less than currentValue
                        }
                    }
                }.lparams(height = dip(70), width = matchParent)
                pos++
            }
            space().lparams(height = dip(40), width = matchParent)
            button {
                textResource = R.string.next
                backgroundResource = R.color.blue
                onClick {
                    if (gs.isPollTime) {
                        gs.nextDebates = !gs.nextDebates
                        debug("it is poll time and nextdebates is %b".format(gs.nextDebates))
                        if (gs.nextDebates) {
                            val intent = Intent(ui.owner, RunnerGameActivity::class.java)
                            when (gs.candidate.resource) {
                                R.drawable.candidate_navalny -> Intent(ui.owner, RunnerGameActivity::class.java)
                                R.drawable.candidate_sobchak -> Intent(ui.owner, LadderGameActivity::class.java)
                                R.drawable.candidate_zhirinovsky -> Intent(ui.owner, DucksGameActivity::class.java)
                                R.drawable.candidate_putin -> Intent(ui.owner, HammerGameActivity::class.java)
                                R.drawable.candidate_grudinin -> Intent(ui.owner, CatcherGameActivity::class.java)
                                R.drawable.candidate_yavlinsky -> Intent(ui.owner, SnakeGameActivity::class.java)
                            }
                            ui.owner.startActivityForResult(intent, gameRequestCode)
                        } else {
                            ctx.startActivity<DebateActivity>("gamestate" to gs)
                        }
                    } else {
                        ctx.startActivity<PollActivity>("gamestate" to gs)
                    }
                }
            }.lparams(width = dip(150), height = dip(60))
        }
    }
}