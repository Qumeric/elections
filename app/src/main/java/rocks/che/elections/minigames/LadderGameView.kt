package rocks.che.elections.minigames

import android.view.Gravity
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R

class LadderGameView() : AnkoComponent<LadderGameActivity> {
    lateinit var ankoContext: AnkoContext<LadderGameActivity>

    lateinit var layout: GridLayout
    lateinit var scoreText: TextView

    override fun createView(ui: AnkoContext<LadderGameActivity>) = with(ui) {
        ankoContext = ui

        linearLayout {
            gravity = Gravity.CENTER

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                backgroundResource = R.color.white
                scoreText = textView { }.lparams(height = wrapContent)
            }.lparams(weight = 0.1f, width = matchParent)

            relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.blue

                button("click") {
                    onClick {
                        (ctx as LadderGameActivity).tap()
                    }
                }
            }.lparams(weight = 0.7f, width = matchParent)

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
