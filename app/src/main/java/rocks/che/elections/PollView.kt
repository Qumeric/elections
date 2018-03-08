package rocks.che.elections

import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.view.Gravity
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.MyAdapter
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.sparkView
import rocks.che.elections.logic.Candidate
import rocks.che.elections.logic.Gamestate
import kotlin.math.round

class PollView(private val h: Map<String, List<Double>> = mapOf(), val gs: Gamestate) : DefaultView<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>
    private val colors = listOf(R.color.red, R.color.green, R.color.aqua,
            R.color.yellow, R.color.fuchsia, R.color.black)
    private val candidateToColor = mutableMapOf<String, Int>()

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            frameLayout {
                gameTextView(20) {
                    text = String.format("Poll results on day %d", gs.step)
                }
            }.lparams(height = 0, width = matchParent, weight = 0.15f)

            verticalLayout {
                frameLayout {
                    backgroundResource = R.color.silver
                    var i = 0
                    for ((c, hd) in h) {
                        candidateToColor[c] = colors[i]
                        val sv = sparkView {}
                        sv.adapter = MyAdapter(FloatArray(hd.size, { j -> hd[j].toFloat() }))
                        sv.lineColor = getColor(ctx, colors[i++])
                    }
                }.lparams {
                    padding=dip(10)
                }
            }.lparams(height = 0, width = matchParent, weight = 0.3f)

            space {

            }.lparams(height = 0, width = matchParent, weight = 0.1f)

            var loser: Candidate? = gs.worst

            frameLayout {
                gridLayout {
                    rowCount = 3
                    columnCount = 2
                    alignmentMode = GridLayout.ALIGN_MARGINS

                    val c = gs.candidate
                    // FIXME
                    val ssum = gs.candidates.sumBy<Candidate> { it.generalOpinion.toInt() } + c.generalOpinion.toInt()

                    var col = 0
                    var row = 0
                    for (oc in gs.candidates) {
                        relativeLayout {
                            gravity = Gravity.CENTER
                            backgroundResource = if ((row + col) % 2 == 1) R.color.silver else R.color.white
                            if (loser == oc) {
                                backgroundResource = R.color.pink
                            }
                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL or Gravity.START
                                view {
                                    id = R.id.poll_indicator
                                    val shape = GradientDrawable()
                                    shape.shape = GradientDrawable.OVAL
                                    shape.cornerRadii = floatArrayOf(8f, 8f, 8f, 8f, 0f, 0f, 0f, 0f)
                                    shape.setColor(ContextCompat.getColor(ctx, candidateToColor[oc.name]!!))
                                    background = shape
                                }.lparams {
                                    width = dip(15)
                                    height = dip(15)
                                }
                                gameTextView(10) {
                                    text = oc.name
                                }.lparams {
                                    leftPadding = dip(3)
                                }
                            }
                            gameTextView(10) {
                                text = "%d%%".format(round(oc.generalOpinion/ssum*100).toInt())
                            }.lparams {
                                alignParentRight()
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
            }.lparams(height = 0, width = matchParent, weight = 0.25f)

            verticalLayout {
                gravity = Gravity.CENTER
                if (gs.isPollTime && (gs.isWon || gs.isWorst)) {
                    themedButton(theme = R.style.button) {
                        if (gs.isWorst) toast(R.string.lost_message) // FIXME snackbar is better but crashes
                        if (gs.isWon) toast(R.string.win_message)
                        textResource = R.string.try_again
                        onClick {
                            ctx.startActivity<ChooseCandidateActivity>()
                        }
                    }
                } else {
                    themedButton(theme = R.style.button) {
                        textResource = R.string.next
                        onClick {
                            if (gs.isPollTime) gs.candidates.remove(gs.worst)
                            ctx.startActivity<GameActivity>("gamestate" to gs)
                        }
                    }
                }
            }.lparams(height = 0, width = matchParent, weight = 0.2f) {
                padding = dip(20)
            }
        }
    }
}

