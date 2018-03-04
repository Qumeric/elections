package rocks.che.elections

import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import rocks.che.elections.logic.*

class NewGameActivity: DefaultActivity() {
    private lateinit var view: NewGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            delete(this, secretFilename)
        }

        try {
            gamestate = loadGame(this)
            // FIXME saveGame last activity
            startActivity(intentFor<GameActivity>())
        } catch (e: Exception) {
            Log.d("NewGameActivity", "unable to loadGame candidate")
        }

        view = NewGameView()
        view.setContentView(this)
    }
}