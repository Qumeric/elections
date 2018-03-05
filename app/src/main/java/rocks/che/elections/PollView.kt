package rocks.che.elections

import android.support.v4.content.ContextCompat.getColor
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.MyAdapter
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.sparkView
import rocks.che.elections.logic.gamestate

class PollView(private val h: Map<String, List<Double>>) : DefaultView<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>
    private val colors = listOf(R.color.red, R.color.green, R.color.blue, R.color.yellow, R.color.teal, R.color.orange)
    private val candidateToColor = mutableMapOf<String, Int>()

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(20) {
                text = String.format("Poll results on day %d", gamestate.step)
            }.lparams(weight = 1f)

            frameLayout {
                backgroundResource = R.color.silver
                var i = 0
                for ((c, hd) in h) {
                    candidateToColor[c] = colors[i]
                    val sv = sparkView {}
                    sv.adapter = MyAdapter(FloatArray(hd.size, { j -> hd[j].toFloat() }))
                    sv.lineColor = getColor(ctx, colors[i++])
                }
            }.lparams(weight = 1f, height = dip(300))

            linearLayout {
                val c = gamestate.candidate
                gameTextView(14, candidateToColor[c.name]!!) {
                    text = String.format("%s: %d", c.name, Math.round(c.generalOpinion()))
                }.lparams(weight = 1f)

                for (oc in gamestate.candidates) {
                    gameTextView(14, candidateToColor[oc.name]!!) {
                        text = String.format("%s: %d", oc.name, Math.round(oc.generalOpinion()))
                    }.lparams(weight = 1f)
                }
            }
            if (gamestate.isPollTime() && (gamestate.isWon() || gamestate.isLost())) {
                gameTextView(12) {
                    textResource = if (gamestate.isWon()) {
                        R.string.win_message
                    } else {
                        R.string.lost_message
                    }
                }
                themedButton(theme = R.style.button) {
                    textResource = R.string.try_again
                    onClick {
                        ctx.startActivity<ChooseCandidateActivity>()
                    }
                }
            } else {
                if (gamestate.isPollTime()) {
                    gameTextView(12) {
                        text = String.format("Candidate %s has been expelled from elections", gamestate.expel())
                    }
                }
                themedButton(theme = R.style.button) {
                    textResource = R.string.next
                    onClick {
                        ctx.startActivity<GameActivity>()
                    }
                }
            }
        }
    }
}

