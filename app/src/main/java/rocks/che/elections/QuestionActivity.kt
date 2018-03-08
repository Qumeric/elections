package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.Question

class QuestionActivity : DefaultActivity() {
    private lateinit var view: QuestionView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val question: Question = intent.extras.getParcelable("question")
        val group: String = intent.extras.getString("group")
        val gs: Gamestate = intent.extras.getParcelable("gamestate")

        view = QuestionView(question, group, gs)
        view.setContentView(this)
    }
}
