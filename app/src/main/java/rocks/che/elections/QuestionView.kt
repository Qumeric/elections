package rocks.che.elections

import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.logic.Question
import rocks.che.elections.logic.getGroupResource

class QuestionView(val question: Question, val group: String) : AnkoComponent<QuestionActivity> {
    private lateinit var ankoContext: AnkoContext<QuestionActivity>

    override fun createView(ui: AnkoContext<QuestionActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageView {
                imageResource = getGroupResource(ctx, group)
            }.lparams {
                width = dip(70)
                height = dip(70)
            }

            textView {
                gravity = Gravity.CENTER
                text = question.statement
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                textSize = dip(15).toFloat()
            }

            space().lparams(width = matchParent, height = dip(20))

            linearLayout {
                orientation = LinearLayout.VERTICAL

                for (answer in question.answers) {
                    button {
                        text = answer.statement
                        backgroundResource = R.color.blue
                        onClick {
                            ctx.startActivity(ctx.intentFor<ChangeActivity>("answer" to answer))
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    space().lparams(width = matchParent, height = dip(30))
                }
            }
        }
    }
}
