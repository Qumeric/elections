package rocks.che.elections.debate

import android.view.Gravity
import android.widget.TextView
import com.squareup.otto.Bus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onSeekBarChangeListener
import rocks.che.elections.R
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.SetOpponentDistribution
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.nextDebateStage
import rocks.che.elections.logic.gamestate

class DebateViewOpponents(val minutes: Int = 40, val bus: Bus = Bus()) : DefaultView<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>
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

            gameTextView(12) {
                textResource = R.string.debate_opponents
            }.lparams(weight = 0.15f, height = 0)

            val minutesTextView = gameTextView(18) {
                text = ctx.getString(R.string.debate_minutes_left_template).format(minutes)
            }.lparams(weight = 0.1f, height = 0)

            var isGray = true
            for ((pos, candidate) in gamestate.candidates.withIndex()) {
                linearLayout {
                    gravity = Gravity.CENTER
                    backgroundResource = if (isGray) {
                        R.color.gray
                    } else {
                        R.color.silver
                    }

                    gameTextView(text=candidate.name) {
                    }

                    seekBar {
                        max = minutes
                        onSeekBarChangeListener {
                            onProgressChanged({ sb, progress, _ ->
                                var spendMinutes = (0 until amountVals.size)
                                        .filter { it != pos }
                                        .sumBy { amountVals[it] }

                                sb!!.progress =  Math.min(max-spendMinutes, progress)
                                amountVals[pos] = sb.progress

                                spendMinutes += sb.progress

                                minutesTextView.text = ctx.getString(R.string.debate_minutes_left_template).format(max-spendMinutes)
                                amounts[pos].text = sb.progress.toString()
                            })
                        }
                    }.lparams(width=dip(200))

                    amounts.add(textView("0"))
                    amountVals.add(0)
                }.lparams(weight = 0.1f, height = 0, width = matchParent)
                isGray = !isGray
            }

            themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick {
                    if (amountVals.sum() == minutes) {
                        bus.post(SetOpponentDistribution(amountVals))
                        bus.post(nextDebateStage)
                    } else {
                        snackbar(this, R.string.debate_spend_minutes_first)
                    }
                }
            }.lparams(weight = 0.08f, height = 0, width = dip(180))
        }
    }
}
