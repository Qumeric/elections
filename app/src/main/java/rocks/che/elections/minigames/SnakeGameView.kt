package rocks.che.elections.minigames

import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import org.jetbrains.anko.*
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView

class SnakeGameView(private val onTouchListener: View.OnTouchListener) : AnkoComponent<SnakeGameActivity> {
    lateinit var ankoContext: AnkoContext<SnakeGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 33
    val colCnt = 20

    override fun createView(ui: AnkoContext<SnakeGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            setOnTouchListener(onTouchListener)

            relativeLayout {
                frameLayout {
                    backgroundResource = R.color.black
                    alpha = 0.33f
                }.lparams {
                    alignParentTop()
                    centerHorizontally()
                    width = matchParent
                    height = matchParent
                }
                scoreText = gameTextView(18) { }.lparams {
                    alignParentTop()
                    centerHorizontally()
                }
            }.lparams(width = matchParent, height = dip(70))

            layout = gridLayout {
                isClickable = false
                columnCount = colCnt
                rowCount = rowCnt
                backgroundResource = R.color.green

                for (row in 0 until rowCount) {
                    for (col in 0 until columnCount) {
                        imageView {
                        }.lparams {
                            rowSpec = GridLayout.spec(row, 1f)
                            columnSpec = GridLayout.spec(col, 1f)
                            height = dip(10)
                            width = dip(10)
                        }
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}
