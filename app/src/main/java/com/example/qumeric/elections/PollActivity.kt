package com.example.qumeric.elections

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import com.androidplot.xy.*
import org.jetbrains.anko.setContentView
import java.lang.reflect.Array
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*

class PollActivity : AppCompatActivity() {
    private lateinit var view: PollView

    val colors = listOf(Color.RED, Color.YELLOW, Color.BLUE, Color.WHITE, Color.CYAN, Color.MAGENTA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = PollView()
        view.setContentView(this)

        val plot: XYPlot = view.plotView.findViewById(R.id.rating_plot)

        val domainLabels  = (0..gamestate.step).map{ i -> i}.toIntArray()

        // These two arrays should have equal size (bad design, I know...)
        val ratings = mutableListOf<XYSeries>()
        val formatters = mutableListOf<LineAndPointFormatter>()

        var colorPtr = 0

        ratings.add(SimpleXYSeries(gamestate.candidate.history,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, gamestate.candidate.name))
        Log.d("PollActivity", "user history len:" + gamestate.candidate.history.size.toString())
        formatters.add(LineAndPointFormatter(colors[colorPtr++], Color.GREEN, Color.TRANSPARENT, null))
        plot.addSeries(ratings[0], formatters[0])
        for (c in gamestate.candidates) {
            ratings.add(SimpleXYSeries(c.history,
                    SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, c.name))
            Log.d("PollActivity", "opponent history len:" + c.history.size.toString())
            formatters.add(LineAndPointFormatter(colors[colorPtr++], Color.GREEN, Color.TRANSPARENT, null))
            plot.addSeries(ratings.last(), formatters.last())
        }

        Log.d("PollActivity", "dl:" + domainLabels.size.toString())
        Log.d("PollActivity", "ra: " + ratings.size.toString())
        Log.d("PollActivity", "fo: " + formatters.size.toString())

        if (!gamestate.isLost()) {
            view.expelledTV.text = String.format("Candidate %s has been expelled from elections", gamestate.expel())
        }

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(object: Format() {
            override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                val i: Int = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabels[i])
            }
            override fun parseObject(source: String, pos: ParsePosition): Any? {
                return null
            }
        })

    }
}