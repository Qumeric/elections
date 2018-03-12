package rocks.che.elections.minigames

import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.Candidate
import java.util.*

class RacesGameView(val candidates: ArrayList<Candidate>) : AnkoComponent<RacesGameActivity> {
    lateinit var ankoContext: AnkoContext<RacesGameActivity>
    lateinit var scoreText: TextView
    lateinit var horse: ImageView
    lateinit var layout: RelativeLayout
    lateinit var preText: TextView

    val opponentHorses = mutableListOf<ImageView>()

    val candidateToHorse = mapOf(
            R.drawable.candidate_putin to R.drawable.races_horse_putin,
            R.drawable.candidate_navalny to R.drawable.races_horse_navalny,
            R.drawable.candidate_grudinin to R.drawable.races_horse_grudinin,
            R.drawable.candidate_yavlinsky to R.drawable.races_horse_yavlinksy,
            R.drawable.candidate_zhirinovsky to R.drawable.races_horse_zhirinovsky,
            R.drawable.candidate_sobchak to R.drawable.races_horse_sobchak)

    var started = false

    override fun createView(ui: AnkoContext<RacesGameActivity>) = with(ui) {
        ankoContext = ui

        layout = relativeLayout {
            backgroundResource = R.drawable.bg_animlist
            relativeLayout {
                backgroundResource = R.color.black
                background.alpha = 33
                gravity = Gravity.CENTER
                scoreText = gameTextView(18) {
                    text = "0"
                }
            }.lparams(width = matchParent, height = dip(60)) {
                alignParentTop()
                centerHorizontally()
            }

            preText = gameTextView(25) {
            }.lparams {
                centerInParent()
            }

            val animatedBackground = background as AnimationDrawable
            animatedBackground.setEnterFadeDuration(2500);
            animatedBackground.setExitFadeDuration(5000);
            animatedBackground.start();

            frameLayout {
                relativeLayout {
                    id = R.id.moving_image_layout
                    gravity = Gravity.BOTTOM
                    onClick {
                        if (started) ui.owner.tap()
                    }
                }.lparams(height= matchParent, width = matchParent)
                relativeLayout {
                    gravity = Gravity.BOTTOM
                    horse = imageView {
                        translationZ = 0f
                        isClickable = false
                        imageResource = R.drawable.races_horse_sobchak
                        scaleType = ImageView.ScaleType.FIT_END
                    }.lparams {
                        height = dip(70)
                        width = dip(70)
                    }
                }.lparams(height= matchParent, width = matchParent)
                for ((i, c) in candidates.filter { it.resource != R.drawable.candidate_sobchak }.withIndex()) {
                    relativeLayout {
                        gravity = Gravity.BOTTOM
                        val h = imageView {
                            translationZ = 0f
                            isClickable = false
                            imageResource = candidateToHorse[c.resource]!!
                            scaleType = ImageView.ScaleType.FIT_END
                        }.lparams {
                            height = dip(70)
                            width = dip(70)
                        }
                        opponentHorses.add(h)
                    }.lparams(height= matchParent, width = matchParent) {
                        bottomMargin = dip(70)*(i+1)
                    }
                }
            }.lparams(width = matchParent, height = matchParent) {
                alignParentBottom()
            }
            imageView {
                imageResource = R.drawable.ic_flag
            }.lparams {
                alignParentBottom()
                alignParentRight()
                rightMargin = dip(20)
            }
        }
        layout
    }
}
