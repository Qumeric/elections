package com.example.qumeric.elections

import android.graphics.RectF
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.TextView
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.listeners.onClick

inline fun ViewManager.sparkView(init: SparkView.() -> Unit): SparkView {
    return ankoView({ SparkView(it) }, theme = 0, init = init)
}

class PollView(val h: Map<String, List<Double>>) : AnkoComponent<PollActivity> {
    private lateinit var ankoContext: AnkoContext<PollActivity>

    lateinit var expelledTV: TextView

    private val colors = listOf(R.color.red, R.color.green, R.color.blue, R.color.yellow, R.color.teal, R.color.orange)

    private val candidateToColor = mutableMapOf<String, Int>()

    override fun createView(ui: AnkoContext<PollActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                gravity = Gravity.CENTER
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                textSize = dip(20).toFloat()
                textAlignment = View.TEXT_ALIGNMENT_CENTER
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
                textView {
                    gravity = Gravity.CENTER
                    text = String.format("%s: %d", c.name, Math.round(c.getGeneralOpinion()))
                }.lparams(weight = 1f)
            }

            for (c in gamestate.candidates) {
                linearLayout {
                    backgroundResource = candidateToColor[c.name]!!
                    alpha = 0.5f
                    textView {
                        gravity = Gravity.CENTER
                        text = String.format("%s: %d", c.name, Math.round(c.generalOpinion))
                    }.lparams(weight = 1f)
                }
            }

            if (gamestate.isWon()) {
                textView {
                    gravity = Gravity.CENTER
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
                expelledTV = textView {
                    gravity = Gravity.CENTER
                }
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

class MyAdapter(val yData: FloatArray) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any? {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }

    override fun getDataBounds(): RectF {
        val bounds = super.getDataBounds()
        bounds.bottom = 100f
        bounds.top = 0f
        return bounds
    }
}
