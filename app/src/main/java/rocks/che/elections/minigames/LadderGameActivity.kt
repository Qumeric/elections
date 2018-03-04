package rocks.che.elections.minigames

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.R

class LadderGameActivity : MiniGameActivity() {
    private lateinit var view: LadderGameView

    private val length = 30 // in seconds

    fun tap() {
        score++
        view.scoreText.text = score.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = LadderGameView()
        view.setContentView(this)


        drawInformationDialog(getString(R.string.ladder_info_title), getString(R.string.ladder_info_message),
                {
                    handler.postDelayed({lose()}, 130 * length.toLong())
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
