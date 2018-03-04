package rocks.che.elections.debate

import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onSeekBarChangeListener
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate

class DebateViewOpponents : AnkoComponent<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>
    private val amounts = mutableListOf<TextView>()
    private val amountVals = mutableListOf<Int>()

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(dip(20)) {
                textResource = R.string.debate
            }.lparams(weight = 0.09f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.012f, height = 0, width = dip(120))

            gameTextView(dip(12)) {
                textResource = R.string.debate_opponents
            }.lparams(weight = 0.15f, height = 0)

            val minutesTextView = gameTextView(dip(18)) {
                text = ctx.getString(R.string.debate_minutes_left_template).format((ctx as DebateActivity).opponentMinutes)
            }.lparams(weight = 0.1f, height = 0)

            var isGray = true
            var pos = 0
            for (candidate in gamestate.candidates.filter { it.name != gamestate.candidate.name }) {
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
                        max = (ctx as DebateActivity).opponentMinutes
                        val p = pos // capture value
                        onSeekBarChangeListener {
                            onProgressChanged({ sb, progress, fromUser ->
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

                    amounts.add(textView("0"))
                    amountVals.add(0)
                }.lparams(weight = 0.1f, height = 0, width = matchParent)
                isGray = !isGray
                pos++
            }

            themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick {
                    val activity = ctx as DebateActivity
                    if (amountVals.sum() == activity.opponentMinutes) {
                        activity.setOpponentDistribution(amountVals)
                        activity.nextStage()
                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.debate_spend_minutes_first), Toast.LENGTH_SHORT).show()
                    }
                }
            }.lparams(weight = 0.08f, height = 0, width = dip(180))
        }
    }
}
