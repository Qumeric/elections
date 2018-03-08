package rocks.che.elections.debate

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
import rocks.che.elections.logic.bus

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

            space{}.lparams(weight = 0.015f, height = 0)

            gameTextView(12) {
                textResource = R.string.debate_groups
            }.lparams(weight = 0.1f, height = 0)

            val minutesTextView = gameTextView(18) {
                text = ctx.getString(R.string.debate_minutes_left_template).format(minutes)
            }.lparams(weight = 0.12f, height = 0)

            var isGray = true
            var pos = 0
            for (group in questions.all.keys) {
                linearLayout {
                    gravity = Gravity.CENTER
                    backgroundResource = if (isGray) {
                        R.color.silver
                    } else {
                        R.color.white
                    }

                    imageView {
                        imageResource = groupToResource[group]!!
                    }.lparams {
                        height = dip(30)
                        width = dip(30)
                    }

                    gameTextView(10, text = group) {}

                    seekBar {
                        max = minutes
                        val p = pos // capture value
                        onSeekBarChangeListener {
                            onProgressChanged({ sb, progress, _ ->
                                var spendMinutes = (0 until amountVals.size)
                                        .filter { it != p }
                                        .sumBy { amountVals[it] }

                                sb!!.progress =  Math.min(max-spendMinutes, progress)
                                amountVals[p] = sb.progress

                                spendMinutes += sb.progress

                                minutesTextView.text = ctx.getString(R.string.debate_minutes_left_template).format(max-spendMinutes)
                                amounts[p].text = sb.progress.toString()
                            })
                        }
                    }.lparams(width=dip(200))

                    amounts.add(gameTextView(10, text="0") {})
                    amountVals.add(0)
                }.lparams(weight = 0.1f, height = 0, width = matchParent)
                isGray = !isGray
                pos++
            }

            nextButton = themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick {
                    if (amountVals.sum() == minutes) {
                        bus.post(SetGroupDistribution(amountVals))
                        bus.post(NextDebateStage())
                    } else {
                        snackbar(this, R.string.debate_spend_minutes_first)
                    }
                }
            }.lparams(weight = 0.08f, height = 0, width = dip(180))
        }
    }
}
