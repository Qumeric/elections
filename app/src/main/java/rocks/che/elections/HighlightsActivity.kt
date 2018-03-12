package rocks.che.elections

import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange

class HighlightsActivity : DefaultActivity() {
    private lateinit var view: HighlightsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gs: Gamestate = intent.getParcelableExtra("gamestate")

        view = HighlightsView(gs.candidate)
        view.setContentView(this)

        playSound(R.raw.choose_sound) // FIXME where is sound

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        Handler().postDelayed({
            inActivityChange = true
            startActivity<GameActivity>("gamestate" to gs)
        }, 5000)
    }
}