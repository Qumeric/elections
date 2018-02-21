package com.example.qumeric.elections

import android.view.Gravity
import android.widget.*
import org.jetbrains.anko.*
import java.util.*

class SnakeGameView() : AnkoComponent<SnakeGameActivity> {
    private lateinit var ankoContext: AnkoContext<SnakeGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 20
    val colCnt = 20

    var field: ArrayList<ArrayList<ImageButton>> = arrayListOf();

    override fun createView(ui: AnkoContext<SnakeGameActivity>) = with(ui) {
        ankoContext = ui

        linearLayout {
            setOnTouchListener(object: OnSwipeTouchListener(ctx) {
                val activity = ctx as SnakeGameActivity
                override fun onSwipeTop() {
                    if (activity.d != Direction.SOUTH) {
                        activity.d = Direction.NORTH
                    }
                }
                override fun onSwipeRight() {
                     if (activity.d != Direction.WEST) {
                         activity.d = Direction.EAST
                     }
                }
                override fun onSwipeLeft() {
                    if (activity.d != Direction.EAST) {
                        activity.d = Direction.WEST
                    }
                }
                override fun onSwipeBottom() {
                    if (activity.d != Direction.NORTH) {
                        activity.d = Direction.SOUTH
                    }
                }
            })

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                scoreText = textView { }.lparams(height= wrapContent)
            }.lparams(weight = 0.1f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER

                layout = gridLayout {
                    columnCount = colCnt
                    rowCount = rowCnt

                    for (row in 1..rowCount) {
                        val rowElems: ArrayList<ImageButton> = arrayListOf();
                        for (column in 1..columnCount) {
                            val e = imageButton {
                                backgroundResource = R.color.grass
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }.lparams {
                                rowSpec = GridLayout.spec(row-1)
                                columnSpec = GridLayout.spec(column-1)
                                width = 20
                                height = 20
                            }
                            rowElems.add(e);
                        }
                        field.add(rowElems);
                    }
                }

            }.lparams(weight = 0.9f, width = matchParent)
        }
    }
}
