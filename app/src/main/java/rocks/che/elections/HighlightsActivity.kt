package rocks.che.elections

import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.gamestate

class HighlightsActivity : DefaultActivity() {
    private lateinit var view: HighlightsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = HighlightsView(gamestate.candidate)
        view.setContentView(this)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        Handler().postDelayed({startActivity<GameActivity>()}, 5000)
    }
}