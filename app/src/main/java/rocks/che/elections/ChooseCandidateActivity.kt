package rocks.che.elections

import android.os.Bundle
import com.pixplicity.easyprefs.library.Prefs
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.secretFilename

class ChooseCandidateActivity : DefaultActivity() {
    private lateinit var view: ChooseCandidateView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ChooseCandidateView(Prefs.contains(secretFilename))
        view.setContentView(this)
    }
}
