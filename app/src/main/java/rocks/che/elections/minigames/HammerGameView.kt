package rocks.che.elections.minigames

import android.view.Gravity
import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import rocks.che.elections.R
import java.util.*

class HammerGameView() : AnkoComponent<HammerGameActivity> {
    lateinit var ankoContext: AnkoContext<HammerGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    val rowCnt = 3
    val colCnt = 3

    var field: ArrayList<ArrayList<ImageButton>> = arrayListOf()

    fun pickRandomEnemyResource(): Int {
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
                scoreText = textView { }.lparams(height = wrapContent)
            }.lparams(weight = 0.1f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.blue


                layout = gridLayout {
                    columnCount = colCnt
                    rowCount = rowCnt

                    for (row in 1..rowCount) {
                        val rowElems: ArrayList<ImageButton> = arrayListOf()
                        for (column in 1..columnCount) {
                            val e = imageButton {
                                background = null
                                visibility = View.INVISIBLE
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }.lparams {
                                rowSpec = GridLayout.spec(row - 1)
                                columnSpec = GridLayout.spec(column - 1)
                                width = 160
                                height = 160
                            }
                            rowElems.add(e)
                        }
                        field.add(rowElems)
                    }
                }

            }.lparams(weight = 0.2f, width = matchParent)

            relativeLayout {
                backgroundResource = R.color.yellow
            }.lparams(weight = 0.05f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.green

            }.lparams(weight = 0.1f, width = matchParent)
        }
    }
}
