package rocks.che.elections

import android.support.v4.content.ContextCompat.getColor
import android.view.Gravity
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.MyAdapter
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.sparkView
import rocks.che.elections.logic.gamestate

class PollView(private val h: Map<String, List<Double>> = mapOf()) : DefaultView<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>
    private val colors = listOf(R.color.red, R.color.green, R.color.blue, R.color.yellow, R.color.teal, R.color.orange)
    private val candidateToColor = mutableMapOf<String, Int>()

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            frameLayout {
                gameTextView(20) {
                    text = String.format("Poll results on day %d", gamestate.step)
                }
            }.lparams(height = 0, width = matchParent, weight = 0.1f)

            frameLayout {
                backgroundResource = R.color.silver
                var i = 0
                for ((c, hd) in h) {
                    candidateToColor[c] = colors[i]
                    val sv = sparkView {}
                    sv.adapter = MyAdapter(FloatArray(hd.size, { j -> hd[j].toFloat() }))
                    sv.lineColor = getColor(ctx, colors[i++])
                }
            }.lparams(height = 0, width = matchParent, weight = 0.4f)

            frameLayout {
                gridLayout {
                    rowCount = 3
                    columnCount = 2
                    alignmentMode = GridLayout.ALIGN_MARGINS

                    val c = gamestate.candidate
                    linearLayout {
                        gravity = Gravity.CENTER
                        backgroundResource = R.color.white
                        gameTextView(14, candidateToColor[c.name]!!) {
                            text = String.format("%s: %d", c.name, Math.round(c.generalOpinion()))
                        }
                    }.lparams {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(0, 1f)
                        rowSpec = GridLayout.spec(0, 1f)
                    }

                    var col = 1
                    var row = 0
                    for (oc in gamestate.candidates) {
                        linearLayout {
                            gravity = Gravity.CENTER
                            backgroundResource = if ((row + col) % 2 == 1) R.color.silver else R.color.white
                            gameTextView(14, candidateToColor[oc.name]!!) {
                                text = String.format("%s: %d", oc.name, Math.round(oc.generalOpinion()))
                            }
                        }.lparams {
                            width = 0
                            height = 0
                            columnSpec = GridLayout.spec(col, 1f)
                            rowSpec = GridLayout.spec(row, 1f)
                        }
                        col++
                        if (col == 2) {
                            col = 0
                            row++
                        }
                    }
                }.lparams {
                    height = matchParent
                    width = matchParent
                }
            }.lparams(height = 0, width = matchParent, weight = 0.4f)

            frameLayout {
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
            }.lparams(height = 0, width = matchParent, weight = 0.1f)
        }
    }
}

