package rocks.che.elections.debate

import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate

class DebateViewStart : AnkoComponent<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            space {
            }.lparams(weight = 0.125f, height = 0)

            verticalLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.white

                imageView {
                    imageResource = gamestate.candidate.getResource(ctx)
                }.lparams(weight = 0.35f, height = 0)

                gameTextView(dip(20)) {
                    textResource = R.string.debate
                }.lparams(weight = 0.15f, height = 0)

                imageView {
                    backgroundResource = R.color.blue
                }.lparams(weight = 0.012f, height = 0, width = dip(120))

                gameTextView (dip(14)) {
                    padding = dip(15)
                    textResource = R.string.debate_description
                }.lparams(weight = 0.4f, height = 0)

                themedButton(theme = R.style.button) {
                    textResource = R.string.next
                    onClick { (ctx as DebateActivity).nextStage() }
                }.lparams(weight = 0.14f, height = 0, width = dip(180))
            }.lparams(weight = 0.75f, height = 0)


            space {
            }.lparams(weight = 0.125f, height = 0)
        }
    }
}
