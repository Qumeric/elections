package rocks.che.elections.minigames

import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import rocks.che.elections.helpers.OnSwipeTouchListener
import rocks.che.elections.R
import rocks.che.elections.helpers.squareGridLayout
import java.util.*



class SnakeGameView(val onTouchListener: View.OnTouchListener) : AnkoComponent<SnakeGameActivity> {
    lateinit var ankoContext: AnkoContext<SnakeGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 20
    val colCnt = 20

    var field: ArrayList<ArrayList<ImageButton>> = arrayListOf()


    override fun createView(ui: AnkoContext<SnakeGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            setOnTouchListener(onTouchListener)

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
