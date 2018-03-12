package rocks.che.elections.debate

import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onSeekBarChangeListener
import rocks.che.elections.R
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.groupToResource
import rocks.che.elections.logic.Questions

class DebateViewGroups(val minutes: Int = 60, val questions: Questions = Questions(hashMapOf())) : DefaultView<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>
    private lateinit var nextButton: Button
    private val amounts = mutableListOf<TextView>()
    private val amountVals = mutableListOf<Int>()

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(20) {
                textResource = R.string.debate
            }.lparams(weight = 0.09f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.012f, height = 0, width = dip(120))

            space {}.lparams(weight = 0.015f, height = 0)

            gameTextView(12) {
                textResource = R.string.debate_groups
            }.lparams(weight = 0.1f, height = 0)

            val minutesTextView = gameTextView(18) {
                text = ctx.getString(R.string.debate_minutes_left_template).format(minutes)
            }.lparams(weight = 0.12f, height = 0)

            var isGray = true
            var pos = 0
            for (group in questions.all.keys) {
                relativeLayout {
                    backgroundResource = if (isGray) {
                        R.color.silver
                    } else {
                        R.color.white
                    }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        imageView {
                            imageResource = groupToResource[group]!!
                        }.lparams {
                            width = 0
                            height = matchParent
                            weight = 0.2f
                        }

                        gameTextView(10, text = group) {}

                        seekBar {
                            splitTrack = false
                            thumb = ContextCompat.getDrawable(ctx, R.drawable.seek)
                            progressDrawable = ContextCompat.getDrawable(ctx, R.drawable.seek_style)
                            max = minutes
                            val p = pos // capture value
                            onSeekBarChangeListener {
                                onProgressChanged({ sb, progress, _ ->
                                    var spendMinutes = (0 until amountVals.size)
                                        .filter { it != p }
                                        .sumBy { amountVals[it] }

                                    sb!!.progress = Math.min(max - spendMinutes, progress)
                                    amountVals[p] = sb.progress

                                    spendMinutes += sb.progress

                                    minutesTextView.text = ctx.getString(R.string.debate_minutes_left_template).format(max - spendMinutes)
                                    amounts[p].text = sb.progress.toString()
                                })
                            }
                        }.lparams(width = dip(200))
                    }.lparams {
                        width = matchParent
                        height = matchParent
                        rightMargin = dip(20)
                        alignParentLeft()
                        centerVertically()
                    }

                    amounts.add(gameTextView(10, text = "0") {
                    }.lparams {
                        alignParentRight()
                        centerVertically()
                    })
                    amountVals.add(0)
                }.lparams(weight = 0.1f, height = 0, width = matchParent)
                isGray = !isGray
                pos++
            }

            nextButton = themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick {
                    if (amountVals.sum() == minutes) {
                        ui.owner.setGroupDistribution(amountVals)
                        ui.owner.nextStage()
                    } else {
                        snackbar(this, R.string.debate_spend_minutes_first)
                    }
                }
            }.lparams(weight = 0.08f, height = 0, width = dip(180))
        }
    }
}
