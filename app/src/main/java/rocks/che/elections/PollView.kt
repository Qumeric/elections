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
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange
import kotlin.math.round

class PollView(private val h: Map<Int, List<Double>> = mapOf(), val gs: Gamestate) : DefaultView<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>
    private val candidateToColor = mutableMapOf(
        R.drawable.candidate_putin to R.color.red,
        R.drawable.candidate_sobchak to R.color.aqua,
        R.drawable.candidate_zhirinovsky to R.color.yellow,
        R.drawable.candidate_grudinin to R.color.fuchsia,
        R.drawable.candidate_yavlinsky to R.color.green,
        R.drawable.candidate_navalny to R.color.black
    )

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER
            backgroundResource = R.color.white

            frameLayout {
                gameTextView(16, autoResize = true) {
                    text = resources.getString(R.string.poll_results_template).format(gs.step) // unbreakable space
                }
            }.lparams(height = 0, width = matchParent, weight = 0.09f)

            verticalLayout {
                backgroundResource = R.color.blue
                frameLayout {
                    backgroundResource = R.color.silver
                    for ((c, hd) in h) {
                        val sv = sparkView {}
                        sv.adapter = MyAdapter(FloatArray(hd.size, { j -> hd[j].toFloat() }))
                        sv.lineColor = getColor(ctx, candidateToColor[c]!!)
                    }
                }.lparams {
                    padding = dip(3)
                }
            }.lparams(height = 0, width = matchParent, weight = 0.3f) { horizontalMargin = dip(5) }

            space {

            }.lparams(height = 0, width = matchParent, weight = 0.1f)

            frameLayout {
                gridLayout {
                    rowCount = 3
                    columnCount = 2
                    alignmentMode = GridLayout.ALIGN_MARGINS

                    val ssum = gs.candidates.sumBy { it.generalOpinion.toInt() }

                    var col = 0
                    var row = 0
                    for (oc in gs.candidates) {
                        relativeLayout {
                            gravity = Gravity.CENTER
                            backgroundResource = if ((row + col) % 2 == 1) R.color.silver else R.color.white
                            linearLayout {
                                gravity = Gravity.CENTER_VERTICAL or Gravity.START
                                view {
                                    id = R.id.poll_indicator
                                    val shape = GradientDrawable()
                                    shape.shape = GradientDrawable.OVAL
                                    shape.cornerRadii = floatArrayOf(8f, 8f, 8f, 8f, 0f, 0f, 0f, 0f)
                                    shape.setColor(ContextCompat.getColor(ctx, candidateToColor[oc.resource]!!))
                                    background = shape
                                }.lparams {
                                    width = dip(15)
                                    height = dip(15)
                                    rightMargin = dip(10)
                                }
                                gameTextView(9) {
                                    if (gs.candidate.resource == oc.resource) {
                                        textResource = R.string.you
                                    } else {
                                        text = oc.name
                                    }
                                }.lparams {
                                    leftPadding = dip(3)
                                }
                            }
                            gameTextView(9) {
                                text = "%d%%".format(round(oc.generalOpinion / ssum * 100).toInt())
                            }.lparams {
                                alignParentRight()
                                centerVertically()
                                rightMargin = dip(4)
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
            }.lparams(height = 0, width = matchParent, weight = 0.15f)

            verticalLayout {
                gravity = Gravity.CENTER
                if (gs.isPollTime && (gs.isWon || gs.isWorst)) {
                    themedButton(theme = R.style.button) {
                        if (gs.isWorst) {
                            toast(R.string.lost_message)
                            startActivity<EndGameActivity>("isWon" to false, "gamestate" to gs)
                        }
                        if (gs.isWon) {
                            toast(R.string.win_message)
                            startActivity<EndGameActivity>("isWon" to true, "gamestate" to gs)
                        }
                        textResource = R.string.try_again
                        onClick {
                            inActivityChange = true
                            ctx.startActivity<ChooseCandidateActivity>()
                        }
                    }
                } else {
                    themedButton(theme = R.style.button) {
                        textResource = R.string.next
                        onClick {
                            if (gs.isPollTime) gs.candidates.remove(gs.worst)
                            inActivityChange = true
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

