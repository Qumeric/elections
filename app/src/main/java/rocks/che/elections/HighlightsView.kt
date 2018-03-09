package rocks.che.elections

import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.jetbrains.anko.*
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.konfettiView
import rocks.che.elections.logic.Candidate

class HighlightsView(val candidate: Candidate) : DefaultView<HighlightsActivity> {
    private lateinit var ankoContext: AnkoContext<HighlightsActivity>

    private fun scaleView(v: View, startScale: Float = 0f, endScale: Float = 1f) {
        val anim = ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.fillAfter = true; // Needed to keep the result of the animation
        anim.duration = 2500;
        v.startAnimation(anim);
    }

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

                val cLayout = relativeLayout {
                    backgroundResource = R.color.white
                    imageView {
                        imageResource = candidate.resource
                    }.lparams {
                        centerInParent()
                        width = matchParent
                        height = matchParent
                    }
                }.lparams(weight = 0.4f, height = 0)

                scaleView(cLayout)

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
                build().addColors(0xff0000.opaque, 0x00ff00.opaque, 0x0000ff.opaque,
                        0xffff00.opaque, 0x00ffff.opaque, 0xff00ff.opaque, 0x000000.opaque)
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
