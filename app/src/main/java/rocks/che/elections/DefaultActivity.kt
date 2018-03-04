package rocks.che.elections

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.AnkoContext
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.saveGame

abstract class DefaultActivity: AppCompatActivity() {
    override fun onBackPressed() {
        // Do nothing
    }

    fun drawInformationDialog(title: String, message: String, run: () -> Unit, ui: AnkoContext<DefaultActivity>) = with(ui) {
        val simpleAlert = AlertDialog.Builder(ctx).create()
        simpleAlert.setTitle(title)
        simpleAlert.setMessage(message)

        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.yes_button), {
            _, _ -> run()
        })

        simpleAlert.show()
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