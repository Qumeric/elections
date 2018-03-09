package rocks.che.elections.minigames

import android.os.Bundle
import android.view.ViewGroup
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import rocks.che.elections.logic.Candidate
import java.lang.Math.random
import kotlin.math.sqrt

class RacesGameActivity : MiniGameActivity() {
    private lateinit var view: RacesGameView

    private val length = 20L // in seconds
    private var speed = 0f
    private var acceleration = 5f
    private var deceleration = 1f
    private val speeds = mutableListOf<Float>(0f, 0f, 0f, 0f, 0f, 0f)

    fun tap() {
        speed += acceleration
        score++
        view.scoreText.text = score.toString()
    }

    private fun update() {
        speed = maxOf(0f, speed-deceleration)
        view.horse.x += speed

        if (view.horse.x >= displayMetrics.widthPixels-view.horse.width) {
            win()
        }

        for ((index, h) in view.opponentHorses.withIndex()) {
            speeds[index] += sqrt(0.2/random()).toFloat()
            speeds[index] = maxOf(0f, speeds[index]-deceleration)
            h.x += speeds[index]
            if (h.x >= displayMetrics.widthPixels-h.width) {
                lose()
            }
        }

        handler.postDelayed({ update() }, 1000 / 50)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val candidates: ArrayList<Candidate> = intent.getParcelableArrayListExtra("candidates")
        view = RacesGameView(candidates)
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

    fun win() = lose()

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
