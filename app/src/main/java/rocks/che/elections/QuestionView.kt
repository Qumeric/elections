package rocks.che.elections

import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.Answer
import rocks.che.elections.logic.Question
import rocks.che.elections.logic.getGroupResource

val fakeQuestion = Question("statement", listOf(
        Answer("answer 1", mapOf()),
        Answer("answer 2", mapOf()),
        Answer("answer 3", mapOf())
))
class QuestionView(private val question: Question = fakeQuestion,
                   val group: String = "group") : DefaultView<QuestionActivity> {
    private lateinit var ankoContext: AnkoContext<QuestionActivity>

    override fun createView(ui: AnkoContext<QuestionActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageView {
                imageResource = getGroupResource(group)
            }.lparams {
                width = dip(70)
                height = dip(70)
                bottomMargin = dip(10)
            }

            gameTextView(15) {
                text = question.statement
            }

            space().lparams(width = matchParent, height = dip(20))

            linearLayout {
                orientation = LinearLayout.VERTICAL

                for (answer in question.answers) {
                    button {
                        text = answer.statement
                        backgroundResource = R.color.blue
                        onClick {
                            ctx.startActivity<ChangeActivity>("answer" to answer)
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    space().lparams(width = matchParent, height = dip(30))
                }
            }
        }
    }
}
