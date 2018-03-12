package rocks.che.elections

import android.os.Bundle
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange
import rocks.che.elections.logic.loadQuotes

class NewGameActivity: DefaultActivity() {
    private lateinit var view: NewGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gamestate = Gamestate.loadGame()
        if (gamestate != null) {
            inActivityChange = true
            startActivity<GameActivity>("gs" to gamestate)
        }

        val quote = loadQuotes(resources).shuffled().first()

        view = NewGameView(quote)
        view.setContentView(this)
        MusicManager.instance.play(this, R.raw.main_music)
    }
}