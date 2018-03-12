package rocks.che.elections.minigames

import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.squareGridLayout
import java.util.*

class HammerGameView : AnkoComponent<HammerGameActivity> {
    lateinit var ankoContext: AnkoContext<HammerGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView
    lateinit var missedText: TextView

    val rowCnt = 3
    val colCnt = 3

    var field: ArrayList<ArrayList<ImageView>> = arrayListOf()

    fun pickRandomEnemyResource(): Int {
        return listOf(R.drawable.candidate_grudinin, R.drawable.candidate_navalny, R.drawable.candidate_sobchak,
                R.drawable.candidate_yavlinsky, R.drawable.candidate_zhirinovsky)[Random().nextInt(5)]
    }

    override fun createView(ui: AnkoContext<HammerGameActivity>) = with(ui) {
        ankoContext = ui

        ui.ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)

        verticalLayout {
            gravity = Gravity.NO_GRAVITY

            relativeLayout {
                backgroundResource = R.color.white
                scoreText = gameTextView(18, color = R.color.black, autoResize = !isInEditMode) {
                    text = "0"
                }.lparams {
                    alignParentLeft()
                    centerVertically()
                }
                missedText = gameTextView(18, color = R.color.maroon, autoResize = !isInEditMode) {
                    text = "0"
                }.lparams {
                    alignParentRight()
                    centerVertically()
                }
            }.lparams(weight = 0.1f, width = matchParent, height = 0)

            layout = squareGridLayout {
                backgroundResource = R.color.blue
                columnCount = colCnt
                rowCount = rowCnt

                for (row in 0 until rowCount) {
                    val rowElems: List<ImageView> = (0 until columnCount).map {
                        imageView {
                            background = null
                            visibility = View.INVISIBLE
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams {
                            rowSpec = GridLayout.spec(row, 1f)
                            columnSpec = GridLayout.spec(it, 1f)
                            height = 20
                            width = 20
                        }
                    }
                    field.add(rowElems as ArrayList<ImageView>)
                }
            }.lparams(weight=9/16f, width= matchParent, height = 0)

            relativeLayout {
                backgroundResource = R.color.yellow
            }.lparams(weight = 0.15f, width = matchParent, height = 0)

            relativeLayout {
                backgroundResource = R.color.green
            }.lparams(weight = 0.05f, width = matchParent, height = 0)
        }
    }
}
