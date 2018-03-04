package rocks.che.elections

import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.getGroupResource
import java.util.*

class ChangeView(val oldOpinions: Map<String, Int>) : AnkoComponent<ChangeActivity> {
    private lateinit var ankoContext: AnkoContext<ChangeActivity>

    override fun createView(ui: AnkoContext<ChangeActivity>) = with(ui) {
        ankoContext = ui

        var pos = 0

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                gravity = Gravity.CENTER
                textResource = R.string.support
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                textSize = dip(15).toFloat()
            }

            space().lparams(height = dip(40), width = matchParent)

            for ((group, opinion) in gamestate.opinions) {
                linearLayout {
                    gravity = Gravity.CENTER
                    if (pos % 2 == 0) {
                        backgroundResource = R.color.silver
                    }
                    imageView {
                        imageResource = getGroupResource(ctx, group)
                    }.lparams {
                        width = dip(50)
                        height = dip(50)
                    }

                    space().lparams(width = dip(15), height = matchParent)

                    textView {
                        gravity = Gravity.CENTER
                        text = String.format("%s  %d(%s)", group, opinion.value,
                                Formatter().format(Locale.US, "%+d", opinion.value - oldOpinions[group]!!))
                        typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                        textSize = dip(10).toFloat()
                    }
                }.lparams(height = dip(70), width = matchParent)
                pos++
            }
            space().lparams(height = dip(40), width = matchParent)
            button {
                textResource = R.string.next
                backgroundResource = R.color.blue
                onClick {
                    if (gamestate.isPollTime()) {
                        ctx.startActivity(ctx.intentFor<PollActivity>())
                    } else {
                        ctx.startActivity(ctx.intentFor<GameActivity>())
                    }
                }
            }.lparams(width = dip(150), height = dip(60))
        }
    }
}