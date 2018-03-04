package rocks.che.elections.minigames

import android.util.Log
import android.view.Gravity
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.gifImageView

class LadderGameView() : AnkoComponent<LadderGameActivity> {
    lateinit var ankoContext: AnkoContext<LadderGameActivity>
    lateinit var stickmanDrawable: GifDrawable
    lateinit var stickmanView: GifImageView
    lateinit var ladderView: ImageView
    lateinit var scoreText: TextView
    lateinit var enemyView: ImageView

    override fun createView(ui: AnkoContext<LadderGameActivity>) = with(ui) {
        ankoContext = ui
        stickmanDrawable = GifDrawable(resources, R.drawable.ladder_anim)
        stickmanDrawable.stop()

        frameLayout {
            relativeLayout {
                gravity = Gravity.CENTER
                onClick {
                    stickmanDrawable.seekToFrame((stickmanDrawable.currentFrameIndex + 1) % stickmanDrawable.numberOfFrames)
                    ladderView.y += 5
                    (ctx as LadderGameActivity).tap()
                }

                scoreText = gameTextView(dip(18)) {
                    text = "0"
                }.lparams {
                    alignParentTop()
                    centerHorizontally()
                }

                ladderView = imageView {
                    isClickable = false
                    imageResource = R.drawable.ladder
                }.lparams {
                    centerInParent()
                    width = dip(100)
                }

                stickmanView = gifImageView {
                    isClickable = false
                    setImageDrawable(stickmanDrawable)
                }.lparams {
                    centerInParent()
                    width = dip(80)
                }
            }
            relativeLayout {
                gravity = Gravity.LEFT
                enemyView = imageView {
                    isClickable = false
                    imageResource = R.drawable.putin
                    rotation = 90f
                }.lparams {
                    width = dip(100)
                    x = -width.toFloat()
                }
            }
        }
    }
}
