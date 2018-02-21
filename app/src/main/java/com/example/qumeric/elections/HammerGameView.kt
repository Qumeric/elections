package com.example.qumeric.elections

import android.view.Gravity
import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import java.util.*

class HammerGameView() : AnkoComponent<HammerGameActivity> {
    private lateinit var ankoContext: AnkoContext<HammerGameActivity>

    public lateinit var layout: GridLayout
    public lateinit var scoreText: TextView

    private val rowCnt = 3
    private val colCnt = 3

    public var field: ArrayList<ArrayList<ImageButton>> = arrayListOf();

    public fun pickRandomEnemyResource(): Int {
        val enemyResources: List<Int> = listOf(R.drawable.grudinin, R.drawable.navalny,
                R.drawable.putin, R.drawable.sobchak, R.drawable.yavlinsky, R.drawable.zhirinovsky)
        return enemyResources[Random().nextInt(enemyResources.size)]
    }

    override fun createView(ui: AnkoContext<HammerGameActivity>) = with(ui) {
        ankoContext = ui

        ui.ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)

        linearLayout {
            gravity = Gravity.NO_GRAVITY

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                backgroundResource = R.color.white
                scoreText = textView { }.lparams(height= matchParent)
            }.lparams(weight = 0.1f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.brightBlue


                layout = gridLayout {
                    columnCount = colCnt
                    rowCount = rowCnt

                    for (row in 1..rowCount) {
                        val rowElems: ArrayList<ImageButton> = arrayListOf();
                        for (column in 1..columnCount) {
                            val e = imageButton {
                                background = null
                                visibility = View.INVISIBLE
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }.lparams {
                                rowSpec = GridLayout.spec(row-1)
                                columnSpec = GridLayout.spec(column-1)
                                width = 160
                                height = 160
                            }
                            rowElems.add(e);
                        }
                        field.add(rowElems);
                    }
                }

            }.lparams(weight = 0.2f, width = matchParent)

            relativeLayout {
                backgroundResource = R.color.sand
            }.lparams(weight = 0.05f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.grass

            }.lparams(weight = 0.1f, width = matchParent)
        }
    }
}
