package rocks.che.elections

import android.os.Bundle
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Subscribe
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.helpers.GamestateUpdate
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.secretFilename

class ChooseCandidateActivity : DefaultActivity() {
    private lateinit var view: ChooseCandidateView

    @Subscribe fun updateGamestate(e: GamestateUpdate) {
        gamestate = e.g
        startActivity<HighlightsActivity>() // FIXME is it the right place?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ChooseCandidateView(Prefs.contains(secretFilename), bus)
        view.setContentView(this)
    }
}
