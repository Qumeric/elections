package rocks.che.elections

import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.setContentView
import rocks.che.elections.logic.*

const val secretFilename = "che.rocks.secret"

class EndGameActivity: DefaultActivity() {
    private lateinit var view: EndGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            delete(this)
        } catch (e: Exception) {
            Log.d("EndGameActivity", "unable to delete save after game completion")
        }

        view = EndGameView()
        view.setContentView(this)

        save(this, "", secretFilename) // TODO: check if it is sane
    }
}