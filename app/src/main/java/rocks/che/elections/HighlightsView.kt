package rocks.che.elections

import android.view.Gravity
import android.widget.ImageView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.jetbrains.anko.*
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.konfettiView
import rocks.che.elections.helpers.scaleView
import rocks.che.elections.logic.Candidate

class HighlightsView(val candidate: Candidate) : DefaultView<HighlightsActivity> {
    private lateinit var ankoContext: AnkoContext<HighlightsActivity>

    override fun createView(ui: AnkoContext<HighlightsActivity>) = with(ui) {
        ankoContext = ui

        frameLayout {

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

                lateinit var cView: ImageView
                relativeLayout {
                    backgroundResource = R.color.white
                    cView = imageView {
                        imageResource = candidate.resource
                    }.lparams {
                        centerInParent()
                        width = matchParent
                        height = matchParent
                    }
                }.lparams(weight = 0.4f, height = 0)

                scaleView(cView)

                space { }.lparams(weight = 0.05f, height = 0)

                verticalLayout {
                    gravity = Gravity.CENTER
                    for (perk in candidate.perks) {
                        linearLayout {
                            gravity = Gravity.START or Gravity.BOTTOM
                            imageView {
                                imageResource = R.drawable.red_star
                            }
                            gameTextView(12) {
                                text = perk
                            }
                        }.lparams(weight = 0.07f, height = 0) {
                            gravity = Gravity.START or Gravity.BOTTOM
                            bottomMargin = dip(5)
                        }
                    }
                }.lparams(weight = 0.21f, height = 0)
                space { }.lparams(weight = 0.1f, height = 0)
            }.lparams(height = matchParent, width = matchParent)

            konfettiView {
                build().addColors(0xee0000.opaque, 0x00ee00.opaque, 0x0000ee.opaque,
                        0xeeee00.opaque, 0x00eeee.opaque, 0xdd33dd.opaque, 0x000000.opaque)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(Size(12))
                        .setPosition(0f, displayMetrics.widthPixels.toFloat(), 0f, 0f)
                        .streamFor(300, 3000L)
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}
