package rocks.che.elections.minigames

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.gifImageView

class RunnerGameView : AnkoComponent<RunnerGameActivity> {
    lateinit var ankoContext: AnkoContext<RunnerGameActivity>
    lateinit var stickmanDrawable: GifDrawable
    lateinit var stickmanView: GifImageView
    lateinit var layout: RelativeLayout
    lateinit var scoreText: TextView

    @SuppressLint("RtlHardcoded")
    override fun createView(ui: AnkoContext<RunnerGameActivity>) = with(ui) {
        ankoContext = ui

        frameLayout {
            backgroundResource = R.color.aqua
            if (!isInEditMode) { // FIXME
                stickmanDrawable = GifDrawable(resources, R.drawable.runner_anim)
                stickmanDrawable.setSpeed(2.5f)
            }

            relativeLayout {
                backgroundResource = R.color.black
                background.alpha = 33
                gravity = Gravity.CENTER
                scoreText = gameTextView(18) {
                    text = "0"
                }
            }.lparams {
                width = matchParent
            }

            frameLayout {
                id = R.id.moving_image_layout
                layout = relativeLayout {
                    gravity = Gravity.BOTTOM
                }.lparams(height= matchParent, width = matchParent)
                relativeLayout {
                    gravity = Gravity.BOTTOM
                    onClick {
                        if (ui.owner.jumps < 2) {
                            stickmanDrawable.seekToFrame((stickmanDrawable.currentFrameIndex + 1) % stickmanDrawable.numberOfFrames)
                            ui.owner.startFlight()
                        }
                    }
                    if (!isInEditMode) { // FIXME
                        stickmanView = gifImageView {
                            translationZ = 0f
                            isClickable = false
                            setImageDrawable(stickmanDrawable)
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams {
                            height = dip(80)
                            width = dip(66)
                        }
                    } else {
                        imageView {
                            backgroundResource = R.color.fuchsia
                        }
                    }
                }.lparams(height = matchParent, width = matchParent)
            }.lparams {
                height = matchParent
                width = matchParent
            }
        }
    }
}
