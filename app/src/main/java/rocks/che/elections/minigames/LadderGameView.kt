package rocks.che.elections.minigames

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.gifImageView

class LadderGameView : AnkoComponent<LadderGameActivity> {
    lateinit var ankoContext: AnkoContext<LadderGameActivity>
    private lateinit var stickmanDrawable: GifDrawable
    lateinit var stickmanView: GifImageView
    lateinit var ladderView: ImageView
    lateinit var scoreText: TextView
    lateinit var enemyView: ImageView

    @SuppressLint("RtlHardcoded")
    override fun createView(ui: AnkoContext<LadderGameActivity>) = with(ui) {
        ankoContext = ui

        frameLayout {
            if (!isInEditMode()) { // FIXME
                stickmanDrawable = GifDrawable(resources, R.drawable.ladder_anim)
                stickmanDrawable.stop()
            }
            relativeLayout {
                gravity = Gravity.CENTER
                onClick {
                    stickmanDrawable.seekToFrame((stickmanDrawable.currentFrameIndex + 1) % stickmanDrawable.numberOfFrames)
                    ladderView.y += 5
                    (ctx as LadderGameActivity).tap()
                }

                scoreText = gameTextView(18) {
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

                if (!isInEditMode) { // FIXME Anko preview does not support gifImageView
                    stickmanView = gifImageView {
                        isClickable = false
                        setImageDrawable(stickmanDrawable)
                    }.lparams {
                        centerInParent()
                        width = dip(80)
                    }
                } else {
                    imageView {
                        backgroundResource = R.color.fuchsia
                    }
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
