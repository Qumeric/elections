package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView

class ChooseCandidateActivity : DefaultActivity() {
    private lateinit var view: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = MainView()
        view.setContentView(this)
    }
}
