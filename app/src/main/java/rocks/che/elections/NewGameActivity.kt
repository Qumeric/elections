package rocks.che.elections

import android.os.Bundle
import android.view.View
import im.delight.android.audio.MusicManager
import kotlinx.android.synthetic.main.activity_new_game.*
import org.jetbrains.anko.*
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange
import rocks.che.elections.logic.loadQuotes

class NewGameActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        val gamestate = Gamestate.loadGame()
        if (gamestate != null) {
            inActivityChange = true
            startActivity<GameActivity>("gs" to gamestate)
        }

        val quote = loadQuotes(resources).shuffled().first()

        quoteTV.text = "%s: %s".format(quote.author, quote.text)

        MusicManager.instance.play(this, R.raw.main_music)
    }

    fun play(v: View) {
        inActivityChange = true
        alert(getString(R.string.welcome_text), getString(R.string.welcome)) {
            yesButton { startActivity<ChooseCandidateActivity>() }
            noButton {  }
        }.show()
    }
}