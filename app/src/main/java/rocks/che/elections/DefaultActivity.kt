package rocks.che.elections

import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alert
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.saveGame

abstract class DefaultActivity: AppCompatActivity() {
    override fun onBackPressed() {
        // Do nothing
    }

    fun drawInformationDialog(_title: String, _message: String, run: () -> Unit, ui: AnkoContext<DefaultActivity>) = with(ui) {
        alert {
            title = _title
            message = _message
            positiveButton(R.string.yes_button) {
                run()
            }

            onCancelled {
                // FIXME make non-cancellable
                run()
            }
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (gamestate.candidate.name != "Fake") {
                saveGame(gamestate, this)
            }
        } catch (e: Exception) {
            Log.e("DefaultActivity", "Unable to saveGame")
        }
    }
}