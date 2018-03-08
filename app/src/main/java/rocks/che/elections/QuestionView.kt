package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.groupToResource
import rocks.che.elections.logic.Answer
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.Opinions
import rocks.che.elections.logic.Question

val fakeQuestion = Question("statement", listOf(
        Answer("answer 1", Opinions()),
        Answer("answer 2", Opinions()),
        Answer("answer 3", Opinions())
))
class QuestionView(private val question: Question = fakeQuestion,
                   val group: String = "group", val gs: Gamestate) : DefaultView<QuestionActivity> {
    private lateinit var ankoContext: AnkoContext<QuestionActivity>

    override fun createView(ui: AnkoContext<QuestionActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageView {
                imageResource = groupToResource[group]!!
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
                gravity = Gravity.CENTER
                weightSum = 1f
                verticalLayout {
                    for (answer in question.answers) {
                        themedButton(theme = R.style.button) {
                            text = answer.statement
                            onClick {
                                ctx.startActivity<ChangeActivity>("answer" to answer, "gamestate" to gs)
                            }
                        }
                        space().lparams(width = matchParent, height = dip(30))
                    }
                }.lparams(width=0, weight = 0.75f)
            }
        }
    }
}
