package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate

class HighlightsView : AnkoComponent<HighlightsActivity> {
    private lateinit var ankoContext: AnkoContext<HighlightsActivity>

    override fun createView(ui: AnkoContext<HighlightsActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            space { }.lparams(weight = 0.1f, height = 0)

            gameTextView(dip(20)) {
                textResource = R.string.your_candidate
            }.lparams(weight = 0.1f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.01f, height = 0, width = dip(120))

            gameTextView(dip(16)) {
                text = gamestate.candidate.name
            }.lparams(weight = 0.09f, height = 0)

            relativeLayout {
                backgroundResource = R.color.white
                imageView {
                    imageResource = gamestate.candidate.resource
                }.lparams {
                    centerInParent()
                    width = matchParent
                    height = matchParent
                }
            }.lparams(weight = 0.4f, height = 0)

            for (perk in gamestate.candidate.perks) {
                linearLayout {
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.drawable.red_star
                    }
                    gameTextView(dip(12)) {
                        text = perk
                    }
                }.lparams(weight = 0.07f, height = 0)
            }
            space { }.lparams(weight = 0.25f, height = 0)
        }
    }
}
