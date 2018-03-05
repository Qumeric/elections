package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Question

class QuestionActivity : DefaultActivity() {
    private lateinit var view: QuestionView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val question: Question = intent.extras.getSerializable("question") as Question
        val group: String = intent.extras.getString("group")

        view = QuestionView(question, group)
        view.setContentView(this)
    }
}
