package rocks.che.elections.minigames

import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import java.util.*

class LadderGameView : AnkoComponent<LadderGameActivity> {
    lateinit var ankoContext: AnkoContext<LadderGameActivity>
    lateinit var scoreText: TextView
    lateinit var horse: ImageView
    val horseResource = listOf(R.drawable.races_trojan_1, R.drawable.races_trojan_2, R.drawable.races_trojan_3,
            R.drawable.races_trojan_4, R.drawable.races_trojan_5, R.drawable.races_trojan_6)[Random().nextInt(6)]

    override fun createView(ui: AnkoContext<LadderGameActivity>) = with(ui) {
        ankoContext = ui

        relativeLayout {
            //id = "@+id/linear_layout"
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

            val animatedBackground = background as AnimationDrawable
            animatedBackground.setEnterFadeDuration(2500);
            animatedBackground.setExitFadeDuration(5000);
            animatedBackground.start();

            frameLayout {
                relativeLayout {
                    id = R.id.moving_image_layout
                    gravity = Gravity.BOTTOM
                    onClick {
                        ui.owner.tap()
                    }
                }.lparams(height= matchParent, width = matchParent)
                relativeLayout {
                    gravity = Gravity.BOTTOM
                    horse = imageView {
                        translationZ = 0f
                        isClickable = false
                        imageResource = horseResource
                        scaleType = ImageView.ScaleType.FIT_END
                    }.lparams {
                        height = dip(70)
                        width = dip(70)
                    }
                }.lparams(height= matchParent, width = matchParent)
            }.lparams(width = matchParent, height = matchParent) {
                alignParentBottom()
            }
        }
    }
}
