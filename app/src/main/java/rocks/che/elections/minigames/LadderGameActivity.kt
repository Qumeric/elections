package rocks.che.elections.minigames

import android.os.Bundle
import android.view.ViewGroup
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import java.lang.Math.random

class LadderGameActivity : MiniGameActivity() {
    private lateinit var view: LadderGameView

    private val length = 20L // in seconds
    private var speed = 0f
    private var acceleration = 5f
    private var deceleration = 5f

    fun tap() {
        speed += acceleration
        score++
        view.scoreText.text = score.toString()
    }

    private fun update() {
        speed -= deceleration * (view.horse.x / displayMetrics.widthPixels)
        speed = maxOf(speed, -view.horse.x / 5)
        view.horse.x += speed

        handler.postDelayed({ update() }, 1000 / 50)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = LadderGameView()
        view.setContentView(this)

        useMovingImages(find<ViewGroup>(R.id.moving_image_layout))
        //MusicManager.getInstance().play(this, R.raw.) FIXME

        drawInformationDialog(getString(R.string.ladder_info_title), getString(R.string.ladder_info_message),
                {
                    handler.postDelayed({ lose() }, 1000 * length)
                    update()
                    doEach(
                            { createMovingImage(R.drawable.races_clown, 10f, 10f) },
                            { (1 + random()) * 1000 }
                    )
                }, view.ankoContext)

    }

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
                getString(R.string.ladder_end_title),
                getString(R.string.ladder_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}
