package rocks.che.elections.minigames

import android.annotation.SuppressLint
import android.view.Gravity
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
        stickmanDrawable = GifDrawable(resources, R.drawable.runner_anim)

        verticalLayout {
            gravity = Gravity.CENTER

            relativeLayout {
                backgroundResource = R.color.black
                background.alpha = 33
                gravity = Gravity.CENTER
                scoreText = gameTextView(dip(18)) {
                    text = "0"
                }
            }.lparams(weight = 0.15f, height = 0, width = matchParent)

            layout = relativeLayout {
                ankoContext = ui
                gravity = Gravity.BOTTOM or Gravity.LEFT
                onClick {
                    stickmanDrawable.seekToFrame((stickmanDrawable.currentFrameIndex + 1) % stickmanDrawable.numberOfFrames)
                    (ctx as RunnerGameActivity).startFlight()
                }
                stickmanView = gifImageView {
                    isClickable = false
                    setImageDrawable(stickmanDrawable)
                }
            }.lparams(weight = 0.85f, height = 0, width = matchParent)
        }
    }
}
