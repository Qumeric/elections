package rocks.che.elections

import android.content.Intent
import android.os.Parcelable
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.groupToResource
import rocks.che.elections.helpers.toMaybeRussian
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange
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

                    var delta = opinion - oldOpinions[group]!!

                    gameTextView(10) {
                        text = "%s: %d(%s)".format(group.toMaybeRussian(resources.configuration.locale.toString()), maxOf(0, opinion), Formatter().format(Locale.US, "%+d", delta))
                    }.lparams {
                        horizontalMargin = dip(15)
                    }
                    linearLayout {
                        while (delta >= 2) {
                            imageView {
                                imageResource = if (delta >= 4) R.drawable.blue_star else R.drawable.blue_star_half
                            }.lparams {
                                height = dip(18)
                                width = dip(18)
                                marginEnd = dip(5)
                            }
                            delta = (delta - 1) / 4 * 4 // largest divisor of 4 which is less than currentValue
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
                            val intent = when (gs.candidate.resource) {
                                R.drawable.candidate_navalny -> Intent(ui.owner, RunnerGameActivity::class.java)
                                R.drawable.candidate_sobchak -> Intent(ui.owner, RacesGameActivity::class.java)
                                R.drawable.candidate_zhirinovsky -> Intent(ui.owner, DucksGameActivity::class.java)
                                R.drawable.candidate_putin -> Intent(ui.owner, HammerGameActivity::class.java)
                                R.drawable.candidate_grudinin -> Intent(ui.owner, CatcherGameActivity::class.java)
                                R.drawable.candidate_yavlinsky -> Intent(ui.owner, SnakeGameActivity::class.java)
                                else -> throw Exception("Cannot determine what game should be started")
                            }
                            if (gs.candidate.resource == R.drawable.candidate_sobchak) {
                                intent.putParcelableArrayListExtra("candidates", gs.candidates as ArrayList<out Parcelable>)
                            }
                            ui.owner.startActivityForResult(intent, gameRequestCode)
                        } else {
                            inActivityChange = true
                            ctx.startActivity<DebateActivity>("gamestate" to gs)
                        }
                    } else {
                        inActivityChange = true
                        ctx.startActivity<PollActivity>("gamestate" to gs)
                    }
                }
            }.lparams(width = dip(150), height = dip(60))
        }
    }
}