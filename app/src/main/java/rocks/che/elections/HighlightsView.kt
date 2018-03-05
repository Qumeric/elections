package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.Candidate
import rocks.che.elections.logic.fakeCandidate

class HighlightsView(val candidate: Candidate = fakeCandidate) : DefaultView<HighlightsActivity> {
    private lateinit var ankoContext: AnkoContext<HighlightsActivity>

    override fun createView(ui: AnkoContext<HighlightsActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            space { }.lparams(weight = 0.1f, height = 0)

            gameTextView(20) {
                textResource = R.string.your_candidate
            }.lparams(weight = 0.1f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.01f, height = 0, width = dip(120))

            gameTextView(16) {
                text = candidate.name
            }.lparams(weight = 0.09f, height = 0)

            relativeLayout {
                backgroundResource = R.color.white
                imageView {
                    imageResource = candidate.resource
                }.lparams {
                    centerInParent()
                    width = matchParent
                    height = matchParent
                }
            }.lparams(weight = 0.4f, height = 0)

            space { }.lparams(weight = 0.05f, height = 0)

            verticalLayout {
                gravity = Gravity.CENTER
                for (perk in candidate.perks) {
                    linearLayout {
                        imageView {
                            imageResource = R.drawable.red_star
                        }
                        gameTextView(12) {
                            text = perk
                        }
                    }.lparams(weight = 0.07f, height = 0) {
                        gravity = Gravity.START
                    }
                }
            }.lparams(weight = 0.21f, height = 0)
            space { }.lparams(weight = 0.1f, height = 0)
        }
    }
}
