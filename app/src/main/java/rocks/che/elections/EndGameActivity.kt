package rocks.che.elections

import android.os.Bundle
import android.util.Log
import com.pixplicity.easyprefs.library.Prefs
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.secretFilename

class EndGameActivity: DefaultActivity() {
    private lateinit var view: EndGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gs: Gamestate = intent.getParcelableExtra("gamestate")

        try {
            Prefs.remove("gamestate")
        } catch (e: Exception) {
            Log.d("EndGameActivity", "unable to delete save after game completion")
        }

        view = EndGameView(gs.candidate.resource == R.drawable.candidate_putin)
        view.setContentView(this)

        Prefs.putBoolean(secretFilename, true)
    }
}