package com.example.qumeric.elections

import android.view.Gravity
import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import java.util.*

class LadderGameView() : AnkoComponent<LadderGameActivity> {
    private lateinit var ankoContext: AnkoContext<LadderGameActivity>

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

    override fun createView(ui: AnkoContext<LadderGameActivity>) = with(ui) {
        ankoContext = ui

        ui.ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)

        linearLayout {
            gravity = Gravity.CENTER

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                backgroundResource = R.color.white
                scoreText = textView { }.lparams(height= wrapContent)
            }.lparams(weight = 0.1f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.brightBlue

                button("click") {
                    onClick {
                        (ctx as LadderGameActivity).tap()
                    }
                }
            }.lparams(weight = 0.7f, width = matchParent)

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
