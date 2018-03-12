package rocks.che.elections.minigames

import android.os.Bundle
import android.view.ViewGroup
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.textColor
import rocks.che.elections.R
import rocks.che.elections.helpers.scaleView
import rocks.che.elections.logic.Candidate
import java.lang.Math.random
import kotlin.math.sqrt

class RacesGameActivity : MiniGameActivity() {
    private lateinit var view: RacesGameView

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
        speed = maxOf(0f, speed - deceleration)
        view.horse.x += speed

        if (view.horse.x >= displayMetrics.widthPixels - view.horse.width) {
            win()
            return
        }

        for ((index, h) in view.opponentHorses.withIndex()) {
            speeds[index] += sqrt(0.2 / random()).toFloat()
            speeds[index] = maxOf(0f, speeds[index] - deceleration)
            h.x += speeds[index]
            if (h.x >= displayMetrics.widthPixels - h.width) {
                lose()
                return
            }
        }

        handler.postDelayed({ update() }, 1000 / 50)
    }

    private fun onStartNumber(number: String) {
        val tv = view.preText
        tv.text = number
        tv.textColor = R.color.red
        scaleView(tv, 1f, 0f, duration = 1000)
    }

    private fun start() {
        onStartNumber("3")
        handler.postDelayed({onStartNumber("2")}, 1000)
        handler.postDelayed({onStartNumber("1")}, 2000)

        handler.postDelayed({
            view.started = true
            update()
            doEach(
                    { createMovingImage(R.drawable.races_clown, 10f, 10f) },
                    { (1 + random()) * 3000 }
            )
        }, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val candidates: ArrayList<Candidate> = intent.getParcelableArrayListExtra("candidates")
        view = RacesGameView(candidates)
        view.setContentView(this)

        useMovingImages(find<ViewGroup>(R.id.moving_image_layout))
        //MusicManager.getInstance().play(this, R.raw.) FIXME

        drawInformationDialog(getString(R.string.ladder_info_title), getString(R.string.ladder_info_message),
                { start() }, view.ankoContext)

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
