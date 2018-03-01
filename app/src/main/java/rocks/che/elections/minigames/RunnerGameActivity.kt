package rocks.che.elections.minigames

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.R

class RunnerGameActivity : MiniGameActivity() {
    private lateinit var view: RunnerGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = RunnerGameView()
        view.setContentView(this)


        drawInformationDialog(getString(R.string.runner_info_title), getString(R.string.runner_info_message),
                {
                }, view.ankoContext)
    }
}