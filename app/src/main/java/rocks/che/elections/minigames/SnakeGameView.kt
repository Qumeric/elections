package rocks.che.elections.minigames

import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.squareGridLayout
import java.util.*

class SnakeGameView(private val onTouchListener: View.OnTouchListener) : AnkoComponent<SnakeGameActivity> {
    lateinit var ankoContext: AnkoContext<SnakeGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 20
    val colCnt = 20

    var field: ArrayList<ArrayList<ImageView>> = arrayListOf()

    override fun createView(ui: AnkoContext<SnakeGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            setOnTouchListener(onTouchListener)

            relativeLayout {
                scoreText = gameTextView(18) { }
                //?lengthText = gameTextView {}
            }.lparams(weight = 7/32f, width = matchParent, height = 0)

            layout = squareGridLayout {
                isClickable = false
                columnCount = colCnt
                rowCount = rowCnt

                for (row in 0 until rowCount) {
                    val rowElems: ArrayList<ImageView> = arrayListOf()
                    (0 until columnCount).mapTo(rowElems) {
                        imageView {
                            isClickable = false
                            backgroundResource = R.color.green
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams {
                            rowSpec = GridLayout.spec(row, 1f)
                            columnSpec = GridLayout.spec(it, 1f)
                            height = 40
                            width = 40
                        }
                    }
                    field.add(rowElems)
                }
            }.lparams(weight = 9/16f, width = matchParent, height = 0)
        }
    }
}
