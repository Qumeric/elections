package rocks.che.elections

import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.MyAdapter
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.sparkView
import rocks.che.elections.logic.gamestate


class PollView(val h: Map<String, List<Double>>) : AnkoComponent<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>

    lateinit var expelledTV: TextView

    private val colors = listOf(R.color.red, R.color.green, R.color.blue, R.color.yellow, R.color.teal, R.color.orange)

    private val candidateToColor = mutableMapOf<String, Int>()

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(dip(20)) {
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
                backgroundResource = candidateToColor[c.name]!!
                alpha = 0.5f
                gameTextView {
                    text = String.format("%s: %d", c.name, Math.round(c.getGeneralOpinion()))
                }.lparams(weight = 1f)
            }

            for (c in gamestate.candidates) {
                linearLayout {
                    backgroundResource = candidateToColor[c.name]!!
                    alpha = 0.5f
                    gameTextView {
                        text = String.format("%s: %d", c.name, Math.round(c.generalOpinion))
                    }.lparams(weight = 1f)
                }
            }

            if (gamestate.isWon()) {
                gameTextView {
                    textResource = R.string.win_message
                }
                button {
                    textResource = R.string.try_again
                    onClick {
                        // FIXME check is this working properly
                        ctx.startActivity(ctx.intentFor<ChooseCandidateActivity>())
                    }
                }
            }
            if (gamestate.isLost()) {
                textView {
                    gravity = Gravity.CENTER
                    textResource = R.string.lost_message
                }
                button {
                    textResource = R.string.try_again
                    onClick {
                        // FIXME check is this working properly
                        ctx.startActivity(ctx.intentFor<ChooseCandidateActivity>())
                    }
                }
            } else {
                expelledTV = gameTextView { }
                button {
                    textResource = R.string.next
                    onClick {
                        ctx.startActivity(ctx.intentFor<GameActivity>())
                    }
                }
            }
        }
    }
}

