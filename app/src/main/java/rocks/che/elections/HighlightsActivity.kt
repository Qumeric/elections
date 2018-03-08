package rocks.che.elections

import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate

class HighlightsActivity : DefaultActivity() {
    private lateinit var view: HighlightsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gs: Gamestate = intent.getParcelableExtra("gamestate")

        view = HighlightsView(gs.candidate)
        view.setContentView(this)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        Handler().postDelayed({startActivity<GameActivity>()}, 5000)
    }
}