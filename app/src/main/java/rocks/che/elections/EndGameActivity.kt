package rocks.che.elections

import android.os.Bundle
import android.util.Log
import com.pixplicity.easyprefs.library.Prefs
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.secretFilename

class EndGameActivity: DefaultActivity() {
    private lateinit var view: EndGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Prefs.remove("gamestate")
        } catch (e: Exception) {
            Log.d("EndGameActivity", "unable to delete save after game completion")
        }

        view = EndGameView()
        view.setContentView(this)

        Prefs.putBoolean(secretFilename, true)
    }
}