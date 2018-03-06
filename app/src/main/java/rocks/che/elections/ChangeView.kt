package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.Opinion
import rocks.che.elections.logic.fakeOpinions
import rocks.che.elections.logic.getGroupResource
import java.util.*

class ChangeView(private val oldOpinions: Map<String, Int> = mapOf(),
                 val opinions: Map<String, Opinion> = fakeOpinions, val isPollTime: Boolean = false) : DefaultView<ChangeActivity> {
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

            for ((group, opinion) in opinions) {
                linearLayout {
                    leftPadding = dip(25)
                    gravity = Gravity.CENTER_VERTICAL
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

                    // FIXME strings of different length?
                    gameTextView(10) {
                        text = if (!isInEditMode) {
                            "%s: %d(%s)".format(group, opinion.value,
                                    Formatter().format(Locale.US, "%+d", opinion.value - oldOpinions[group]!!))
                        } else {
                            "Group: val(+c)"
                        }
                    }.lparams {
                        horizontalMargin = dip(15)
                    }
                    var currentValue = opinion.value
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
                    if (isPollTime) {
                        ctx.startActivity<DebateActivity>()
                    } else {
                        ctx.startActivity<PollActivity>()
                    }
                }
            }.lparams(width = dip(150), height = dip(60))
        }
    }
}