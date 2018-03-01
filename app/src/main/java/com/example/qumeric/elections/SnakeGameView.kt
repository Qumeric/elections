package com.example.qumeric.elections

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import java.util.*

class SquareGridLayout(ctx: Context): _GridLayout(ctx) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (width > height) {
            width = height
        } else {
            height = width
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }
}


inline fun ViewManager.squareGridLayout(init: SquareGridLayout.() -> Unit): SquareGridLayout {
    return ankoView({ SquareGridLayout(it) }, theme = 0, init = init)
}

class SnakeGameView : AnkoComponent<SnakeGameActivity> {
    lateinit var ankoContext: AnkoContext<SnakeGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 20
    val colCnt = 20

    var field: ArrayList<ArrayList<ImageButton>> = arrayListOf()


    override fun createView(ui: AnkoContext<SnakeGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            setOnTouchListener(object : OnSwipeTouchListener(ctx) {
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

            relativeLayout {
                scoreText = textView { }.lparams(height = wrapContent)
            }.lparams(weight = 0.1f, width = matchParent)


            layout = squareGridLayout {

                isClickable = false

                columnCount = colCnt
                rowCount = rowCnt


                for (row in 0 until rowCount) {
                    val rowElems: ArrayList<ImageButton> = arrayListOf()
                    (0 until columnCount).mapTo(rowElems) {
                        imageButton {
                            isClickable = false
                            backgroundResource = R.color.green
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams {
                            rowSpec = GridLayout.spec(row, 1f)
                            columnSpec = GridLayout.spec(it, 1f)
                            width = 20
                            height = 20
                        }
                    }
                    field.add(rowElems)
                }
            }.lparams(weight = 0.9f, width = matchParent)
        }
    }
}
